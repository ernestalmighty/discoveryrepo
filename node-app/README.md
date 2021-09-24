# Node JS app using the Kotlin JS output as a module

```sh
pnpm install
# after a ./gradlew build on the root project
node index.js
```

Importing the function works from kotlin, but running it breaks with the following error:

```sh
TypeError: Cannot set property 'exports' of undefined
    at shared/build/distributions/shared.js:2:203903
    at new Promise (<anonymous>)
    at _ (shared/build/distributions/shared.js:2:194966)
    at invoke_0 (shared/build/distributions/shared.js:2:366410)
    at initDb (shared/build/distributions/shared.js:2:366208)
    at initDb$default (shared/build/distributions/shared.js:2:366323)
    at initSqlDriver (shared/build/distributions/shared.js:2:365117)
    at $getWebInstanceCOROUTINE$9.doResume_0_k$ (shared/build/distributions/shared.js:2:1423561)
    at getWebInstance (shared/build/distributions/shared.js:2:1029260)
    at _no_name_provided__228.doResume_0_k$ (shared/build/distributions/shared.js:2:1424505)
```

This points to this function

```kotlin
// DiscoveryViewModelWeb.kt
suspend fun DiscoveryViewModel.Factory.getWebInstance() : DiscoveryViewModel {
    val sqlDriver: SqlDriver = initSqlDriver(DiscoveryDatabase.Schema).await() // <-- this line
    val repository = LocalRepository(sqlDriver)
    return DiscoveryViewModel(repository)
}
```

i.e. initialization of the SQL Delight DB 🤔
