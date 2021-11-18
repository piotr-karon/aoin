import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.0"
    application
}

group = "aoin.wit.kar"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val jacksonVersion = "2.13.0"

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv:$jacksonVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-cli-jvm:0.3.3")

    testImplementation("org.jetbrains.kotlin:kotlin-test:1.6.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClass.set("MainKt")
}
