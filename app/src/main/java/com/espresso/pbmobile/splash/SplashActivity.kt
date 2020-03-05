package com.espresso.pbmobile.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.espresso.pbmobile.R
import com.espresso.pbmobile.main.MainActivity
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {

    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Completable.timer(500L, TimeUnit.MILLISECONDS)
            .subscribe {
                MainActivity.createIntent(this)
            }.let(disposables::add)
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}
