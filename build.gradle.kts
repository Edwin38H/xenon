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
        prefix.set("v")
        versionSeparator.set("-")
        versionIncrementer("incrementPatch")
    }
    nextVersion {
        suffix = "ALPHA"
    }
}

tasks.shadowJar {
    dependsOn("updatePluginYml")

    archiveBaseName.set(project.name)
    archiveVersion.set(scmVersion.version)
    archiveClassifier.set("")
}

tasks.register<Copy>("updatePluginYml") {
    from(sourceSets.main.get().resources.srcDirs) {
        include("**/plugin.yml")
        expand("version" to scmVersion.version)
    }
    into(File(buildDir, "resources/main"))
}