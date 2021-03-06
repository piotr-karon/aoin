import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.0"
    application

    id("com.google.cloud.tools.jib") version "3.1.4"
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
// https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-core
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")

    implementation("org.slf4j:slf4j-api:1.7.33")
    implementation("com.michael-bull.kotlin-inline-logger:kotlin-inline-logger:1.0.4")

    implementation("org.jetbrains.kotlinx:kotlinx-cli-jvm:0.3.3")

    testImplementation("org.jetbrains.kotlin:kotlin-test:1.6.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClass.set("MainKt")
}

jib {
    to {
        image = "pkaron/aoin"
        this.tags = setOf("latest")
    }
    from {
        image = "eclipse-temurin:17.0.1_12-jdk-alpine"
    }
}
