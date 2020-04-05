package com.espresso.pbmobile.main.carwash

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.espresso.data.RetrofitClient
import com.espresso.data.models.carwash.CarWashReservationModel
import com.espresso.pbmobile.R
import com.espresso.pbmobile.databinding.FragmentCarWashBinding
import com.espresso.pbmobile.history.CarWashHistoryItemModel
import com.espresso.pbmobile.history.HistoryActivity
import com.espresso.pbmobile.history.HistoryActivityViewModel
import com.espresso.pbmobile.utlis.RecyclerMarginDecorator
import io.reactivex.disposables.CompositeDisposable
import java.text.DateFormat
import java.util.*

class CarWashFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    private lateinit var binding: FragmentCarWashBinding
    private val service = RetrofitClient.getInstance()
    private val calendar = Calendar.getInstance()
    private var reservationDate: String = ""
    private val disposables = CompositeDisposable()
    private var actualCarWashType: CarWashItemModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_car_wash, container, false)
        setupRecycler()
        setupReservationRecycler()
        setupBindings()
        return binding.root
    }

    private fun setupReservationRecycler() {
        binding.reservationItems = listOf(
            CarWashReservationModel("12:30", ::carWashReservationItemClickHandler),
            CarWashReservationModel("13:00", ::carWashReservationItemClickHandler),
            CarWashReservationModel("13:30", ::carWashReservationItemClickHandler),
            CarWashReservationModel("14:00", ::carWashReservationItemClickHandler),
            CarWashReservationModel("14:00", ::carWashReservationItemClickHandler),
            CarWashReservationModel("14:00", ::carWashReservationItemClickHandler),
            CarWashReservationModel("14:00", ::carWashReservationItemClickHandler),
            CarWashReservationModel("14:00", ::carWashReservationItemClickHandler),
            CarWashReservationModel("14:00", ::carWashReservationItemClickHandler),
            CarWashReservationModel("14:00", ::carWashReservationItemClickHandler),
            CarWashReservationModel("14:00", ::carWashReservationItemClickHandler),
            CarWashReservationModel("14:00", ::carWashReservationItemClickHandler),
            CarWashReservationModel("14:00", ::carWashReservationItemClickHandler),
            CarWashReservationModel("14:00", ::carWashReservationItemClickHandler),
            CarWashReservationModel("14:00", ::carWashReservationItemClickHandler),
            CarWashReservationModel("14:00", ::carWashReservationItemClickHandler),
            CarWashReservationModel("14:00", ::carWashReservationItemClickHandler),
            CarWashReservationModel("14:00", ::carWashReservationItemClickHandler),
            CarWashReservationModel("14:30", ::carWashReservationItemClickHandler)
        )
    }

    private fun carWashReservationItemClickHandler(model: CarWashReservationModel) {
        startActivity(
            ConfirmReservationActivity.createIntent(
                requireContext(), ConfirmReservationModel(
                    carWashType = actualCarWashType?.title ?: "Myjnia",
                    date = reservationDate,
                    hour = model.hour
                )
            )
        )
    }

    private fun getReservationList(date: String) {
        reservationDate = date
        println("TEKST DATE $date")
    }

    private fun setupRecycler() {
        binding.items = listOf(
            CarWashItemModel(1, CarWashItemModel.CAR_WASH, ::handleCarWashClick),
            CarWashItemModel(2, CarWashItemModel.CAR_WASH_WAX, ::handleCarWashClick)
        )
        binding.carWashRecycler.addItemDecoration(RecyclerMarginDecorator(requireContext()))
    }

    private fun handleCarWashClick(model: CarWashItemModel) {
        actualCarWashType = model
        binding.items?.map {
            it.copy(clicked = it.id == model.id)
        }?.let(binding::setItems)
    }

    private fun setupBindings() {
        binding.getDateButton.setOnClickListener {
            if (actualCarWashType != null) createDatePicker()
            else Toast.makeText(requireContext(), "Wybierz typ myjni", Toast.LENGTH_SHORT).show()
        }
        binding.historyButton.setOnClickListener {
            getHistoryItems()
        }
    }

    private fun getHistoryItems() {
/*            service.getRefuelHistory(store.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    it.reversed().map { model ->
                        CarWashHistoryItemModel(
                            date = model.dateRefueling.substring(0, 10),
                        )
                    }
                }
                .subscribe { items ->
                    openHistoryActivity(items)
                }
                .let(disposables::add)*/

        openHistoryActivity(
            listOf(
                CarWashHistoryItemModel("12.03.2020", "12:30", "Myjnia"),
                CarWashHistoryItemModel("12.03.2020", "12:30", "Myjnia z woskiem"),
                CarWashHistoryItemModel("12.03.2020", "12:30", "Myjnia"),
                CarWashHistoryItemModel("12.03.2020", "12:30", "Myjnia z woskiem")
            )
        )

    }

    private fun openHistoryActivity(items: List<CarWashHistoryItemModel>) {
        startActivity(HistoryActivity.createIntent(requireContext(), HistoryActivityViewModel(items, HistoryActivity.TYPE_CAR_WASH)))
    }

    private fun createDatePicker() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(requireContext(), this, year, month, day).show()
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }
        val date = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.time)
        getReservationList(date)
    }

    companion object {
        fun createInstance() = CarWashFragment()
    }
}
