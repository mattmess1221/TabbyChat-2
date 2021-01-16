import java.util.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.61"
    id("net.minecraftforge.gradle")
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

group = "mnm.mods"
version = "2.4.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

val include: Configuration by configurations.creating

repositories {
    jcenter()
    maven("https://thedarkcolour.github.io/KotlinForForge/") {
        name = "kotlinforforge"
    }
}

dependencies {
    minecraft("net.minecraftforge:forge:1.15.2-31.2.0")

    include(implementation("net.sf.jazzy:jazzy:0.5.2-rtext-1.4.1-2")!!)

    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation("thedarkcolour:kotlinforforge:1.6.1")

    testImplementation("junit:junit:4.12")
}
minecraft {
    mappingChannel = "snapshot"
    mappingVersion = "20200925-1.15.1"
    accessTransformer(file("src/main/resources/META-INF/accesstransformer.cfg"))
    runs {
        listOf("client", "server").forEach {
            create(it) {

                workingDirectory = file("run").absolutePath

                property("forge.logging.markers", "SCAN")
                property("forge.logging.console.level", "info")

                mods {
                    create("tabbychat") {
                        source(sourceSets.main.get())
                    }
                }
                ideaModule = "${project.name}.main"
            }
        }
    }
}

tasks.shadowJar {
    archiveClassifier.set("")
    configurations = listOf(include)
    relocate("com.swabunga", "mnm.mods.tabbychat.redist.com.swabunga")
}

tasks.jar {
    manifest.attributes(
            "Specification-Title" to project.name,
            "Specification-Vendor" to "killjoy1221",
            "Specification-Version" to project.version,
            "Implementation-Title" to project.name,
            "Implementation-Version" to project.version,
            "Implementation-Vendor" to "killjoy1221",
            "Implementation-Timestamp" to Date()
    )
}

reobf {
    create("shadowJar")
}

artifacts {
    archives(tasks.shadowJar)
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
    freeCompilerArgs = listOf("-Xinline-classes")
}