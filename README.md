[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.hadilq/coroutinelifecyclehandler/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.hadilq/coroutinelifecyclehandler)
[![CircleCI](https://circleci.com/gh/hadilq/CoroutineLifecycleHandler.svg?style=svg)](https://circleci.com/gh/hadilq/CoroutineLifecycleHandler)
[![codecov](https://codecov.io/gh/hadilq/CoroutineLifecycleHandler/branch/master/graph/badge.svg)](https://codecov.io/gh/hadilq/CoroutineLifecycleHandler)

Coroutine Lifecycle Handler
---
This library is a glue between the lifecycle of `androidx.lifecycle:lifecycle-extensions` and `Flow` class of 
`org.jetbrains.kotlinx:kotlinx-coroutines-core` library. The assumption is that we want the emitted values between 
`start` and `stop` of `LifecycleOwner`, so on other moments this library would unsubscribe from the upstream of `Flow`.
Also, we support a more general assumption that we want the emitted values after `start` and before `stop` or
`saveState` of `SavedStateRegistryOwner`, depends which one gets called sooner.

Also you can find its twin library for RxJava in https://github.com/hadilq/RxLifecycleHandler/.

Usage
---
This source has a sample app, which doesn't do anything, where you can find the usage in `MainActivity`,
`MainViewModelActivity` and `ScopeMainActivity`. However, in `MVVM` or `MVI` architectural patterns, you can use it
based on the data you want to save in `onSaveInstanceState` or not. For now let's restrict ourselves to `MVVM`.
If the data you want to propagate is an action, then you probably don't want to save it so in `ViewModel` you have.
```kotlin
class MainViewModel : ViewModel() {

    private val publisher = BroadcastChannel<String>(CONFLATED)
    val stringEmitter = publisher.toLifecycleAware()
}

```
And in the `Activity` or `Fragment` you have.
```kotlin
class MyViewModelActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        (viewModel.stringEmitter.observe()) { testString ->
            /* use it here */
        }
    }
}
```
But if you want to save the propagated data in `onSaveInstanceState` method, then you may want to
use it as follows.
```kotlin
class MainViewModel : ViewModel() {

    private val extendedPublisher = BroadcastChannel<String>(CONFLATED)

    val extendedStringEmitter = extendedPublisher.toELifeAware(KEY)

    companion object {
        private const val KEY = "key_to_save_string_emitter"
    }
}

```
And in the `Activity` or `Fragment` you have.
```kotlin
class MyViewModelActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        (viewModel.extendedStringEmitter.observe()) { testString ->
            /* use it here */
        }
    }
}
```
Here, you may noticed the extra parentheses above, or you may noticed that we didn't passe the
`Activity` or `Fragment` to the `observe` method. In both cases, read the last sentence again!
Because they're related to each other, which means in this library we preferred to avoid writing
`this` to pass the `Activity` or `Fragment` directly in trade off of having odd position of parentheses.

Any way, in case you want to use this library out of `MVVM` architectural pattern, you can use it
 as follows.
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

        // OR
        flow
            .onEach { }
            .catch { }
            .onCompletion { }
            .observeIn(life, KEY)()
    }

    companion object {
        private const val KEY = "key_to_save_the_data"
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
