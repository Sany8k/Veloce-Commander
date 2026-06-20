plugins {
    id("java")
    id("application")
}

group = "com.commander"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val javafxVersion = "26"
val javafxPlatform = when {
    System.getProperty("os.name").lowercase().contains("win") -> "win"
    System.getProperty("os.name").lowercase().contains("mac") -> "mac"
    else -> "linux"
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.openjfx:javafx-base:$javafxVersion:$javafxPlatform")
    implementation("org.openjfx:javafx-graphics:$javafxVersion:$javafxPlatform")
    implementation("org.openjfx:javafx-controls:$javafxVersion:$javafxPlatform")
    implementation("io.github.mkpaz:atlantafx-base:2.1.0")
    implementation("de.jensd:fontawesomefx-fontawesome:4.7.0-9.1.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:6.1.0")
    testImplementation("org.assertj:assertj-core:3.27.7")
    testImplementation("org.mockito:mockito-core:5.23.0")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("app.Bootstrap")
}
