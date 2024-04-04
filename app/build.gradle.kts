@file:Suppress("UnstableApiUsage")

plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.android.kotlin)
	alias(libs.plugins.serialization)
	id("kotlin-parcelize")
	alias(libs.plugins.ksp)
	alias(libs.plugins.detekt)
	alias(libs.plugins.google.dagger.hilt.android)
}

android {
	signingConfigs {
		create("release") {
			storeFile = file(findProperty("RICKANDMORTY_RELEASE_STORE_FILE").toString())
			storePassword = findProperty("RICKANDMORTY_RELEASE_STORE_PASSWORD").toString()
			keyAlias = findProperty("RICKANDMORTY_RELEASE_KEY_ALIAS").toString()
			keyPassword = findProperty("RICKANDMORTY_RELEASE_KEY_PASSWORD").toString()
		}
	}

	namespace = "com.alvaroquintana.rickandmorty"
	compileSdk = libs.versions.compileSdkVersion.get().toInt()

	defaultConfig {
		minSdk = libs.versions.minSdkVersion.get().toInt()
		targetSdk = libs.versions.targetSdkVersion.get().toInt()
		versionCode = libs.versions.versionCode.get().toInt()
		versionName = libs.versions.versionName.toString()

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		vectorDrawables {
			useSupportLibrary = true
		}
		signingConfig = signingConfigs.getByName("release")
	}

	buildTypes {
		debug {
			isMinifyEnabled = false
			isDebuggable = true
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
		release {
			isMinifyEnabled = true
			isDebuggable = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}
	kotlinOptions {
		jvmTarget = "17"
	}
	buildFeatures {
		compose = true
	}
	composeOptions {
		kotlinCompilerExtensionVersion = libs.versions.androidx.compose.compiler.get()
	}

	//Needed for Mockk
	testOptions { packaging { jniLibs { useLegacyPackaging = true } } }

	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}


}

dependencies {
	implementation(project(":common"))
	implementation(project(":data"))
	implementation(project(":domain"))
	implementation(project(":usecases"))

	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.compose.ui.ui)
	implementation(libs.androidx.compose.ui.graphics)
	implementation(libs.androidx.compose.ui.text)
	implementation(libs.androidx.compose.ui.unit)
	implementation(libs.androidx.compose.ui.tooling)
	implementation(libs.androidx.compose.ui.toolingPreview)
	implementation(libs.androidx.compose.material3)
	implementation(libs.androidx.navigation.common)
	implementation(libs.androidx.navigation.compose)

	implementation(libs.google.dagger.hilt.android)
	add("ksp", libs.androidx.hilt.compiler)
	kspTest(libs.androidx.hilt.compiler)
	add("ksp", libs.google.dagger.hilt.androidCompiler)
	kspTest(libs.google.dagger.hilt.androidCompiler)
	kspAndroidTest(libs.google.dagger.hilt.androidCompiler)
	implementation(libs.androidx.hilt.navigation.compose)
	implementation(libs.kotlinResult.result)
	implementation(libs.kotlinResult.coroutines)
	implementation(libs.inject)

	implementation(libs.androidx.activity.compose)
	implementation(libs.coil)
	//detektPlugins(Libs.Detekt.rulesCompose)
	implementation(libs.lottie.compose)

}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	kotlinOptions.freeCompilerArgs += "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	kotlinOptions.freeCompilerArgs += "-opt-in=androidx.compose.ui.test.ExperimentalTestApi"
}
