import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	war
	id("com.avast.gradle.docker-compose") version "0.17.6"
	id("org.springframework.boot") version "3.2.3"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.22"
	kotlin("plugin.spring") version "1.9.22"
	kotlin("plugin.jpa") version "1.9.22"
}

group = "org.ids"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_21
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("io.jsonwebtoken:jjwt-api:0.12.5")
	implementation("io.jsonwebtoken:jjwt-impl:0.12.5")
	implementation("io.jsonwebtoken:jjwt-jackson:0.12.5")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.flywaydb:flyway-mysql:10.0.0")
	implementation("org.flywaydb:flyway-core:10.0.0")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	developmentOnly("org.springframework.boot:spring-boot-docker-compose")
	runtimeOnly("com.mysql:mysql-connector-j")
	annotationProcessor("org.projectlombok:lombok")
	providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework:spring-test:6.1.6")
	testImplementation("org.flywaydb.flyway-test-extensions:flyway-spring-test:10.0.0")
	testImplementation("org.springframework.boot:spring-boot-docker-compose")
	testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.2")
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
	testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "21"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

buildscript {
	dependencies {
		classpath("org.flywaydb:flyway-mysql:10.0.0")
	}
}

dockerCompose {
	useComposeFiles.add("src/test/resources/compose.yaml")
	isRequiredBy(tasks.test)
}

tasks.test {
	doFirst {
		// expose the port as env variable POSTGRES_TCP_5432
		dockerCompose.exposeAsEnvironment(this@test)
	}
}