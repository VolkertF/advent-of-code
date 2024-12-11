repositories{
    mavenCentral()
}

plugins{
    id("idea")
    kotlin("jvm") version "2.1.0"
}

dependencies{
    implementation(group = "org.jetbrains.kotlinx", name= "kotlinx-coroutines-core", version= "1.7.3")
    testImplementation(group = "org.junit.jupiter", name= "junit-jupiter-api", version= "5.10.1")
    testRuntimeOnly(group = "org.junit.jupiter", name= "junit-jupiter-engine", version= "5.10.1")
}

tasks.test{
    useJUnitPlatform()
}