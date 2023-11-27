plugins {
	kotlin("multiplatform")
	alias(libs.plugins.kotlin.compose)
}

kotlin {
	macosX64 {
		binaries {
			executable {
				entryPoint = "main"
			}
		}
	}
	macosArm64 {
		binaries {
			executable {
				entryPoint = "main"
			}
		}
	}

	sourceSets {
		val macosX64Main by getting
		val macosArm64Main by getting

		val macosMain by creating {
			macosX64Main.dependsOn(this)
			macosArm64Main.dependsOn(this)
			dependencies {
				implementation(compose.ui)
				implementation(compose.foundation)
				implementation(compose.material3)
				implementation(compose.runtime)
				implementation(project(":mpfilepicker"))
			}
		}
	}
}

repositories {
	mavenCentral()
	maven { url = uri("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven") }
	maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
	google()
}

