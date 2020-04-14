plugins {
    `kotlin-dsl`
    `java-library`
}

repositories {
    mavenCentral()
    jcenter()
    maven("https://files.minecraftforge.net/maven")
}

dependencies {
    implementation("net.minecraftforge.gradle:ForgeGradle:3.+")
}