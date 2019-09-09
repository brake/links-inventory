import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.3.50"
    id("com.github.johnrengelman.shadow") version "5.1.0"
}

val ktorVersion = "1.2.4"
val kotlinTestVersion = "3.4.1"
val klaxonVersion = "5.0.12"
val jedisVersion = "3.1.0"

group = "com.github.brake"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-jackson:$ktorVersion")
    implementation("redis.clients:jedis:$jedisVersion")
    // DI
//    implementation("org.kodein.di:kodein-di-generic-jvm:6.3.3")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:$kotlinTestVersion")
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    withType<Test> {
        useJUnitPlatform()
    }
    val mainClassName = "com.github.brake.funbox_test.MainKt"
    val mainClassKey = "Main-Class"
    jar {
        manifest {
            attributes(mainClassKey to mainClassName)
        }
    }
    val shadowJar by getting(ShadowJar::class) {
        mergeServiceFiles()
    }
    build {
        dependsOn(shadowJar)
    }
}