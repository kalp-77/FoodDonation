// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    dependencies {

        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.7")
        classpath ("com.google.gms:google-services:4.4.1")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.48")
        classpath ("android.arch.navigation:navigation-safe-args-gradle-plugin:1.0.0")

    }
}

plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    id ("com.android.library") version "7.2.1" apply false
    id ("androidx.navigation.safeargs") version "2.4.2" apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false

}
