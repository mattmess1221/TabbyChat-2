import java.util.Date
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.50"
    id("net.minecraftforge.gradle") version "3.0.142"
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

val ConfigurationContainer.include: Configuration by configurations.creating
val ConfigurationContainer.mod: Configuration by configurations.creating
fun DependencyHandlerScope.include(dep: Any) = add("include", dep)
fun DependencyHandlerScope.mod(dep: Any) = add("mod", dep)

repositories {
    jcenter()
    maven("https://minecraft.curseforge.com/api/maven/") {
        name = "curseforge"
    }
}

dependencies {
    minecraft("net.minecraftforge:forge:$forge_version")

    include(implementation("net.sf.jazzy:jazzy:0.5.2-rtext-1.4.1-2")!!)

    testImplementation("junit:junit:4.12")
    compileOnly(kotlin("stdlib-jdk8"))
    mod(compileOnly("kottle:Kottle:1.1.1")!!)
}
minecraft {
    mappings(mappings_channel, mappings_version)
    accessTransformer(file("src/main/resources/META-INF/accesstransformer.cfg"))
    runs {
        val client by creating {

            fun properties(vararg props: Pair<String, String>) = properties(mapOf(*props))

            workingDirectory = file("run").absolutePath

            property("forge.logging.markers", "SCAN")
            property("forge.logging.console.level", "info")

            mods {
                val tabbychat by creating {
                    source(sourceSets.main.get())
                }
            }
            ideaModule = "${project.name}.main"
        }
    }
}


tasks {

    shadowJar {
        archiveClassifier.set("release")
        configurations = listOf(project.configurations.include)
        relocate("com.swabunga", "mnm.mods.tabbychat.redist.com.swabunga")
    }

    withType<Jar> {
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

    val uninstallMods by creating(Delete::class) {
        delete(fileTree("run/mods"))
    }

    val installMods by creating(Copy::class) {
        dependsOn(uninstallMods)
        from(configurations.mod)
        include("**/*.jar")
        into(file("run/mods"))
    }

    afterEvaluate {
        named("prepareRuns") {
            dependsOn(installMods)
        }
    }
}
reobf {
    val shadowJar by creating {
        dependsOn(tasks.createMcpToSrg)
        mappings = tasks.createMcpToSrg.get().outputs.files.singleFile
    }
}

artifacts {
    archives(tasks.shadowJar)
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}