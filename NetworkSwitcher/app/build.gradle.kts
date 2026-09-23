plugins { id("com.android.application") }

android {
    namespace = "com.gagan.networkswitcher"
    compileSdk = 36
    defaultConfig {
        applicationId = "com.gagan.networkswitcher"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation("dev.rikka.shizuku:api:13.1.5")
    implementation("dev.rikka.shizuku:provider:13.1.5")
    implementation("org.lsposed.hiddenapibypass:hiddenapibypass:4.3")
    implementation("androidx.annotation:annotation:1.9.1")
}
