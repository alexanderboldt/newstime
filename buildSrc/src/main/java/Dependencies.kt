object Versions {
    val minSdk = 21
    val sdk = 28
    val code = 1
    val name = "1.0"
}

object Support {
    private const val supportVersion = "28.0.0"
    private const val constraintLayoutVersion = "1.1.3"
    private const val lifecycleVersion = "1.1.1"

    val appCompat = "com.android.support:appcompat-v7:$supportVersion"
    val annotations = "com.android.support:support-annotations:$supportVersion"
    val design = "com.android.support:design:$supportVersion"
    val recyclerView = "com.android.support:recyclerview-v7:$supportVersion"
    val constraintLayout = "com.android.support.constraint:constraint-layout:$constraintLayoutVersion"

    val lifecycle = "android.arch.lifecycle:extensions:$lifecycleVersion"
    val lifecycleCompiler = "android.arch.lifecycle:compiler:$lifecycleVersion"
}

object Test {
    val junit = "junit:junit:4.12"
    val testRunner = "com.android.support.test:runner:1.0.2"
    val mockitoCore = "org.mockito:mockito-core:2.24.5"
    val mockitoAndroid = "org.mockito:mockito-android:2.24.5"

    val coreTesting = "android.arch.core:core-testing:1.1.1"
}

object Libs {
    private val roomVersion = "1.1.1"
    val room = "android.arch.persistence.room:rxjava2:$roomVersion"
    val roomCompiler = "android.arch.persistence.room:compiler:$roomVersion"

    val ahbottomnavigation = "com.aurelhubert:ahbottomnavigation:2.1.0"

    val timber = "com.jakewharton.timber:timber:4.7.0"

    val threetenabp = "com.jakewharton.threetenabp:threetenabp:1.1.0"

    private const val retrofitVersion = "2.4.0"
    val retrofit = "com.squareup.retrofit2:retrofit:$retrofitVersion"
    val retrofitMoshiConverter = "com.squareup.retrofit2:converter-moshi:$retrofitVersion"
    val okHttpLogging = "com.github.ihsanbal:LoggingInterceptor:3.0.0"

    val moshi = "com.squareup.moshi:moshi-kotlin:1.6.0"

    val glide = "com.github.bumptech.glide:glide:4.8.0"

    private const val conductorVersion = "2.1.5"
    val conductor = "com.bluelinelabs:conductor:$conductorVersion"
    val conductorSupport = "com.bluelinelabs:conductor-support:$conductorVersion"
    val conductorViewModel = "com.github.miquelbeltran:conductor-viewmodel:1.0.2"

    val dexter = "com.karumi:dexter:4.2.0"

    private const val parcelerVersion = "1.1.11"
    val parcel = "org.parceler:parceler-api:$parcelerVersion"
    val parcelCompiler = "org.parceler:parceler:$parcelerVersion"

    val calligraphy = "uk.co.chrisjenx:calligraphy:2.3.0"

    val rxjava = "io.reactivex.rxjava2:rxjava:2.2.2"
    val rxandroid = "io.reactivex.rxjava2:rxandroid:2.1.0"
    val rxretrofitAdapter = "com.jakewharton.retrofit:retrofit2-rxjava2-adapter:1.0.0"

    private const val rxbindingVersion = "2.2.0"
    val rxbinding = "com.jakewharton.rxbinding2:rxbinding-kotlin:$rxbindingVersion"
    val rxbindingAppcompat = "com.jakewharton.rxbinding2:rxbinding-appcompat-v7-kotlin:$rxbindingVersion"
    val rxbindingSupport = "com.jakewharton.rxbinding2:rxbinding-support-v4-kotlin:$rxbindingVersion"

    val crashlytics = "com.crashlytics.sdk.android:crashlytics:2.9.3@aar"

    private const val leakCanaryVersion = "1.5.4"
    val leakCanaryDebug = "com.squareup.leakcanary:leakcanary-android:$leakCanaryVersion"
    val leakCanaryRelease = "com.squareup.leakcanary:leakcanary-android-no-op:$leakCanaryVersion"
}