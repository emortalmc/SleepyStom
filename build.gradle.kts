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
    compileOnly("com.github.Minestom:Minestom:dab6ec6000") // Dead Code commit
    implementation("net.kyori:adventure-text-minimessage:4.2.0-SNAPSHOT")
    implementation("com.google.guava:guava:31.0.1-jre")



    testImplementation("com.github.Minestom:Minestom:dab6ec6000")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}