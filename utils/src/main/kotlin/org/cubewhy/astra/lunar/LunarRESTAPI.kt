package org.cubewhy.astra.lunar

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import org.cubewhy.astra.lunar.entity.*
import org.cubewhy.utils.Platform
import java.net.URI
import java.util.*

// https://editor-next.swagger.io/?url=https%3A%2F%2Flunarclient.top%2Fopenapi%2Fopenapi-latest.json

class LunarRESTAPI(private val baseUri: URI) {

    companion object {
        private val installationId = UUID.randomUUID().toString() // fake installation id
        private val client = HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json()
            }
            engine {
                config {
                    followRedirects(true)
                }
            }
        }
    }


    suspend fun lunarVersionMetadata(branch: String?): LunarVersionMetadata {
        // /launcher/metadata/versions/lunar
        val uri = baseUri.resolve("/launcher/metadata/versions/lunar")
        val response = client.get(uri.toURL()) {
            parameter("installation_id", installationId)
            if (branch != null) {
                parameter("branch", branch)
            }
        }
        if (!response.status.isSuccess()) {
            throw IllegalStateException("Failed to lunar version response: ${response.status.value} (${response.status.description})")
        }
        // parse json
        return response.body()
    }

    suspend fun launchLunar(
        version: String,
        module: String,
        branch: String,
        profile: LauncherProfile,
        launcherVersion: String = "10.0.0",
        canaryPreference: CanaryPreference = CanaryPreference.OPT_IN,
        os: Platform = Platform.current
    ): LaunchResponse {
        val uri = baseUri.resolve("/launcher/launch")

        val data = LaunchRequest(
            installationId = installationId,
            os = os.os.requestName,
            osRelease = os.version,
            arch = os.arch.requestName,
            launcherVersion = launcherVersion,
            canaryPreference = canaryPreference.name,
            launchType = "OFFLINE",
            branch = branch,
            version = version,
            module = module,
            args = listOf(), // unknown field
            profile = profile
        )

        val response = client.post(uri.toURL()) {
            contentType(ContentType.Application.Json)
            setBody(data)
        }
        if (!response.status.isSuccess()) {
            throw IllegalStateException("Failed to fetch lunar launch response: ${response.status.value} (${response.status.description})")
        }
        // parse json
        return response.body()
    }
}
