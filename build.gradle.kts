// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    extra.apply {
        set("hilt_version", "2.45")
        set("compose_version", "1.3.3")
        set("nav_version", "2.5.3")
        set("room_version", "2.5.0")
        set("lifecycle_version", "2.6.0")
    }

    dependencies {
        classpath("com.google.gms:google-services:4.3.15")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.4")
    }
}

plugins {
    id("com.android.application") version "7.4.2" apply false
    id("com.android.library") version "7.4.2" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("com.google.dagger.hilt.android") version "2.44" apply false
    id("com.google.devtools.ksp") version "1.8.10-1.0.9"
}