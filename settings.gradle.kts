pluginManagement {
	repositories {
		google()
		gradlePluginPortal()
		mavenCentral()
		maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
	}
}

rootProject.name = "MultiplatformFilePicker"

include(
	":mpfilepicker",
	":examples:android",
	":examples:jvm",
	":examples:web",
	":examples:macosX64",
	":examples:ios",
)
