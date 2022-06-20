## Initializr

A framework for app initialization.

## Getting Started

Configuring [booster](https://github.com/didi/booster) plugin and initializr booster plugin:

```kotlin
buildscripts {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${agpVersion}")
        classpath("com.didiglobal.booster:booster-gradle-plugin:${boosterVersion}")
        classpath("io.johnsonlee.initializr:compiler:${initializrVersion}")
    }
}
```

Then, applying plugins:

```kotlin
plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.didiglobal.booster")
}
```

Then, configuring initializr compiler and runtime library:

```kotlin
dependencies {
    kapt("io.johnsonlee.initializr:compiler:${initializrVersion}")
    implementation("io.johnsonlee.initializr:runtime:${initializrVersion}")
}
```

## Example

### PrimaryInitializer

```kotlin
@Initialization(
    name = "PrimaryInitializer",
    dependencies = []
)
class PrimaryInitializer : AbstractInitializer {

    override fun initialize(context: Context) {
    }

}
```

### SecondaryInitializer

```kotlin
@Initialization(
    name = "SecondaryInitializer",
    dependencies = ["PrimaryInitializer"]
)
class SecondaryInitializer : AbstractInitializer {

    override fun initialize(context: Context) {
    }

}
```

### MyApplication

```kotlin
class MyApplication: Application() {

    fun onCreate() {
        super.onCreate()
        Initiator.init(this)
    }

}
```
