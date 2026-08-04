import com.vanniktech.maven.publish.AndroidSingleVariantLibrary

plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    id("com.vanniktech.maven.publish") version "0.29.0" apply false
}

subprojects {
    apply(plugin = "com.vanniktech.maven.publish")

    afterEvaluate {
        extensions.findByType<com.vanniktech.maven.publish.MavenPublishBaseExtension>()?.apply {
            if (plugins.hasPlugin("com.android.library")) {
                configure(
                    AndroidSingleVariantLibrary(
                        variant = "release",
                        sourcesJar = true,
                        publishJavadocJar = false
                    )
                )
            }

            publishToMavenCentral(com.vanniktech.maven.publish.SonatypeHost.CENTRAL_PORTAL, automaticRelease = false)
            signAllPublications()

            coordinates(group.toString(), project.name, version.toString())

            pom {
                name.set(project.name)
                description.set("Authora — an opinionated authentication framework for Android")
                url.set("https://github.com/authora-org/authora")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }

                developers {
                    developer {
                        id.set("hastagaming")
                        name.set("Komandan Nasa")
                        url.set("https://github.com/hastagaming")
                    }
                }

                scm {
                    url.set("https://github.com/authora-org/authora")
                    connection.set("scm:git:git://github.com/authora-org/authora.git")
                    developerConnection.set("scm:git:ssh://git@github.com/authora-org/authora.git")
                }
            }
        }
    }
}