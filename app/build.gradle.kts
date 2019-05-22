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
    compileSdkVersion(Deps.Config.sdk)
    defaultConfig {
        applicationId = Deps.Config.applicationId
        minSdkVersion(Deps.Config.minSdk)
        targetSdkVersion(Deps.Config.sdk)
        versionCode = Deps.Config.code
        versionName = Deps.Config.name

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

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
    testImplementation(Deps.Test.junit)
    testImplementation(Deps.Test.testRunner)

    // android-testing
    androidTestImplementation(Deps.Test.mockitoCore)
    androidTestImplementation(Deps.Test.mockitoAndroid)
    androidTestImplementation(Deps.Test.coreTesting)
    androidTestImplementation(Deps.Test.testRunner)

    // support
    implementation(Deps.AndroidX.appCompat)
    implementation(Deps.AndroidX.material)
    implementation(Deps.AndroidX.recyclerView)
    implementation(Deps.AndroidX.constraintLayout)

    implementation(Deps.AndroidX.lifecycleRuntime)
    implementation(Deps.AndroidX.lifecycleExtensions)
    implementation(Deps.AndroidX.lifecycleCommonJava)
    kapt(Deps.AndroidX.lifecycleCompiler)

    // 3rd-party libraries

    // database
    implementation(Deps.Libs.room)
    kapt(Deps.Libs.roomCompiler)
    implementation(Deps.Libs.roomRxJava)

    // network
    implementation(Deps.Libs.retrofit)
    implementation(Deps.Libs.retrofitMoshiConverter)
    implementation(Deps.Libs.retrofitRxAdapter)
    implementation(Deps.Libs.okHttpLogging)
    implementation(Deps.Libs.moshi)

    // image
    implementation(Deps.Libs.glide)

    // fragments-alternative
    implementation(Deps.Libs.conductor)
    implementation(Deps.Libs.conductorSupport)
    implementation(Deps.Libs.conductorViewModel)

    // custom-font
    implementation(Deps.Libs.calligraphy)

    // model-parcel
    implementation(Deps.Libs.parcel)
    kapt(Deps.Libs.parcelCompiler)

    // reactive
    implementation(Deps.Libs.rxjava)
    implementation(Deps.Libs.rxandroid)

    // view-binding
    implementation(Deps.Libs.rxbinding)
    implementation(Deps.Libs.rxbindingAppcompat)
    implementation(Deps.Libs.rxbindingSupport)

    // leak-detection
    debugImplementation(Deps.Libs.leakCanaryDebug)
    releaseImplementation(Deps.Libs.leakCanaryRelease)
}