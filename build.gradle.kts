import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.30"
    application
    id("org.jlleitschuh.gradle.ktlint") version "9.4.1"
}

val versionDetails: groovy.lang.Closure<org.gradle.tooling.internal.consumer.versioning.VersionDetails> by extra
val prismVersion = "1.2.0"

group = "io.iohk.atala.prism.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
    google()
    maven("https://plugins.gradle.org/m2/")
    maven {
        url = uri("https://maven.pkg.github.com/input-output-hk/better-parse")
        credentials {
            username = "atala-dev"
            password = "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"
        }
    }
}
configurations { create("internalLibs") }

dependencies {


    testImplementation(kotlin("test"))
    "internalLibs"(files("src/lib/prism-api-jvm-1.2.0.jar"))
    "internalLibs"(files("src/lib/prism-common-jvm-1.2.0.jar"))
    "internalLibs"(files("src/lib/prism-credentials-jvm-1.2.0.jar"))
    "internalLibs"(files("src/lib/prism-crypto-jvm-1.2.0.jar"))
    "internalLibs"(files("src/lib/prism-identity-jvm-1.2.0.jar"))

    //implementation("io.iohk.atala:prism-api:$prismVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.2.1")

}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

kotlin.sourceSets.all {
    languageSettings.optIn("io.iohk.atala.prism.common.PrismSdkInternal")
}

application {
    mainClassName = "io.iohk.atala.prism.example.MainKt"
}

apply(plugin = "org.jlleitschuh.gradle.ktlint")

ktlint {
    verbose.set(true)
    outputToConsole.set(true)

    // Exclude generated proto classes
    filter {
        exclude { element ->
            element.file.path.contains("generated/") or
                element.file.path.contains("externals/")
        }
    }
}
