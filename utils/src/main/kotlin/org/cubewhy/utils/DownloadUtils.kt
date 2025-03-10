package org.cubewhy.utils

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.*
import org.cubewhy.astra.events.*
import java.io.File
import java.io.OutputStream
import java.security.MessageDigest
import java.util.*

object DownloadUtils {
    private val logger = KotlinLogging.logger {}

    private val client = HttpClient(OkHttp) {
        engine {
            config {
                followRedirects(true)
            }
        }
    }

    suspend fun downloadFile(
        url: String,
        outputFile: File,
        numThreads: Int,
        expectedHash: String? = null,
        hashAlgorithm: HashAlgorithm = HashAlgorithm.SHA256
    ) {
        logger.info { "Starting download: $url -> ${outputFile.absolutePath}" }
        // push event
        val fileId = UUID.randomUUID().toString()

        val totalSize = getFileSize(url)
        if (totalSize == 0L) {
            logger.warn { "Server does not support range requests. Falling back to single-threaded download." }
            downloadSingleThread(fileId, url, outputFile)
            return
        }
        EventBus.post(StartDownloadEvent(fileId, outputFile, numThreads))

        val partSize = totalSize / numThreads
        val jobs = mutableListOf<Job>()
        val partFiles = mutableListOf<File>()

        var downloadedBytes = 0L
        val progressMutex = Any()

        for (i in 0 until numThreads) {
            val startByte = i * partSize
            val endByte = if (i == numThreads - 1) totalSize - 1 else (startByte + partSize - 1)

            val partFile = File(outputFile.parent, "part_${UUID.randomUUID()}")
            partFiles.add(partFile)

            var lastProgress = .0

            jobs += CoroutineScope(Dispatchers.IO).launch {
                downloadPart(url, partFile, startByte, endByte) { partDownloaded ->
                    synchronized(progressMutex) {
                        downloadedBytes += partDownloaded
                        val progress = (downloadedBytes * 100 / totalSize).toDouble()
                        // push update progress event
                        EventBus.post(UpdateDownloadProgressEvent(fileId, progress))
                        if (lastProgress != progress) {
                            logger.debug {
                                "Download progress (${outputFile.path}): ${
                                    String.format(
                                        "%.1f",
                                        progress
                                    )
                                }%"
                            }
                            lastProgress = progress
                        }
                    }
                }
            }
        }

        try {
            jobs.forEach { it.join() }
        } catch (e: Exception) {
            logger.error(e) { "Error while downloading $url" }
            EventBus.post(FinishDownloadEvent(fileId, DownloadStatus.FAILURE))
            return
        }

        logger.info { "Merging downloaded parts of file ${outputFile.path}" }
        if (outputFile.exists()) outputFile.delete()
        mergeFiles(partFiles, outputFile.outputStream())

        // Verify hash if expectedHash is provided
        if (expectedHash != null) {
            if (!verifyHash(outputFile, expectedHash, hashAlgorithm)) {
                logger.error { "Hash verification failed for file ${outputFile.absolutePath}" }
                EventBus.post(FinishDownloadEvent(fileId, DownloadStatus.FAILURE))
                return
            }
        }

        // push download completed
        EventBus.post(FinishDownloadEvent(fileId, DownloadStatus.SUCCESS))
        logger.info { "Download completed: ${outputFile.absolutePath}" }
    }

    private suspend fun getFileSize(url: String): Long {
        val response: HttpResponse = client.head(url)
        if (!response.headers.contains("Accept-Ranges")) {
            return 0L
        }
        return response.headers["Content-Length"]?.toLong() ?: 0L
    }

    private suspend fun downloadPart(
        url: String,
        outputFile: File,
        startByte: Long,
        endByte: Long,
        onProgress: (Long) -> Unit
    ) {
        val rangeHeader = "bytes=$startByte-$endByte"
        logger.info { "Downloading part: $rangeHeader -> ${outputFile.name}" }

        val response: HttpResponse = client.get(url) {
            headers {
                append("Range", rangeHeader)
            }
        }

        if (!outputFile.parentFile.exists()) outputFile.parentFile.mkdirs()

        outputFile.outputStream().use { out ->
            val buffer = ByteArray(8192)
            var downloaded = 0L
            response.bodyAsChannel().apply {
                while (!isClosedForRead) {
                    val read = readAvailable(buffer)
                    if (read > 0) {
                        out.write(buffer, 0, read)
                        downloaded += read
                        onProgress(read.toLong())
                    }
                }
            }
        }
        logger.info { "Completed part: ${outputFile.name}" }
    }

    private fun downloadSingleThread(fileId: String, url: String, outputFile: File) {
        EventBus.post(StartDownloadEvent(fileId, outputFile, 1)) // start download
        runBlocking {
            try {
                val response: HttpResponse = client.get(url)
                outputFile.outputStream().use { out ->
                    val buffer = ByteArray(8192)
                    var downloaded = 0L
                    response.bodyAsChannel().apply {
                        while (!isClosedForRead) {
                            val read = readAvailable(buffer)
                            if (read > 0) {
                                out.write(buffer, 0, read)
                                downloaded += read
                                val progress = (downloaded * 100 / (response.contentLength() ?: 1)).toDouble()
                                EventBus.post(UpdateDownloadProgressEvent(fileId, progress))
                                logger.debug { "Download progress: ${String.format("%.1f", progress)}%" }
                            }
                        }
                    }
                }
                logger.info { "Single-threaded download completed: ${outputFile.absolutePath}" }
            } catch (e: Exception) {
                logger.error(e) { "Error while downloading $url" }
                EventBus.post(FinishDownloadEvent(fileId, DownloadStatus.FAILURE))
            }
        }
    }

    private fun mergeFiles(partFiles: List<File>, outputStream: OutputStream) {
        synchronized(this) {
            partFiles.forEach { partFile ->
                outputStream.write(partFile.readBytes())
                partFile.delete()
            }
        }
    }

    private fun verifyHash(file: File, expectedHash: String, algorithm: HashAlgorithm): Boolean {
        val digest = MessageDigest.getInstance(algorithm.algorithmName)
        file.inputStream().use { inputStream ->
            val buffer = ByteArray(8192)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                digest.update(buffer, 0, bytesRead)
            }
        }
        val computedHash = digest.digest().joinToString("") { "%02x".format(it) }
        return computedHash.equals(expectedHash, ignoreCase = true)
    }

    fun close() {
        client.close()
    }
}