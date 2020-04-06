package com.espresso.pbmobile.main.carwash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.espresso.data.RetrofitClient
import com.espresso.data.models.carwash.ReservationModel
import com.espresso.data.store.Store
import com.espresso.pbmobile.R
import com.espresso.pbmobile.databinding.ActivityConfirmReservationBinding
import com.espresso.pbmobile.utlis.DateParser
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ConfirmReservationActivity : AppCompatActivity() {
    private val binding by lazy { DataBindingUtil.setContentView<ActivityConfirmReservationBinding>(this, R.layout.activity_confirm_reservation) }
    private val store by lazy { Store(this) }
    private val service = RetrofitClient.getInstance()
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val model = intent.getSerializableExtra(MODEL) as ConfirmReservationModel
        binding.model = model
        binding.reservationButton.clicks()
            .subscribe {
                val date = DateParser.parse(model.date, DateParser.shortPattern, DateParser.shortReversedPattern)
                addReservation(ReservationModel(dateOfReservation = "${date}T${model.hour}", type = model.carWashType))
            }
            .let(disposables::add)
    }

    private fun addReservation(model: ReservationModel) {
        service.addReservation(store.userId, model)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe{binding.loading = true}
            .doAfterTerminate { binding.loading = false }
            .subscribe {
                Toast.makeText(this, getString(R.string.message_reservation_added_succesfully), Toast.LENGTH_SHORT).show()
                finish()
            }
            .let(disposables::add)
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    companion object {
        private const val MODEL = "model"
        fun createIntent(context: Context, model: ConfirmReservationModel) = Intent(context, ConfirmReservationActivity::class.java).apply {
            putExtra(MODEL, model)
        }
    }
}
