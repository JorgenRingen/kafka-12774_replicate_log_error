import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.4.5"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("com.google.cloud.tools.jib") version "2.8.0"
    kotlin("jvm") version "1.4.30"
    kotlin("plugin.spring") version "1.4.30"
    jacoco
}

group = "org.example" // <<replace-me>>
version = "0.1"

java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenLocal()
    jcenter()
    maven(url = "https://packages.confluent.io/maven/")
}

configurations {
    "implementation" {
        exclude(module = "spring-boot-starter-logging")
        exclude(group = "ch.qos.logback")
    }
}

jib {
    from {
        image = "adoptopenjdk:11-jre-hotspot"
    }
    to {
        credHelper = "ecr-login"
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

jacoco {
    toolVersion = "0.8.6"
    reportsDir = file("$buildDir/reports/jacoco")
}


extra["kafka.version"] = "2.8.0"

dependencies {

    // Spring dependencies
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-log4j2")

    // Micrometer
    implementation("io.micrometer:micrometer-registry-prometheus")

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // Kafka
    implementation("org.apache.kafka:kafka-streams")

    ////////////////////////////////////////////////////////////
    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }

    testImplementation("io.mockk:mockk:1.11.0")
    testImplementation ("org.apache.kafka:kafka-streams-test-utils")

    // optional
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("org.awaitility:awaitility")
}

tasks {
    jacocoTestReport {
        reports {
            xml.isEnabled = true
            html.isEnabled = true
            csv.isEnabled = false
        }
    }

    test {
        useJUnitPlatform()
        failFast = false
        testLogging {
            showCauses = true
            showExceptions = true
            showStackTraces = true
            exceptionFormat = TestExceptionFormat.FULL
            displayGranularity = 1
        }
        reports.html.isEnabled = false
        reports.junitXml.isEnabled = true
    }
}
