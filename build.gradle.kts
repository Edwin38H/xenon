import org.gradle.configurationcache.extensions.capitalized
import java.io.ByteArrayOutputStream

plugins {
    id("java-library")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "dev.portero.xenon"
version = "DEV"

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

    maven {
        name = "PaperMC"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")

    compileOnly("org.jetbrains:annotations:24.0.0")

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    implementation("org.postgresql:postgresql:42.7.1")
    implementation("com.zaxxer:HikariCP:5.1.0")
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

    archiveBaseName.set(project.name.capitalized())

    if (version == "DEV") {
        val devVersion = "1.0.0-DEV"
        archiveVersion.set(devVersion)
    } else {
        archiveVersion.set(fullVersion)
    }

    archiveClassifier.set("")
}
