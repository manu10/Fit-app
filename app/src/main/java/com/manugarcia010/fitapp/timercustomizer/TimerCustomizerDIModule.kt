package com.manugarcia010.fitapp.timercustomizer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.manugarcia010.fitapp.di.ViewModelKey
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module(includes = [
    TimerCustomizerDIModule.ProvideViewModel::class
])
abstract class TimerCustomizerDIModule {

    @ContributesAndroidInjector(
        modules = [
            InjectViewModel::class
        ]
    )
    abstract fun bind(): TimerCustomizerFragment

    @Module
    class ProvideViewModel {

        @Provides
        @IntoMap
        @ViewModelKey(TimerCustomizerViewModel::class)
        fun provideAddNoteViewModel(): ViewModel =
            TimerCustomizerViewModel()
    }

    @Module
    class InjectViewModel {

        @Provides
        fun provideAddNoteViewModel(
            factory: ViewModelProvider.Factory,
            target: TimerCustomizerFragment
        ) = ViewModelProvider(target, factory).get(TimerCustomizerViewModel::class.java)
    }

}
