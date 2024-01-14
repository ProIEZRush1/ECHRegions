plugins {
    kotlin("jvm") version "1.9.21"
    id("com.github.johnrengelman.shadow") version("8.1.1")
    java
}

group = "com.proiezrush.echregions"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.xenondevs.xyz/releases")
}

dependencies {
    implementation(kotlin("stdlib"))
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    implementation("xyz.xenondevs.invui:invui:1.24")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.22")
    implementation("mysql:mysql-connector-java:8.0.23")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {
    shadowJar {
        // Your existing configuration
        relocate("kotlin", "com.proiezrush.echregions.kotlin")

        // Setting the destination directory
        destinationDirectory.set(file("/Users/ech/Documents/Test plugins server/plugins"))
    }
}