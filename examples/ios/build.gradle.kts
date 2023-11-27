plugins {
	kotlin("multiplatform")
	kotlin("native.cocoapods")
	alias(libs.plugins.kotlin.compose)
}

kotlin {
	iosX64()
	iosArm64()
	iosSimulatorArm64()

	sourceSets {
		val iosX64Main by getting
		val iosArm64Main by getting
		val iosSimulatorArm64Main by getting
		val iosMain by creating {
			iosX64Main.dependsOn(this)
			iosArm64Main.dependsOn(this)
			iosSimulatorArm64Main.dependsOn(this)

			dependencies {
				implementation(compose.ui)
				implementation(compose.foundation)
				implementation(compose.material)
				implementation(compose.runtime)

				implementation(project(":mpfilepicker"))
			}
		}
	}
}


kotlin.cocoapods {
	name = "ios"
	version = libs.versions.library.get()
	summary =
		"A multiplatform compose widget for picking files with each platform''s Native File Picker Dialog."
	homepage = "https://github.com/Wavesonics/compose-multiplatform-file-picker"
	ios.deploymentTarget = "14.1"

	framework {
		baseName = "ios"
	}

	podfile = project.file("../iosApp/Podfile")
}

repositories {
	mavenCentral()
	maven { url = uri("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven") }
	maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
	google()
}
