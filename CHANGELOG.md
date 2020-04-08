Changelog
=========

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
