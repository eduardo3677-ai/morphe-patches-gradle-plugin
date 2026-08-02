import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin)
    `java-gradle-plugin`
    `maven-publish`
    signing
}

group = "io.github.eduardo3677-ai"

repositories {
    mavenLocal()
    mavenCentral()
    google()
}

dependencies {
    implementation(libs.android.application)
    implementation(libs.guava)
    implementation(libs.kotlin)

    implementation(gradleApi())
    implementation(gradleKotlinDsl())
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11

    withSourcesJar()
    withJavadocJar()
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

gradlePlugin {
    website = "https://github.com/eduardo3677-ai/morphe-patches-gradle-plugin"
    vcsUrl = "ssh://git@github.com:eduardo3677-ai/morphe-patches-gradle-plugin.git"

    plugins {
        create("patchesSettingsPlugin") {
            id = "app.morphe.patches"
            implementationClass = "app.morphe.patches.gradle.SettingsPlugin"
            version = version
            description = "Plugin to configure a Morphe Patches project."
            displayName = "Morphe Patches Gradle settings plugin"
        }
    }
}

publishing {
    repositories {
        maven {
            name = "MavenCentral"
            url = uri(
                if (project.version.toString().endsWith("-SNAPSHOT")) {
                    "https://central.sonatype.com/repository/maven-snapshots/"
                } else {
                    "https://central.sonatype.com/api/v1/publisher/deploy/"
                }
            )
            credentials {
                username = System.getenv("MAVEN_CENTRAL_USERNAME") ?: ""
                password = System.getenv("MAVEN_CENTRAL_PASSWORD") ?: ""
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications)
}
