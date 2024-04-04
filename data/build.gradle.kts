plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.android.kotlin)
	alias(libs.plugins.ksp)
	alias(libs.plugins.google.dagger.hilt.android)
}

android {
	namespace = "com.alvaroquintana.data"
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
	implementation(project(":domain"))

	implementation(libs.androidx.room.common)
	implementation(libs.androidx.room.runtime)
	implementation(libs.androidx.room.ktx)
	implementation(libs.kotlinx.coroutines.core)
	implementation(libs.squareUp.retrofit2.retrofit)
	implementation(libs.inject)
	implementation(libs.kotlinResult.result)
	implementation(libs.kotlinResult.coroutines)
	implementation(libs.squareUp.okhttp3.loggingInterceptor)
	implementation(libs.squareUp.retrofit2.retrofit)
	implementation(libs.squareUp.retrofit2.converterGson)

	implementation(libs.google.dagger.hilt.android)
	kspTest(libs.androidx.hilt.compiler)
	kspTest(libs.google.dagger.hilt.androidCompiler)
	kspAndroidTest(libs.google.dagger.hilt.androidCompiler)

	add("ksp", libs.androidx.hilt.compiler)
	add("ksp", libs.google.dagger.hilt.androidCompiler)
	add("ksp", libs.androidx.room.compiler)

}
