
buildscript {

    extra["minSdkVersion"] = 15
    extra["compileSdkVersion"] = 34
    extra["targetSdkVersion"] = 34

    repositories {
        google()
        mavenCentral()
        jcenter()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:4.1.2")
       // classpath("com.android.tools.build:gradle:7.1.2")
        classpath("io.deepmedia.tools:publisher:0.4.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.21")
        classpath( "io.realm:realm-gradle-plugin:10.4.0")
        classpath( "com.google.gms:google-services:4.3.3")

        classpath(  "com.github.dcendents:android-maven-gradle-plugin:1.5")
        classpath("com.neenbedankt.gradle.plugins:android-apt:1.8")


    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
    }
}

tasks.register("clean", Delete::class) {
    delete(buildDir)
}