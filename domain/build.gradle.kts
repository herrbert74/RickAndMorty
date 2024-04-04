plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.android.kotlin)
}

android {
	namespace = "com.alvaroquintana.domain"
	compileSdk = 34

	defaultConfig {
		minSdk = 24

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		consumerProguardFiles("consumer-rules.pro")
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}

	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}

}

dependencies {
	implementation(project(":common"))
	implementation(libs.kotlinx.coroutines.core)
	implementation(libs.kotlinResult.result)
	implementation(libs.inject)
}
