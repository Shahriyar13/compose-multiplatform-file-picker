import java.net.URI

plugins {
	kotlin("multiplatform")
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.compose)
	id("maven-publish")
	id("signing")
}

val readableName = "Multiplatform File Picker + Multiple File Picker"
val repoUrl = "https://github.com/Shahriyar13/compose-multiplatform-file-picker"
group = "app.duss"
description = "A multiplatform compose widget for picking files"
version = libs.versions.library.get()

extra.apply {
	set("isReleaseVersion", !(version as String).endsWith("SNAPSHOT"))
}

kotlin {
	explicitApi()

	androidTarget {
		publishLibraryVariants("release")
	}
	jvm {
		jvmToolchain(11)
		compilations.all {
			kotlinOptions.jvmTarget = "11"
		}
	}
	js(IR) {
		browser()
		binaries.executable()
	}

	macosX64()
	macosArm64()

	listOf(
		iosX64(),
		iosArm64(),
		iosSimulatorArm64(),
	).forEach {
		it.binaries.framework {
			baseName = "MPFilePicker"
		}
	}

	sourceSets {
		val commonMain by getting {
			dependencies {
				api(compose.runtime)
				api(compose.foundation)
			}
		}
		val commonTest by getting {
			dependencies {
				implementation(kotlin("test"))
			}
		}
		val androidMain by getting {
			dependencies {
				api(compose.uiTooling)
				api(compose.preview)
				api(compose.material)
				api(libs.androidx.appcompat)
				api(libs.androidx.coreKtx)
				api(libs.compose.activity)
				api(libs.kotlinx.coroutines.android)
			}
		}
		val jvmMain by getting {
			dependencies {
				api(compose.uiTooling)
				api(compose.preview)
				api(compose.material)

				listOf("lwjgl", "lwjgl-tinyfd").forEach { lwjglDep ->
					implementation("org.lwjgl:${lwjglDep}:${libs.versions.lwjgl.get()}")
					listOf(
						"natives-windows",
						"natives-windows-x86",
						"natives-windows-arm64",
						"natives-macos",
						"natives-macos-arm64",
						"natives-linux",
						"natives-linux-arm64",
						"natives-linux-arm32"
					).forEach { native ->
						runtimeOnly("org.lwjgl:${lwjglDep}:${libs.versions.lwjgl.get()}:${native}")
					}
				}
			}
		}
		val jvmTest by getting
		val jsMain by getting

		val macosX64Main by getting
		val macosArm64Main by getting

		val macosMain by creating {
			dependsOn(commonMain)
			macosX64Main.dependsOn(this)
			macosArm64Main.dependsOn(this)
		}

		val iosX64Main by getting
		val iosArm64Main by getting
		val iosSimulatorArm64Main by getting

		val iosMain by creating {
			dependsOn(commonMain)
			iosX64Main.dependsOn(this)
			iosArm64Main.dependsOn(this)
			iosSimulatorArm64Main.dependsOn(this)
		}
	}

	val javadocJar by tasks.registering(Jar::class) {
		archiveClassifier.set("javadoc")
	}

	publishing {
		repositories {
			maven {
				val releaseRepo = URI("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
				val snapshotRepo = URI("https://s01.oss.sonatype.org/content/repositories/snapshots/")
				url = if (extra["isReleaseVersion"] == true) releaseRepo else snapshotRepo
				credentials {
					username = properties["ossrh.username"]?.toString() ?: "Unknown user"
					password = properties["ossrh.password"]?.toString() ?: "Unknown password"
				}
			}
		}
	}
}

tasks.withType<AbstractPublishToMaven>().configureEach {
	val signingTasks = tasks.withType<Sign>()
	mustRunAfter(signingTasks)
}

android {
	namespace = "com.darkrockstudios.libraries.mpfilepicker"
	compileSdk = libs.versions.android.compile.sdk.get().toInt()
	sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
	defaultConfig {
		minSdk = libs.versions.android.min.sdk.get().toInt()
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}
}