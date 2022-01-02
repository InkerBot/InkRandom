plugins {
    kotlin("jvm") version "1.5.10"
    id ("com.github.johnrengelman.shadow").version("7.0.0")
}

group = "bot.inker.example"
version = "1.0-SNAPSHOT"
description = "Hello, InkerBot."
val mainClass = "bot.inker.example.ExamplePlugin"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    compileOnly("bot.inker:api:1.0-SNAPSHOT")
}

tasks.processResources {
    filesMatching("META-INF/plugin.json") {
        expand(mapOf(
            "pluginName" to project.name,
            "pluginVersion" to project.version,
            "pluginDescribe" to project.description,
            "pluginMainClass" to mainClass,
        ))
    }
}

tasks.shadowJar {
    archiveVersion.set("")
    archiveClassifier.set("app")

    dependencies{
        exclude("org.jetbrains.kotlin")
        exclude {
            (excludes.contains(it.moduleGroup) || excludes.contains(it.moduleGroup+":")
                    || excludes.contains(":"+it.moduleName) || excludes.contains(it.moduleGroup+":"+it.moduleName))
        }
    }
}

tasks.assemble{
    dependsOn(tasks.shadowJar)
}