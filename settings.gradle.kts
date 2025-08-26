pluginManagement {
    repositories {
        gradlePluginPortal() // must be first so Gradle can find KSP
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MyLearningProject"
include(":app")
