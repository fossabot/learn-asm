/*
 * Copyright (C) 2020 PatrickKR
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * Contact me on <mailpatrickkr@gmail.com>
 */

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    jacoco
    `maven-publish`
    kotlin("jvm") version "1.4.10"
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "com.github.patrick-mc"
version = requireNotNull(properties["version"])

val kebabRegex = "-[a-z]".toRegex()
val relocations = setOf(
        "kotlin",
        "org.intellij.lang.annotations",
        "org.jetbrains.annotations",
        "org.objectweb.asm"
)

repositories {
    maven("https://repo.maven.apache.org/maven2/")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.ow2.asm:asm:9.0")

    testImplementation("org.jacoco:org.jacoco.agent:0.8.6")

    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.0")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    withType<Test> {
        useJUnitPlatform()

        finalizedBy(jacocoTestReport)
    }

    withType<JacocoReport> {
        dependsOn(test)
        dependsOn(jacocoTestCoverageVerification)

        reports {
            csv.isEnabled = false
            html.isEnabled = true
            xml.isEnabled = true
            html.destination = file("${buildDir}/reports/jacoco")
            xml.destination = file("${buildDir}/reports/jacoco/jacoco.xml")
        }
    }

    withType<JacocoCoverageVerification> {
        violationRules {
            rule {
                limit {
                    minimum = "0.8".toBigDecimal()
                }
            }
        }
    }

    withType<AbstractPublishToMaven> {
        dependsOn(jacocoTestReport)
    }

    withType<ShadowJar> {
        archiveClassifier.set("")

        val projectName = kebabRegex.replace(rootProject.name) { result ->
            result.value.drop(1)
        }

        relocations.forEach { pattern ->
            relocate(pattern, "com.github.patrick.$projectName.shaded.$pattern")
        }
    }

    create<Jar>("sourcesJar") {
        archiveClassifier.set("sources")
        from(sourceSets["main"].allSource)
    }
}

jacoco {
    toolVersion = "0.8.6"
}

publishing {
    publications {
        val projectName = kebabRegex.replace(rootProject.name) { result ->
            result.value.drop(1).capitalize()
        }

        create<MavenPublication>(projectName) {
            from(components["java"])
            artifact(tasks["sourcesJar"])

            repositories {
                mavenLocal()
            }

            pom {
                name.set(rootProject.name)
                description.set("Learning ASM with Kotlin")
                url.set("https://github.com/patrick-mc/${rootProject.name}")

                licenses {
                    license {
                        name.set("GNU General Public License v3.0")
                        url.set("https://www.gnu.org/licenses/gpl-3.0.html")
                    }
                }

                developers {
                    developer {
                        id.set("patrick-mc")
                        name.set("PatrickKR")
                        email.set("mailpatrickkorea@gmail.com")
                        url.set("https://github.com/patrick-mc")
                        roles.addAll("developer")
                        timezone.set("Asia/Seoul")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/patrick-mc/${rootProject.name}.git")
                    developerConnection.set("scm:git:ssh://github.com:patrick-mc/${rootProject.name}.git")
                    url.set("https://github.com/patrick-mc/${rootProject.name}")
                }
            }
        }
    }
}