plugins {
    java
    kotlin("jvm") version "1.4.31"
    kotlin("plugin.serialization") version "1.5.0"
    id("application")
    id("org.openjfx.javafxplugin") version "0.0.8"
}

group = "com"
version = "1.0"

repositories {
    mavenCentral()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>{
    kotlinOptions.jvmTarget = "1.8"
}

dependencies {
    implementation(kotlin("stdlib"))
    testCompile("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    implementation("io.github.microutils:kotlin-logging-jvm:2.0.6")

    implementation("com.google.guava:guava:30.1-jre")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0-RC")

    implementation("org.apache.commons","commons-lang3", "3.0")
    implementation("org.apache.commons","commons-collections4", "4.4")
    implementation("org.apache.commons","commons-text", "1.9")
    implementation("org.apache.commons","commons-math3", "3.6.1")
    implementation("org.apache.poi","poi", "3.9")
    implementation("no.tornado","tornadofx", "1.7.15")

    compile("org.slf4j:slf4j-api:1.7.25")
    implementation("org.slf4j:slf4j-api:1.7.25")
//    api("org.slf4j","slf4j-api", "2.0.0-alpha1")
    compile("org.slf4j","slf4j-log4j12", "1.7.25")

    implementation("io.insert-koin:koin-core:3.0.1")
    testImplementation("io.insert-koin:koin-test-junit5:3.0.1")
    implementation("io.insert-koin:koin-core-ext:3.0.1")
    testImplementation("io.insert-koin:koin-test-junit4:3.0.1")
    testImplementation("io.insert-koin:koin-test-junit5:3.0.1")
}

javafx {
    version = "11.0.2"
    modules("javafx.controls", "javafx.graphics")
}
