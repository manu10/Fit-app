package com.manugarcia010.fitapp

import android.app.Application
import androidx.fragment.app.Fragment
import com.manugarcia010.fitapp.di.AppComponent
import com.manugarcia010.fitapp.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class App : Application(), HasSupportFragmentInjector {
    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>


    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder()
            .context(this)
            .build()
            .inject(this)
    }

    override fun supportFragmentInjector() = fragmentInjector
}