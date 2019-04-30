import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
}
apply {
    plugin("kotlin-android")
}

android {
    compileSdkVersion(Versions.sdk)
    defaultConfig {
        applicationId = "com.alex.newstime"
        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.sdk)
        versionCode = Versions.code
        versionName = Versions.name

        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"

        vectorDrawables.useSupportLibrary = true
    }
    buildTypes {
        getByName("debug") {
            isDebuggable = true
            isMinifyEnabled = false

            applicationIdSuffix = ".development"

            buildConfigField("String", "BASE_URL", "\"${LocalProperties.BASE_URL}\"")
            buildConfigField("String", "API_KEY", "\"${LocalProperties.API_KEY}\"")
        }

        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = true

            signingConfig = signingConfigs.getByName("debug")

            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    lintOptions {
        isAbortOnError = false
    }

    dataBinding {
        isEnabled = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

repositories {
    maven { setUrl("https://www.jitpack.io") }
    mavenCentral()
}

dependencies {

    // testing
    testImplementation(Test.junit)
    testImplementation(Test.testRunner)

    // android-testing
    androidTestImplementation(Test.mockitoCore)
    androidTestImplementation(Test.mockitoAndroid)
    androidTestImplementation(Test.coreTesting)
    androidTestImplementation(Test.testRunner)

    // support
    implementation(Support.appCompat)
    implementation(Support.recyclerView)
    implementation(Support.design)
    implementation(Support.constraintLayout)

    implementation(Support.lifecycle)
    kapt(Support.lifecycleCompiler)

    // 3rd-party libraries

    // network
    implementation(Libs.retrofit)
    implementation(Libs.retrofitMoshiConverter)
    implementation(Libs.rxretrofitAdapter)
    implementation(Libs.okHttpLogging)
    implementation(Libs.moshi)

    // image
    implementation(Libs.glide)

    // fragments-alternative
    implementation(Libs.conductor)
    implementation(Libs.conductorSupport)
    implementation(Libs.conductorViewModel)

    implementation(Libs.parcel)
    kapt(Libs.parcelCompiler)

    // reactive

    implementation(Libs.rxjava)
    implementation(Libs.rxandroid)

    implementation(Libs.rxbinding)
    implementation(Libs.rxbindingAppcompat)
    implementation(Libs.rxbindingSupport)

    // leak-detection
    debugImplementation(Libs.leakCanaryDebug)
    releaseImplementation(Libs.leakCanaryRelease)
}