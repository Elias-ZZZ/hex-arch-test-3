import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
  java
  application
  id("com.github.johnrengelman.shadow") version "7.0.0"
  id("io.vertx.vertx-plugin") version "1.3.0"
}

group = "com.example"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
  mavenLocal()
  jcenter()
}

val vertxVersion = "4.1.2"
val junitJupiterVersion = "5.7.0"

val mainVerticleName = "com.example.starter.MainVerticle"
val launcherClassName = "io.vertx.core.Launcher"

val watchForChange = "src/**/*"
val doOnChange = "${projectDir}/gradlew classes"

application {
  mainClass.set(launcherClassName)
}

vertx {
  mainVerticle = mainVerticleName
  redeploy = false
  vertxVersion = vertxVersion
  launcher = launcherClassName

}

dependencies {
  implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
  implementation("io.vertx:vertx-web-client")
  implementation("io.vertx:vertx-service-proxy")
  implementation("io.vertx:vertx-web")
  implementation("io.vertx:vertx-mysql-client")
  implementation("io.vertx:vertx-service-discovery")
  implementation("io.vertx:vertx-hazelcast")
  implementation("io.vertx:vertx-json-schema")
  implementation("io.vertx:vertx-reactive-streams")
  implementation("io.vertx:vertx-jdbc-client")
  implementation("io.vertx:vertx-config")
  implementation("io.vertx:vertx-rx-java2")
  implementation("io.vertx:vertx-circuit-breaker")
  implementation("io.vertx:vertx-rx-java")
  implementation("io.vertx:vertx-mongo-client")
  testImplementation("io.vertx:vertx-unit")
  testImplementation("junit:junit:4.13.1")

  annotationProcessor("io.vertx:vertx-rx-java2-gen:$vertxVersion")
  //annotationProcessor("io.vertx:vertx-service-proxy:$vertxVersion:processor")
  compileOnly("io.vertx:vertx-codegen:$vertxVersion")
  annotationProcessor("io.vertx:vertx-codegen:$vertxVersion")
}

tasks.register<JavaCompile>("annotationProcessing") {
  group = "build"

  source = sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME).java
  destinationDir = project.file("${project.projectDir}/src/generated/main/java")
  classpath = configurations.compileClasspath.get()
  options.annotationProcessorPath = configurations.compileClasspath.get()
  options.compilerArgs = listOf(
    "-proc:only",
    "-processor", "io.vertx.codegen.CodeGenProcessor",
    "-Acodegen.output=${project.projectDir}/src/main"
  )
}

tasks.compileJava {
  dependsOn(tasks.named("annotationProcessing"))
  source += sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME).java
  options.compilerArgs.add("-proc:none")
}

sourceSets {
  main {
    java {
      srcDirs(project.file("${project.projectDir}/src/generated/"))
    }
  }
}

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<ShadowJar> {
  archiveClassifier.set("fat")
  manifest {
    attributes(mapOf("Main-Verticle" to mainVerticleName))
  }
  mergeServiceFiles()
}

tasks.withType<Test> {
  useJUnit()
  testLogging {
    events = setOf(PASSED, SKIPPED, FAILED)
  }
}

tasks.withType<JavaExec> {
  args = listOf("run", mainVerticleName, "--redeploy=$watchForChange", "--launcher-class=$launcherClassName", "--on-redeploy=$doOnChange")
}
