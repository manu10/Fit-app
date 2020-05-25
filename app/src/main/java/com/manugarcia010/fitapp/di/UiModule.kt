package com.manugarcia010.fitapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.manugarcia010.fitapp.timercustomizer.TimerCustomizerDIModule
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module(includes = [
    TimerCustomizerDIModule::class
])
class UiModule {

    @Provides
    fun provideViewModelFactory(
        providers: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
    ): ViewModelProvider.Factory =
        AppViewModelFactory(providers)
}