1. Add dependencies
   ```
   def koin_version = "3.1.5"
   implementation "io.insert-koin:koin-android:$koin_version"
   implementation "io.insert-koin:koin-androidx-navigation:$koin_version"
   ```
2. Sync project
3. Add `KoinGraph`-Object
4. Add `startKoin`-Call to `MainActivity`
    - Set `androidContext` with `applicationContext`
    - Set `modules` with `KoinGraph.mainModule`
5. Refactor `DatabaseSingleton`-Object to `DBConnection`-Class
    - Add a constructor parameter `context: Context`
    - Make `instance` public and initialize the variable directly
    - Remove all the `singleton` stuff
6. Refactor `ComicRepository` to extend `KoinComponent`
   - Declare the type for `comicsDB` to be `DBConnection`
   - use `by inject()` to initialize the `comicsDB`
   - Adjust the code to use the `instance` of `comicsDB`
7. Go to the `ComicsFragment`
   - Rename the `viewModel`-Variable to `comicViewModel`
   - Remove the `lateinit` from the variable
   - Use the `by viewModel()` call to initialise the variable
8. Go to the `ComicViewModel`
   - Remove the `ComicViewModelFactory`
9. Add `viewModel { ComicViewModel(get(), get()) }` to `KoinGraph`
10. Repeat step 6 to 9 for the Favorites-Part
   

