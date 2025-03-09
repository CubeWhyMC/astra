package org.cubewhy.utils

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import java.io.OutputStream

/**
 * Utility object for downloading large files using multiple threads (via coroutines).
 *
 * This object provides a method to download files in parallel by splitting the file into multiple parts
 * and downloading each part concurrently.
 */
object DownloadUtils {
    private val logger = KotlinLogging.logger {}

    // HttpClient instance using OkHttp engine
    private val client = HttpClient(OkHttp) {
        engine {
            config {
                followRedirects(true) // Ensure redirects are followed
            }
        }
    }

    /**
     * Downloads a file in multiple parts using coroutines.
     *
     * This method splits the file into multiple parts and downloads each part concurrently in separate
     * coroutines. Once all parts are downloaded, they are combined into the final file.
     *
     * @param url The URL of the file to download.
     * @param outputFile The target file where the downloaded content will be saved.
     * @param numThreads The number of threads (coroutines) to use for downloading the file.
     * @throws Exception if the file size cannot be retrieved or any other download-related issue occurs.
     */
    suspend fun downloadFile(url: String, outputFile: File, numThreads: Int) {
        // Get the total size of the file from the URL
        val totalSize = getFileSize(url)

        if (totalSize == 0L) {
            throw Exception("Failed to retrieve file size.") // Throw an exception if the file size is invalid
        }

        // Calculate the size of each part for downloading
        val partSize = totalSize / numThreads
        val jobs = mutableListOf<Job>() // List to hold all download jobs (coroutines)
        val partFiles = mutableListOf<File>()

        // Launch coroutines to download each part of the file
        for (i in 0 until numThreads) {
            val startByte = i * partSize
            val endByte = if (i == numThreads - 1) totalSize - 1 else (startByte + partSize - 1)

            jobs += CoroutineScope(Dispatchers.IO).launch {
                val part = downloadPart(url, outputFile, startByte, endByte, i)
                partFiles.add(part)
            }
        }

        // Wait for all download parts to finish
        jobs.forEach { it.join() }

        // join files
        mergeFiles(partFiles, outputFile.outputStream())
    }

    /**
     * Retrieves the size of the file from the given URL using a HEAD request.
     *
     * @param url The URL of the file.
     * @return The size of the file in bytes, or 0 if the size could not be retrieved.
     */
    private suspend fun getFileSize(url: String): Long {
        val response: HttpResponse = client.head(url) // Perform a HEAD request to get the file size
        return response.headers["Content-Length"]?.toLong() ?: 0L
    }

    /**
     * Downloads a part of the file, starting from [startByte] to [endByte], and writes it to a temporary file.
     *
     * @param url The URL of the file to download.
     * @param outputFile The target file where the downloaded content will be saved.
     * @param startByte The starting byte position of the part to download.
     * @param endByte The ending byte position of the part to download.
     * @param partIndex The index of this part, used for creating temporary files and logs.
     *
     * @return part
     */
    private suspend fun downloadPart(
        url: String,
        outputFile: File,
        startByte: Long,
        endByte: Long,
        partIndex: Int
    ): File {
        val rangeHeader = "bytes=$startByte-$endByte" // Range header to specify which part of the file to download
        val response: HttpResponse = client.get(url) {
            headers {
                append("Range", rangeHeader) // Add the range header to the request
            }
        }

        // Temporary file for the downloaded part
        val partFile = File(outputFile.parent, "part_$partIndex")
        partFile.writeBytes(response.readRawBytes()) // Write the downloaded part to the temporary file

        logger.info { "Part $partIndex downloaded." } // Log the completion of this part
        return partFile
    }

    private fun mergeFiles(partFiles: List<File>, outputStream: OutputStream) {
        // Ensure all parts are downloaded first
        synchronized(this) {
            partFiles.forEach { partFile ->
                outputStream.write(partFile.readBytes())
                partFile.delete() // Delete the temporary part file
            }
        }
    }

    /**
     * Closes the HttpClient to release resources.
     */
    fun close() {
        client.close()
    }
}
