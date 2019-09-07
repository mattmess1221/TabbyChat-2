import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.util.Date
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.50"
    `java-library`
    id("net.minecraftforge.gradle") version "3.0.138"
    id("com.github.johnrengelman.shadow") version "4.0.4"
}

val release: String by project
val forge_version: String by project
val mappings_channel: String by project
val mappings_version: String by project

if (release != "RELEASE")
    version = "$version-$release"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

val Project.sourceSets: SourceSetContainer by project
val SourceSetContainer.main by sourceSets.getting

configurations {
    create("include")
}

repositories {
    jcenter()
}

dependencies {
    minecraft("net.minecraftforge:forge:$forge_version")

    implementation("net.sf.jazzy:jazzy:0.5.2-rtext-1.4.1-2")
    include("net.sf.jazzy:jazzy:0.5.2-rtext-1.4.1-2")

    testImplementation("junit:junit:4.12")
    implementation(kotlin("stdlib-jdk8"))
}
minecraft {
    mappings(mappings_channel, mappings_version)
    accessTransformer(file("src/main/resources/META-INF/accesstransformer.cfg"))
    runs {
        create("client") {
            workingDirectory = file("run").absolutePath

            properties(mapOf(
                    "forge.logging.markers" to "SCAN",
                    "forge.logging.console.level" to "info"))

            mods {
                create("tabbychat") {
                    source(sourceSets.main)
                }
            }
            ideaModule = "${project.name}.main"
        }
    }
}

tasks {

    getByName<Jar>("jar") {
        classifier = "base"
    }
    getByName<ShadowJar>("shadowJar") {
        classifier = ""
        configurations = listOf(project.configurations.include)
        relocate("com.swabunga", "mnm.mods.tabbychat.redist.com.swabunga")
    }
    withType<Jar> {
        manifest.attributes(mapOf(
                "Specification-Title" to project.name,
                "Specification-Vendor" to "killjoy1221",
                "Specification-Version" to project.version,
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version,
                "Implementation-Vendor" to "killjoy1221",
                "Implementation-Timestamp" to Date()
        ))
    }
}
val TaskContainer.shadowJar by tasks.getting
val TaskContainer.createMcpToSrg by tasks.getting

reobf {
    create("shadowJar") {
        dependsOn(tasks.createMcpToSrg)
        mappings = tasks.createMcpToSrg.outputs.files.singleFile
    }
}

artifacts {
    add("archives", tasks.shadowJar)
}
defaultArtifacts {

}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}