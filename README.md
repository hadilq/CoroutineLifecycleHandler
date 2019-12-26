[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.hadilq/coroutinelifecyclehandler/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.hadilq/coroutinelifecyclehandler)
[![CircleCI](https://circleci.com/gh/hadilq/CoroutineLifecycleHandler.svg?style=svg)](https://circleci.com/gh/hadilq/CoroutineLifecycleHandler)
[![codecov](https://codecov.io/gh/hadilq/coroutinelifecyclehandler/branch/master/graph/badge.svg)](https://codecov.io/gh/hadilq/coroutinelifecyclehandler)

Coroutine Lifecycle Handler
---
This library is a glue between the lifecycle of `androidx.lifecycle:lifecycle-extensions` and `Flow` class of 
`org.jetbrains.kotlinx:kotlinx-coroutines-core` library. The assumption is that we want the emitted values between 
`start` and `stop` of `LifecycleOwner`, so on other moments this library would unsubscribe from the upstream of `Flow`.

Also you can find its tween library for RxJava in https://github.com/hadilq/RxLifecycleHandler/.

Usage
---
This source has a sample app, which doesn't do anything, where you can find the usage in `MainActivity`.

```kotlin
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        ...

        val flow: Flow<String> = ...
        
        // Flow usage
        flow
            .onEach { }
            .catch { }
            .onCompletion { }
            .observeIn()()
    }
}
```

Enjoy!

Download
---
Download via gradle
```groovy
implementation "com.github.hadilq:coroutinelifecyclehandler:$libVersion"
```
where the `libVersion` is [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.hadilq/coroutinelifecyclehandler/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.hadilq/coroutinelifecyclehandler).

Contribution
---
Just create your branch from the master branch, change it, write additional tests, satisfy all 
tests, create your pull request, thank you, you're awesome.
