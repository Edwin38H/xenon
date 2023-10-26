plugins {
    id("java-library")
    id("pl.allegro.tech.build.axion-release") version "1.15.5"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "dev.portero.xenon"
version = scmVersion.version

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
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.2-R0.1-SNAPSHOT")
}

scmVersion {
    tag {
        versionIncrementer("incrementPatch")
    }
}

tasks.shadowJar {
    archiveBaseName.set(project.name)
    archiveVersion.set(scmVersion.version)
    archiveClassifier.set("ALPHA")
}