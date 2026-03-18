@file:Suppress("UnstableApiUsage")

pluginManagement {
  repositories {
    google {
      content {
        includeGroupByRegex("com\\.android.*")
        includeGroupByRegex("com\\.google.*")
        includeGroupByRegex("androidx.*")
      }
    }
    mavenCentral()
    gradlePluginPortal()
  }
}

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
  }

  versionCatalogs {
    create("sharedLibs") {
      from(files("submodules/Shared/gradle/libs.versions.toml"))
    }
  }
}

rootProject.name = "Vigil"
include(":app")

include(":Shared")
project(":Shared").projectDir = file("submodules/Shared/app")
