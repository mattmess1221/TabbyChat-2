import java.util.*

plugins {
    kotlin("jvm") version "1.3.61"
    id("net.minecraftforge.gradle") version "3.0.157"
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

group = "mnm.mods"
version = "2.4.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

val include: Configuration by configurations.creating
val mod: Configuration by configurations.creating

repositories {
    jcenter()
    maven("https://minecraft.curseforge.com/api/maven/") {
        name = "curseforge"
    }
}

dependencies {
    minecraft("net.minecraftforge:forge:1.15.1-30.0.16")

    include(implementation("net.sf.jazzy:jazzy:0.5.2-rtext-1.4.1-2")!!)

    testImplementation("junit:junit:4.12")
    implementation(kotlin("stdlib-jdk8"))
    mod(implementation("kottle:Kottle:1.2.1")!!)
}
minecraft {
    mappingChannel = "snapshot"
    mappingVersion = "20191225-1.14.3"
    accessTransformer(file("src/main/resources/META-INF/accesstransformer.cfg"))
    runs {
        create("client") {

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

tasks.shadowJar {
    archiveClassifier.set("release")
    configurations = listOf(include)
    relocate("com.swabunga", "mnm.mods.tabbychat.redist.com.swabunga")
}

tasks.withType<Jar> {
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

task<Delete>("uninstallMods") {
    delete(fileTree("run/mods"))
}

task<Copy>("installMods") {
    dependsOn("uninstallMods")
    from(mod)
    include("**/*.jar")
    into(file("run/mods"))
}

afterEvaluate {
    tasks {
        "prepareRuns" {
            dependsOn("installMods")
        }
    }
}

reobf {
    create("shadowJar")
}

artifacts {
    archives(tasks.shadowJar)
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}