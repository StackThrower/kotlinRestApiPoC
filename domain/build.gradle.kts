plugins {
    kotlin("jvm")
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:${property("junit.version")}")
    testImplementation("io.mockk:mockk:${property("mockk.version")}")
}

