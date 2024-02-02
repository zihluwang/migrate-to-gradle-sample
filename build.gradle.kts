import java.net.URI

plugins {
    id("java")
    id("java-library")
    id("maven-publish")
    id("signing")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

    withJavadocJar()
    withSourcesJar()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

configurations {
    compileOnly {
        extendsFrom(annotationProcessor.get())
    }
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    compileOnly("org.slf4j:slf4j-api:2.0.7")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    implementation("ch.qos.logback:logback-classic:1.4.14")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("migrateSample") {
            groupId = "cc.zihluwang"
            artifactId = "migrate-sample"
            version = "0.0.1-SNAPSHOT"

            pom {
                // 定义生成的 POM 文件
                name = "Migrate to Gradle Sample"
                description = "This is a sample for migrating from maven to gradle."
                url = "https://zihlu.wang/2024/01/migrate-from-maven-to-gradle"

                licenses {
                    license {
                        name = "The Apache License, Version 2.0"
                        url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                    }
                }

                scm {
                    connection = "ssh:git@github.com:zihluwang/migrate-to-gradle-sample.git"
                    developerConnection = "ssh:git@github.com:zihluwang/migrate-to-gradle-sample.git"
                    url = "github.com/zihluwang/migrate-to-gradle-sample"
                }

                developers {
                    developer {
                        id = "zihluwang"
                        name = "Zihlu Wang"
                        email = "really@zihlu.wang"
                        timezone = "Asia/Hong_Kong"
                    }
                }
            }

            from(components["java"]) // 这一行表示需要发布 jar 包

            // 如果不需要签名可以把这一段去掉
            signing {
                sign(publishing.publications["migrateSample"])
            }
        }

        repositories {
            // 在这里配置你的目标仓库，可以有多个
            maven { // 这是 Maven 中央仓库的示例
                name = "sonatypeNexus"
                url = URI(providers.gradleProperty("repo.ossrh.host").get())
                credentials {
                    username = providers.gradleProperty("repo.ossrh.username").get()
                    password = providers.gradleProperty("repo.ossrh.password").get()
                }
            }
        }
    }
}

