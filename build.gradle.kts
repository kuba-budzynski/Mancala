import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.31"
    application
}

group = "com"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.6")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0-RC")

    implementation("org.apache.commons","commons-lang3", "3.0")
    implementation("org.apache.commons","commons-collections4", "4.4")
    implementation("org.apache.commons","commons-text", "1.9")
    implementation("org.apache.commons","commons-math3", "3.6.1")
    implementation("org.apache.poi","poi", "3.9")
    implementation("org.apache.poi", "poi-ooxml", "3.9")

    compile("org.slf4j:slf4j-api:1.7.25")
    implementation("org.slf4j:slf4j-api:1.7.25")
    compile("org.slf4j","slf4j-log4j12", "1.7.25")

    implementation("io.insert-koin:koin-core:3.0.1")
    testImplementation("io.insert-koin:koin-test-junit5:3.0.1")
    implementation("io.insert-koin:koin-core-ext:3.0.1")
    testImplementation("io.insert-koin:koin-test-junit4:3.0.1")
    testImplementation("io.insert-koin:koin-test-junit5:3.0.1")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "13"
}

application {
    mainClassName = "MainKt"
}