plugins {
    kotlin("jvm") version "1.7.22"
}

repositories {
    mavenCentral()
}


dependencies {
    implementation("org.jgrapht:jgrapht-core:1.5.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.test {
    useJUnitPlatform()
}
