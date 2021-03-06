Changelog
=========

0.4.2
-----

_2020-08-25_

Update androidx.lifecycle and coroutines libraries

0.4.1
-----

_2020-08-21_

Add `execute` extension function

0.4.0
-----

_2020-08-21_

 - Move to Kotlin Multiplatform
  + The previous artifactId was `coroutinelifecyclehandler` which is now as following:
     * For Android, it's `coroutines-lifecycle-handler-android`
     * For Jvm, it's `coroutines-lifecycle-handler-jvm`
     * For Common, it's `coroutines-lifecycle-handler-metadata`
 - Remove `buildSrc`, instead use composite build with `build-plugin`
 - Move `Entry`, and `EEntry`
 - Create `DEntry`
 - Configure JaCoCo
 - Configure Maven Publication
 - Configure Dokka

0.3.3
-----

_2020-04-11_

Add `Flow.toLife` and `Flow.toELife` extension functions.

0.3.2
-----

_2020-04-09_

Fix resolving problem.

0.3.1
-----

_2020-04-08_

Add `@ExperimentalCoroutinesApi` and `@FlowPreview` annotations.

0.3.0
-----

_2020-04-08_

Renaming:
 - `AndroidExtendedLifecycleHandlerImpl` to `AndroidELifecHandlerImpl`
 - `AndroidExtendedLifecycleHandler` to `AndroidELifecHandler`
 - `AndroidLifecycleHandlerImpl` to `AndroidLifeHandlerImpl`
 - `AndroidLifecycleHandler` to `AndroidLifeHandler`
 - `BaseLifecycleHandler` to `BaseLifeHandler`
 - `ExtendedLife` to `ELife`
 - `CoroutineExtendedLifecycleHandlerImpl` to `CoroutineELifeHandlerImpl`
 - `CoroutineExtendedLifecycleHandler` to `CoroutineELifeHandler`
 - `CoroutineLifeHandlerImpl` to `CoroutineLifeHandlerImpl`
 - `CoroutineLifeHandler` to `CoroutineLifeHandler`
 - `ExtendedEntry` to `EEntry`
 - `ExtendedLifecycleAwareImpl` to `ELifeAwareImpl`
 - `ExtendedLifecycleAware` to `ELifeAware`
 - `LifecycleAwareImpl` to `LifeAwareImpl`
 - `LifecycleAware` to `LifeAware`
 - `toExtendedLifecycleAware` to `toELifeAware`
 - `toLifecycleAware` to `toLifeAware`

Adding:
 - `LifeSpan#CONFIGURATION_CHANGED`
 - `LifeSpan#USER_FLOW`
 - `LifeStore`
 - `SLife`

0.2.0
-----

_2020-02-02_

Implement `AndroidLifecycleHandlerImpl` and `AndroidExtendedLifecycleHandlerImpl`. Also make the
`ExtendedLife#onBorn` method accepts null bundle.

Also implement `CoroutineExtendedLifecycleHandlerImpl`, `toLifecycleAware` and `toExtendedLifecycleAware`.

0.1.0
-----

_2020-01-15_

Initial release
