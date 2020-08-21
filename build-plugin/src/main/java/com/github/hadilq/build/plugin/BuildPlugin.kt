/**
 * Copyright 2020 Hadi Lashkari Ghouchani

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.hadilq.build.plugin

import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.creating
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.signing.SigningPlugin
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoReport
import java.io.File

class BuildPlugin : Plugin<Project> {

  override fun apply(target: Project) = target.setup()
}

private const val VERSION_LIFECYCLE = "2.2.0-rc02"
private const val VERSION_ANDROIDX_APPCOMPAT = "1.2.0"
private const val VERSION_ANDROID_LIFECYCLE = "0.4.2"
private const val VERSION_COROUTINES = "1.3.3"

private const val VERSION_JUNIT = "4.12"
private const val VERSION_MOCKITO = "1.4.0"
private const val VERSION_ROBOLECTRIC = "4.3"

const val VERSION_JACOCO = "0.8.5"

const val GROUP_ID = "com.github.hadilq"
const val LIB_VERSION = "0.4.0"

const val VERSION_COMPILE_SDK = 29
const val VERSION_MIN_SDK = 15
const val VERSION_TARGET_SDK = 29

const val KOTLIN_STDLIB = "stdlib"
const val KOTLIN_STDLIB_COMMON = "stdlib-common"
const val KOTLIN_TEST_COMMON = "test-common"
const val KOTLIN_TEST_ANNOTATIONS_COMMON = "test-annotations-common"
const val ANDROIDX_APPCOMPAT = "androidx.appcompat:appcompat:$VERSION_ANDROIDX_APPCOMPAT"

const val LIFECYCLE = "androidx.lifecycle:lifecycle-extensions:$VERSION_LIFECYCLE"
const val LIFECYCLE_COMPILER = "androidx.lifecycle:lifecycle-compiler:$VERSION_LIFECYCLE"
const val ANDROIDX_VIEW_MODEL = "androidx.lifecycle:lifecycle-viewmodel-ktx:$VERSION_LIFECYCLE"
const val ANDROID_LIFECYCLE = "com.github.hadilq:android-lifecycle-handler-metadata:$VERSION_ANDROID_LIFECYCLE"
const val ANDROID_LIFECYCLE_ANDROID = "com.github.hadilq:android-lifecycle-handler-android:$VERSION_ANDROID_LIFECYCLE"
const val ANDROID_LIFECYCLE_JVM = "com.github.hadilq:android-lifecycle-handler-jvm:$VERSION_ANDROID_LIFECYCLE"
const val COROUTINES = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$VERSION_COROUTINES"
const val COROUTINES_COMMON = "org.jetbrains.kotlinx:kotlinx-coroutines-core-common:$VERSION_COROUTINES"
const val COROUTINES_ANDROID = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$VERSION_COROUTINES"

const val JUNIT = "junit:junit:$VERSION_JUNIT"
const val MOCKITO = "com.nhaarman:mockito-kotlin:$VERSION_MOCKITO"
const val ROBOLECTRIC = "org.robolectric:robolectric:$VERSION_ROBOLECTRIC"
const val COROUTINES_TEST = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$VERSION_COROUTINES"
const val MOCKK_COMMON = "io.mockk:mockk-common:1.10.0"

private fun Project.setup() {
}

fun Project.setupJacoco() {
  plugins.apply(JacocoPlugin::class.java)

  extensions.getByType<JacocoPluginExtension>().run {
    toolVersion = VERSION_JACOCO

    tasks.register<JacocoReport>("jacocoTestReport") {
      val coverageSourceDirs = arrayOf(
        "src/commonMain/kotlin",
        "src/jvmMain/kotlin",
        "src/androidMain/kotlin"
      )

      val classFiles = File("${buildDir}/tmp/kotlin-classes/debug")
        .walkBottomUp()
        .toSet()

      classDirectories.setFrom(classFiles)
      sourceDirectories.setFrom(files(coverageSourceDirs))

      executionData
        .setFrom(files("${buildDir}/jacoco/testDebugUnitTest.exec"))

      reports {
        xml.isEnabled = true
        html.isEnabled = true
      }
    }
  }
}

fun Project.setupPublication() {
  plugins.apply("org.jetbrains.dokka")

  if (!hasProperty("signing.keyId")) {
    return
  }
  plugins.apply("maven-publish")
  plugins.apply(SigningPlugin::class.java)

  group = GROUP_ID
  version = LIB_VERSION

  val userId = "hadilq"
  val userName = "Hadi Lashkari Ghouchani"
  val userEmail = "hadilq.dev@gmail.com"
  val githubUrl = "https://github.com/hadilq/AndroidLifecycleHandler"
  val githubScmUrl = "scm:git@github.com:hadilq/AndroidLifecycleHandler.git"

  val javadocJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    archiveClassifier.value("javadoc")
    from(tasks.getByName("dokkaJavadoc"))
  }

  val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.value("sources")
  }

  publishing {
    publications {
      withType<MavenPublication>()["kotlinMultiplatform"].artifact(sourcesJar)
    }
  }

  extensions.getByType<PublishingExtension>().run {
    publications.withType<MavenPublication>().all {
      signing.sign(this)
      artifact(javadocJar)
      pom {
        withXml {
          asNode().apply {
            appendNode("name", "AndroidLifecycleHandler")
            appendNode(
              "description",
              "This library is a simplifier for unnecessary complex lifecycles of `androidx.lifecycle:lifecycle-extensions`."
            )
            appendNode("url", githubUrl)
          }
        }
        licenses {
          license {
            name.set("The Apache License, Version 2.0")
            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
          }
        }
        developers {
          developer {
            id.set(userId)
            name.set(userName)
            email.set(userEmail)
          }
        }
        scm {
          url.set(githubUrl)
          connection.set(githubScmUrl)
          developerConnection.set(githubScmUrl)
        }
      }

    }

    repositories {
      maven {
        url = if ("$version".endsWith("-SNAPSHOT"))
          uri("https://oss.sonatype.org/content/repositories/snapshots/")
        else
          uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
        credentials {
          username = findProperty("ossrhUsername")?.toString()
          password = findProperty("ossrhPassword")?.toString()
        }
      }
    }

  }
}

/**
 * Retrieves the [signing][org.gradle.plugins.signing.SigningExtension] extension.
 */
val org.gradle.api.Project.`signing`: org.gradle.plugins.signing.SigningExtension
  get() =
    (this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("signing") as org.gradle.plugins.signing.SigningExtension

/**
 * Configures the [signing][org.gradle.plugins.signing.SigningExtension] extension.
 */
fun org.gradle.api.Project.`signing`(configure: org.gradle.plugins.signing.SigningExtension.() -> Unit): Unit =
  (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("signing", configure)

/**
 * Provides the existing [archives][org.gradle.api.artifacts.Configuration] element.
 */
val org.gradle.api.NamedDomainObjectContainer<org.gradle.api.artifacts.Configuration>.`archives`: NamedDomainObjectProvider<Configuration>
  get() = named<org.gradle.api.artifacts.Configuration>("archives")

/**
 * Configures the [publishing][org.gradle.api.publish.PublishingExtension] extension.
 */
fun org.gradle.api.Project.`publishing`(configure: org.gradle.api.publish.PublishingExtension.() -> Unit): Unit =
  (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("publishing", configure)
