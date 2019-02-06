# android-BalloonWindow

[![](https://jitpack.io/v/hyuntae0117/android-BalloonWindow.svg)](https://jitpack.io/#hyuntae0117/android-BalloonWindow)

## Download

```kotlin
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.hyuntae0117:android-BalloonWindow:0.1.1'
}
```

## Usage

### basic usage
```kotlin
val view = TextView(this)
view.text = "position:below"

val window = BalloonWindow(this, targetView, BalloonWindow.Position.below)
window.offset = 48
window.paddingTop = 40
window.margin = 10
window.show(view)
```

### position

### offset

### padding
