package com.composable.poekedex

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * An application class that is annotated with HiltAndroidApp, which sets up Hilt for dependency
 * injection. Hilt is a dependency injection library built on top of Dagger.
 */
@HiltAndroidApp
class PokeApplication : Application() {

    /**
     * Called when the application is starting, before any other application objects have been
     * created. This method is used to initialize the application and set up any necessary
     * components.
     */
//    override fun onCreate() {
//        super.onCreate()
//        // Plant a new instance of Timber's DebugTree, which is used for logging during development.
//        Timber.plant(Timber.DebugTree())
//    }
}
