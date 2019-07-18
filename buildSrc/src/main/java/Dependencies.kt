object Deps {
    object Config {
        val applicationId = "com.alex.newstime"
        val minSdk = 21
        val sdk = 29
    }

    object AndroidX {
        val appCompat = "androidx.appcompat:appcompat:1.0.2"
        val material = "com.google.android.material:material:1.1.0-alpha06"
        val recyclerView = "androidx.recyclerview:recyclerview:1.0.0"
        val constraintLayout = "androidx.constraintlayout:constraintlayout:1.1.3"

        val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime:2.0.0"
        val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:2.0.0"
        val lifecycleCommonJava = "androidx.lifecycle:lifecycle-common-java8:2.0.0"
        val lifecycleCompiler = "androidx.lifecycle:lifecycle-compiler:2.0.0"
        val lifecycleViewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.1.0-beta01"

        val room = "androidx.room:room-runtime:2.1.0"
        val roomCompiler = "androidx.room:room-compiler:2.1.0"
        val roomRxJava = "androidx.room:room-rxjava2:2.1.0"
    }

    object Test {
        val junit = "androidx.test.ext:junit:1.0.0"
        val testRunner = "androidx.test:runner:1.1.0"
        val mockitoCore = "org.mockito:mockito-core:2.6.3"
        val mockitoAndroid = "org.mockito:mockito-android:2.6.3"

        val coreTesting = "androidx.arch.core:core-testing:2.0.0-rc01"

        val activityTestRule = "androidx.test:rules:1.1.0"
        val espressoCore = "androidx.test.espresso:espresso-core:3.1.0"
    }

    object Libs {
        val exoplayer = "com.google.android.exoplayer:exoplayer:2.10.1"

        val dagger = "com.google.dagger:dagger:2.23.2"
        val daggerCompiler = "com.google.dagger:dagger-compiler:2.23.2"

        val ahbottomnavigation = "com.aurelhubert:ahbottomnavigation:2.3.4"

        val timber = "com.jakewharton.timber:timber:4.7.1"

        val threetenabp = "com.jakewharton.threetenabp:threetenabp:1.2.1"

        val retrofit = "com.squareup.retrofit2:retrofit:2.6.0"
        val retrofitMoshiConverter = "com.squareup.retrofit2:converter-moshi:2.6.0"
        val retrofitRxAdapter = "com.squareup.retrofit2:adapter-rxjava2:2.6.0"
        val okHttpLogging = "com.github.ihsanbal:LoggingInterceptor:3.0.0"

        val moshi = "com.squareup.moshi:moshi-kotlin:1.8.0"

        val glide = "com.github.bumptech.glide:glide:4.9.0"
        val glideCompiler = "com.github.bumptech.glide:compiler:4.9.0"
        val glideOkHttpIntegration = "com.github.bumptech.glide:okhttp3-integration:4.9.0"
        val glideTransformations = "jp.wasabeef:glide-transformations:4.0.0"

        val conductor = "com.bluelinelabs:conductor:2.1.5"
        val conductorSupport = "com.bluelinelabs:conductor-support:2.1.5"
        val conductorViewModel = "com.github.miquelbeltran:conductor-viewmodel:1.0.3"

        val parcel = "org.parceler:parceler-api:1.1.12"
        val parcelCompiler = "org.parceler:parceler:1.1.12"

        val rxjava = "io.reactivex.rxjava2:rxjava:2.2.10"
        val rxandroid = "io.reactivex.rxjava2:rxandroid:2.1.1"

        val rxbinding = "com.jakewharton.rxbinding2:rxbinding-kotlin:2.2.0"
        val rxbindingAppcompat = "com.jakewharton.rxbinding2:rxbinding-appcompat-v7-kotlin:2.2.0"
        val rxbindingSupport = "com.jakewharton.rxbinding2:rxbinding-support-v4-kotlin:2.2.0"

        val leakCanary = "com.squareup.leakcanary:leakcanary-android:2.0-alpha-3"

        val pandora = "com.github.whataa:pandora:androidx_v2.0.3"
        val pandoraNoOp = "com.github.whataa:pandora-no-op:v2.0.3"
    }
}