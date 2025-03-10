package org.cubewhy.utils

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.*
import java.io.File
import java.io.OutputStream
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

    suspend fun downloadFile(url: String, outputFile: File, numThreads: Int) {
        logger.info { "Starting download: $url -> ${outputFile.absolutePath}" }

        val totalSize = getFileSize(url)
        if (totalSize == 0L) {
            logger.warn { "Server does not support range requests. Falling back to single-threaded download." }
            downloadSingleThread(url, outputFile)
            return
        }

        val partSize = totalSize / numThreads
        val jobs = mutableListOf<Job>()
        val partFiles = mutableListOf<File>()

        var downloadedBytes = 0L
        val progressMutex = Any()

        for (i in 0 until numThreads) {
            val startByte = i * partSize
            val endByte = if (i == numThreads - 1) totalSize - 1 else (startByte + partSize - 1)

            val partFile = File(outputFile.parent, "part_${UUID.randomUUID().toString()}")
            partFiles.add(partFile)

            var lastProgress = .0

            jobs += CoroutineScope(Dispatchers.IO).launch {
                downloadPart(url, partFile, startByte, endByte) { partDownloaded ->
                    synchronized(progressMutex) {
                        downloadedBytes += partDownloaded
                        val progress = (downloadedBytes * 100 / totalSize).toDouble()
                        if (lastProgress != progress) {
                            logger.info { "Download progress (${outputFile.path}): ${String.format("%.1f", progress)}%" }
                            lastProgress = progress
                        }
                    }
                }
            }
        }

        jobs.forEach { it.join() }

        logger.info { "Merging downloaded parts of file ${outputFile.path}" }
        if (outputFile.exists()) outputFile.delete()
        mergeFiles(partFiles, outputFile.outputStream())
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

    private fun downloadSingleThread(url: String, outputFile: File) {
        runBlocking {
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
                            val progress = (downloaded * 100 / (response.contentLength() ?: 1)).toInt()
                            logger.info { "Download progress: $progress%" }
                        }
                    }
                }
            }
            logger.info { "Single-threaded download completed: ${outputFile.absolutePath}" }
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

    fun close() {
        client.close()
    }
}
