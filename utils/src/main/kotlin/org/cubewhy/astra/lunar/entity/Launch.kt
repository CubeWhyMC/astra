package org.cubewhy.astra.lunar.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LaunchRequest(
    @SerialName("installation_id") val installationId: String,
    val os: String,
    @SerialName("os_release") val osRelease: String,
    val arch: String,
    @SerialName("launcher_version") val launcherVersion: String?,
    @SerialName("canary_preference") val canaryPreference: String?,
    @SerialName("launch_type") val launchType: String = "OFFLINE",
    val branch: String,
    val version: String,
    val args: List<String> = emptyList(),
    val module: String = "lunar",
    val profile: LauncherProfile
)

@Serializable
data class LauncherProfile(
    val id: String,
    val name: String,
    val modrinth: ModrinthData?
)

@Serializable
data class ModrinthData(
    val id: String,
    @SerialName("version_id") val versionId: String
)

@Serializable
data class LaunchResponse(
    val success: Boolean,
    val error: ErrorResponse? = null,
    val updateAssets: Boolean = false,
    val launchTypeData: LaunchTypeData? = null,
    val licenses: List<LicenseData>? = emptyList(),
    val textures: TexturesData? = null,
    val jre: JreData? = null,
    val ui: UiData? = null,
    val canaryToken: String? = null
)

@Serializable
data class ErrorResponse(
    val code: String,
    val short: String,
    val message: String
)

@Serializable
data class LaunchTypeData(
    val mainClass: String,
    val artifacts: List<ArtifactData>
)

@Serializable
data class ArtifactData(
    val name: String,
    val sha1: String,
    val url: String,
    val differentialUrl: String?,
    val type: String,
    val size: Long,
    val mtime: Long
)

@Serializable
data class LicenseData(
    val file: String,
    val url: String,
    val sha1: String,
    val size: Long,
    val mtime: Long
)

@Serializable
data class TexturesData(
    val indexUrl: String,
    val indexSha1: String,
    val baseUrl: String
)

@Serializable
data class JreData(
    val download: JreDownloadData,
    val executablePathInArchive: List<String>,
    val extraArguments: List<String>,
    val javawDownload: String? = null,
    val javawExeChecksum: String? = null,
    val folderChecksum: String
)

@Serializable
data class JreDownloadData(
    val url: String,
    val fallbackUrl: String?,
    val extension: String
)

@Serializable
data class UiData(
    val sourceUrl: String,
    val sourceSha1: String,
    val assets: UiAssetsData
)

@Serializable
data class UiAssetsData(
    val baseUrl: String,
    val indexUrl: String,
    val indexSha1: String
)
