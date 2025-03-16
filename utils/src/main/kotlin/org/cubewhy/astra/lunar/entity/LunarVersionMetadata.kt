package org.cubewhy.astra.lunar.entity

import kotlinx.serialization.Serializable

@Serializable
data class LunarVersionMetadata(
    val versions: List<Version>,
    val branchReset: Boolean,
    val recommendedLibraries: List<RecommendedLibrary>
)

@Serializable
data class Version(
    val id: String,
    val name: String,
    val description: String,
    val carousel: List<CarouselItem>,
    val default: Boolean,
    val releaseDate: String,
    val images: VersionImages,
    val subversions: List<LunarSubversion>
)

@Serializable
data class CarouselItem(
    val image: String,
    val title: String
)

@Serializable
data class VersionImages(
    val background: String,
    val foreground: String,
    val icon: String
)

@Serializable
data class LunarSubversion(
    val id: String,
    val default: Boolean,
    val assets: LunarSubversionAssets,
    val modules: List<LunarSubversionModule>
)

@Serializable
data class LunarSubversionAssets(
    val sha1: String,
    val url: String,
    val id: String
)

@Serializable
data class LunarSubversionModule(
    val id: String,
    val default: Boolean,
    val name: String,
    val description: String,
    val credits: String,
    val image: String,
    val launchButtonImage: String?,
    val launchButtonName: String,
    val loaders: List<String>,
    val sort: Int
)

@Serializable
data class RecommendedLibrary(
    val name: String,
    val version: String
)

