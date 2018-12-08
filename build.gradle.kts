import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    val kotlinVersion = "1.2.71"
    id("org.springframework.boot") version "2.0.3.RELEASE"
    id("org.jetbrains.kotlin.jvm") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.jpa") version kotlinVersion
    id("io.spring.dependency-management") version "1.0.5.RELEASE"
}

version = "0.1.0-SNAPSHOT"
extra["azureVersion"] = "2.0.5"

group = "io.gontrum.auth"

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
}

repositories {
    mavenCentral()
}

application {
    mainClassName = "io.gontrum.auth.AuthenticatorApplicationKt"
}

dependencies {
    compile("org.springframework.boot:spring-boot-starter-web")
    compile("org.springframework.boot:spring-boot-starter-security")
    compile("org.springframework.security:spring-security-oauth2-client")
    compile("org.springframework.security:spring-security-oauth2-jose")
    compile("com.microsoft.azure:azure-active-directory-spring-boot-starter:${extra["azureVersion"]}")
    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    compile("org.jetbrains.kotlin:kotlin-reflect")
}

dependencyManagement {
    imports {
        mavenBom("com.microsoft.azure:azure-spring-boot-bom:${extra["azureVersion"]}")
    }
}