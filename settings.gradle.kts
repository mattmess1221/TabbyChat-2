pluginManagement {
    repositories {
        gradlePluginPortal()
        jcenter()
        maven("http://files.minecraftforge.net/maven") {
            name = "forge"
        }
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "net.minecraftforge") {
                useModule("net.minecraftforge.gradle:ForgeGradle:${requested.version}")
            }
        }
    }
}
rootProject.name = "TabbyChat"
