package com.espresso.pbmobile.splash

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.espresso.data.RetrofitClient
import com.espresso.pbmobile.R
import com.espresso.pbmobile.auth.AuthActivity
import com.espresso.pbmobile.databinding.ActivitySplashBinding
import com.espresso.pbmobile.main.MainActivity
import com.espresso.data.store.Store
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {
    private val store by lazy { Store(this) }
    private val disposables = CompositeDisposable()
    private val binding by lazy(LazyThreadSafetyMode.NONE) { DataBindingUtil.setContentView<ActivitySplashBinding>(this, R.layout.activity_splash) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RetrofitClient.initRetrofitClient()
        binding.image.setBackgroundResource(R.drawable.splash_animation_list)
        (binding.image.background as AnimationDrawable).start()

        Completable.timer(TIMER_DELAY, TimeUnit.MILLISECONDS)
            .subscribe {
                openNextScreen()
            }.let(disposables::add)
    }

    private fun openNextScreen() {
        val intent = if (store.userId == Store.LONG_DEFAULT_VALUE) { AuthActivity.createIntent(this) }
        else MainActivity.createIntent(this)

        startActivity(intent.apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        })
        finish()
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    companion object {
        private const val TIMER_DELAY = 700L
    }
}
