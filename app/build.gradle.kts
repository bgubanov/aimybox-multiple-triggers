val componentsVersion: String by rootProject.extra
val aimyboxVersion: String by rootProject.extra

plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android-extensions")
}

android {

    compileSdkVersion(30)

    defaultConfig {
        applicationId = "com.justai.aimybox.triggers.kaldi"

        minSdkVersion(21)
        targetSdkVersion(30)

        versionName = componentsVersion
        versionCode = 1
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
        getByName("release") {
            //TODO configure pro guard
        }
    }

    packagingOptions {
        pickFirst("lib/x86/libpocketsphinx_jni.so")
        pickFirst("lib/x86_64/libpocketsphinx_jni.so")
        pickFirst("lib/armeabi-v7a/libpocketsphinx_jni.so")
        pickFirst("lib/arm64-v8a/libpocketsphinx_jni.so")
    }

    lintOptions {
        isCheckAllWarnings = true
        isWarningsAsErrors = false
        isAbortOnError = true
    }
}

repositories {
//    mavenLocal()
    google()
    mavenCentral()
    maven("https://kotlin.bintray.com/kotlinx")
    maven("https://alphacephei.com/maven/")
    jcenter()
}

dependencies {

//    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.0-beta-3")

    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.recyclerview:recyclerview:1.0.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("androidx.core:core-ktx:1.1.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.1.0")
    implementation("com.google.android.material:material:1.0.0")

    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.1")
    implementation(project(":components"))
    implementation("com.just-ai.aimybox:core:$aimyboxVersion")
    implementation("com.just-ai.aimybox:google-platform-speechkit:$aimyboxVersion")
    implementation("com.just-ai.aimybox:yandex-speechkit:$aimyboxVersion")
    implementation("com.just-ai.aimybox:kaldi-speechkit:$aimyboxVersion")

    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.9.0"))
    // define any required OkHttp artifacts without version
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")
}
