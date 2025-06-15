plugins {
	kotlin("jvm") version "1.9.25"
}

repositories {
	mavenCentral()
}

dependencies {
	// если DTO нужно сериализовать в JSON:
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.1")
}
