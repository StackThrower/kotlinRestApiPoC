pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
    plugins {
        kotlin("jvm") version "2.0.20"
        id("org.jetbrains.kotlin.plugin.spring") version "2.0.20"
        id("org.springframework.boot") version "3.3.3"
        id("io.spring.dependency-management") version "1.1.6"
    }
}

rootProject.name = "kotlinRestApiPoC"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include("domain")
include("application")
include("web")
