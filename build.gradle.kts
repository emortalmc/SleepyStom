plugins {
    `java-library`
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "dev.emortal"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://oss.sonatype.org/content/repositories/snapshots/") // minimessage? todo plz fix
}

dependencies {
    compileOnly("com.github.Minestom:Minestom:4a976a3333")
    implementation("net.kyori:adventure-text-minimessage:4.2.0-SNAPSHOT")

    testImplementation("com.github.Minestom:Minestom:4a976a3333")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}