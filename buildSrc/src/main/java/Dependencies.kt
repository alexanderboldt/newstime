object Deps {
    object Config {
        val applicationId = "com.alex.newstime"
        val minSdk = 21
        val sdk = 30
    }

    object AndroidX {
        val core = "androidx.core:core-ktx:1.3.0"
        val appCompat = "androidx.appcompat:appcompat:1.1.0"
        val material = "com.google.android.material:material:1.1.0"
        val recyclerView = "androidx.recyclerview:recyclerview:1.0.0"
        val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.0"

        val lifecycleVersion = "2.2.0"
        val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime:$lifecycleVersion"
        val lifecycleRuntimeExtensions = "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion"
        val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:$lifecycleVersion"
        val lifecycleCommonJava = "androidx.lifecycle:lifecycle-common-java8:$lifecycleVersion"
        val lifecycleCompiler = "androidx.lifecycle:lifecycle-compiler:$lifecycleVersion"
        val lifecycleViewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion"

        val fragmentsExt = "androidx.fragment:fragment-ktx:1.2.5"

        val roomVersion = "2.3.0-alpha02"
        val room = "androidx.room:room-runtime:$roomVersion"
        val roomCompiler = "androidx.room:room-compiler:$roomVersion"
        val roomRxJava = "androidx.room:room-rxjava3:$roomVersion"
    }

    object Test {
        val junit = "androidx.test.ext:junit:1.0.0"
        val testRunner = "androidx.test:runner:1.1.0"
        val mockitoCore = "org.mockito:mockito-core:3.0.0"
        val mockitoAndroid = "org.mockito:mockito-android:3.0.0"

        val coreTesting = "androidx.arch.core:core-testing:2.0.0-rc01"

        val activityTestRule = "androidx.test:rules:1.1.0"
        val espressoCore = "androidx.test.espresso:espresso-core:3.1.0"

        val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.0-RC2"
    }

    object Libs {
        val androidCore = "com.github.alexanderboldt:androidcore:2.0.0"

        val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.0-RC2"

        val timber = "com.jakewharton.timber:timber:4.7.1"

        val retrofitVersion = "2.9.0"
        val retrofit = "com.squareup.retrofit2:retrofit:$retrofitVersion"
        val retrofitMoshiConverter = "com.squareup.retrofit2:converter-moshi:$retrofitVersion"
        val retrofitRxAdapter = "com.squareup.retrofit2:adapter-rxjava3:$retrofitVersion"
        val okHttpLogging = "com.github.ihsanbal:LoggingInterceptor:3.0.0"

        val moshiVersion = "1.10.0"
        val moshi = "com.squareup.moshi:moshi:$moshiVersion"
        val moshiCodeGen = "com.squareup.moshi:moshi-kotlin-codegen:$moshiVersion"

        val glideVersion = "4.11.0"
        val glide = "com.github.bumptech.glide:glide:$glideVersion"
        val glideCompiler = "com.github.bumptech.glide:compiler:$glideVersion"
        val glideOkHttpIntegration = "com.github.bumptech.glide:okhttp3-integration:$glideVersion"
        val glideTransformations = "jp.wasabeef:glide-transformations:4.0.0"

        val liveEvent = "com.github.hadilq.liveevent:liveevent:1.2.0"

        val parcelVersion = "1.1.12"
        val parcel = "org.parceler:parceler-api:$parcelVersion"
        val parcelCompiler = "org.parceler:parceler:$parcelVersion"

        val rxjava = "io.reactivex.rxjava3:rxjava:3.0.6"
        val rxandroid = "io.reactivex.rxjava3:rxandroid:3.0.0"

        val rxbindingVersion = "4.0.0"
        val rxbinding = "com.jakewharton.rxbinding4:rxbinding:$rxbindingVersion"
        val rxbindingAppcompat = "com.jakewharton.rxbinding4:rxbinding-appcompat:$rxbindingVersion"
        val rxbindingSwiperefreshlayout = "com.jakewharton.rxbinding4:rxbinding-swiperefreshlayout:$rxbindingVersion"

        val leakCanary = "com.squareup.leakcanary:leakcanary-android:2.3"

        val koinVersion = "2.1.6"
        val koin = "org.koin:koin-android:$koinVersion"
        val koinViewModel = "org.koin:koin-androidx-viewmodel:$koinVersion"
    }
}