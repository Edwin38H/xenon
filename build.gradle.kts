import org.gradle.configurationcache.extensions.capitalized
import java.io.ByteArrayOutputStream

plugins {
    id("java-library")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "dev.portero.xenon"
version = "ALPHA"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

repositories {
    mavenLocal()
    mavenCentral()

    // Spigot API && Documentation
    maven{
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        name = "SpigotMC"
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.2-R0.1-SNAPSHOT")

    compileOnly("org.jetbrains:annotations:24.0.0")

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}

val determinePatchVersion: () -> Int = {
    val tagInfo = ByteArrayOutputStream()
    exec {
        commandLine("git", "describe", "--tags")
        standardOutput = tagInfo
    }
    val output = tagInfo.toString()
    val regex = """v(\d+)\.(\d+)\.(\d+)(?:-(\d+)-g[a-f0-9]+)?""".toRegex()
    val matchResult = regex.find(output)
    matchResult?.groups?.get(4)?.value?.toInt() ?: 0
}

val major = 0
val minor = 0
val patch = determinePatchVersion()
val fullVersion = "$major.$minor.$patch-$version"

tasks.register<Copy>("updatePluginYml") {
    from(sourceSets.main.get().resources.srcDirs) {
        include("**/plugin.yml")
        expand("version" to fullVersion)
    }
    into(File(buildDir, "resources/main"))
}

tasks.shadowJar {
    dependsOn("updatePluginYml")

    // Development environment configuration
    destinationDirectory.set(File(buildDir, "../server/plugins"))

    archiveBaseName.set(project.name.capitalized())
    archiveVersion.set(fullVersion)
    archiveClassifier.set("")
}
