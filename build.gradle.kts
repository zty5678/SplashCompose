plugins {
    alias(libs.plugins.gradle.versions)
    alias(libs.plugins.version.catalog.update)

    alias(libs.plugins.kapt) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.android.application) apply false

}

apply("${project.rootDir}/buildscripts/toml-updater-config.gradle")
