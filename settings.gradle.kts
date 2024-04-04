@file:Suppress("UnstableApiUsage")

pluginManagement {
	repositories {
		gradlePluginPortal()
		mavenCentral()
		google()
	}
}
dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
	repositories {
		mavenCentral()
		google()
		maven("https://jitpack.io")
	}
}

rootProject.name = "RickAndMorty"
include(":app")
include(":data")
include(":domain")
include(":usecases")
include(":common")
