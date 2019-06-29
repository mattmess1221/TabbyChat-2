import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.util.Date

plugins {
    `java-library`
    id("net.minecraftforge.gradle") version "3.0.128"
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