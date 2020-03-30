package com.espresso.pbmobile.main.refueling

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.espresso.data.RetrofitClientInstance
import com.espresso.data.models.profile.UserProfile
import com.espresso.data.models.refuel.RefuelProductsRepo
import com.espresso.data.store.Store
import com.espresso.pbmobile.R
import com.espresso.pbmobile.databinding.FragmentRefuelingBinding
import com.espresso.pbmobile.history.HistoryActivity
import com.espresso.pbmobile.history.HistoryActivityViewModel
import com.espresso.pbmobile.history.RefuelHistoryItemModel
import com.espresso.pbmobile.main.payment.PayActivity
import com.espresso.pbmobile.main.refueling.RefuelFragmentViewModel.Companion.REFUEL_MAX_VALUE
import com.espresso.pbmobile.utlis.AnimationListener
import com.espresso.pbmobile.utlis.RecyclerMarginDecorator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.ZoneId
import java.util.*
import kotlin.random.Random
import kotlin.time.days
import kotlin.time.hours

class RefuelingFragment : Fragment() {
    private val service = RetrofitClientInstance.getInstance()
    private lateinit var binding: FragmentRefuelingBinding
    private lateinit var store: Store

    private val disposables = CompositeDisposable()
    private val initialValue = Random.nextInt(0, 50)
    private val fuelPercentageSubject = BehaviorSubject.create<Int>()
    private val stateSubject = BehaviorSubject.createDefault(FuelingState.INACTION)
    private var canFuel = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRefuelingBinding.inflate(inflater, container, false)
        store = Store(requireContext())
        setupViewModel()
        subscribeToSubjects()
        setupBindings()
        setupRecycler()
        return binding.root
    }

    private fun setupViewModel() {
        RefuelProductsRepo.products().map {
            it.filter { product ->
                product.category == FILTER_PREDICATE
            }.map { product ->
                RefuelItemModel.create(
                    name = product.productName,
                    value = product.priceBrutto,
                    id = product.id,
                    clickHandler = ::handleItemClick
                )
            }
        }
            .subscribe { items ->
                binding.distributorView.items = items
                binding.distributorView.refuelRecycler.adapter = RefuelRecyclerAdapter(items).apply { setHasStableIds(true) }
            }.let(disposables::add)

        binding.model = RefuelFragmentViewModel(isRegistered = UserProfile.TYPE_UNREGISTERED != store.userType)
        fuelPercentageSubject.onNext(initialValue)
    }

    private fun subscribeToSubjects() {
        stateSubject.subscribe {
            updateViewModel { copy(state = it) }
        }.let(disposables::add)

        fuelPercentageSubject.subscribe { value ->
            if (value == REFUEL_MAX_VALUE) {
                stateSubject.onNext(FuelingState.FULL_TANK)
                binding.carImage.clearAnimation()
            }
            updateDetailsModel(value)
        }.let(disposables::add)
    }

    private fun updateDetailsModel(actualValue: Int) {
        val model = binding.model?.detailsModel ?: return
        val realValue = (actualValue - initialValue) / SCALE_FACTOR
        val price = BigDecimal(model.pricePerUnit.times(realValue)).setScale(2, RoundingMode.HALF_UP)
        updateViewModel {
            copy(detailsModel = model.copy(capacity = realValue, price = price.toDouble(), percentageValue = actualValue))
        }
    }

    private fun handleItemClick(model: RefuelItemModel) {
        canFuel = true
        updateViewModel {
            copy(detailsModel = RefuelItemDetailsModel(pricePerUnit = model.pricePerUnit, percentageValue = initialValue, id = model.id, product = model.name), activeItem = model)
        }
        binding.distributorView.items?.map { it.copy(isClicked = it.id == model.id) }?.let { items ->
            (binding.distributorView.refuelRecycler.adapter as RefuelRecyclerAdapter).updateItems(items)
        }
    }

    private fun updateViewModel(model: RefuelFragmentViewModel.() -> RefuelFragmentViewModel) {
        binding.model?.let(model).let(binding::setModel)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupBindings() {
        binding.refuelButton.setOnClickListener {
            when (stateSubject.value) {
                FuelingState.INACTION -> {
                    if (canFuel) {
                        stateSubject.onNext(FuelingState.FUELING)
                        fuelPercentageSubject.value?.let { if (it < REFUEL_MAX_VALUE) refuelAnimation() }
                    } else {
                        Toast.makeText(requireContext(), "Wybierz typ paliwa", Toast.LENGTH_SHORT).show()
                    }
                }
                FuelingState.FUELING -> {
                    stateSubject.onNext(FuelingState.INACTION)
                    binding.carImage.clearAnimation()
                }
                FuelingState.FULL_TANK -> {
                }
            }
        }
        binding.payButton.setOnClickListener {
            startActivity(binding.model?.detailsModel?.let { model -> PayActivity.createIntent(requireContext(), model) })
        }

        binding.historyButton.setOnClickListener {
            service.getRefuelHistory(store.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    it.map { model ->
                        RefuelHistoryItemModel(
                            date = "${model.dateRefueling.time}",
                            cost = model.product.priceBrutto * model.quantity,
                            fuelType = model.product.productName
                        )
                    }
                }
                .subscribe { items ->
                    openHistoryActivity(items)
                }
                .let(disposables::add)
        }
    }

    private fun openHistoryActivity(items: List<RefuelHistoryItemModel>) {
        startActivity(HistoryActivity.createIntent(requireContext(), HistoryActivityViewModel(items, HistoryActivity.TYPE_REFUEL)))
    }

    private fun refuelAnimation() {
        if (stateSubject.value == FuelingState.FUELING) {
            binding.carImage.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.scale_up).apply {
                setAnimationListener(AnimationListener(onAnimationEnd = {
                    binding.carImage.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.scale_down).apply {
                        setAnimationListener(AnimationListener(onAnimationEnd = {
                            if (stateSubject.value == FuelingState.FUELING) {
                                fuelPercentageSubject.value?.plus(Random.nextInt(1, 3))?.coerceAtMost(REFUEL_MAX_VALUE)
                                    ?.let { fuelPercentageSubject.onNext(it) }
                                refuelAnimation()
                            }
                        }))
                    })
                }))
            })
        }
    }

    private fun setupRecycler() {
        binding.distributorView.refuelRecycler.addItemDecoration(RecyclerMarginDecorator(requireContext()))
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    companion object {
        private const val FILTER_PREDICATE = "Petrol"
        private const val SCALE_FACTOR = 2.0
        fun createInstance() = RefuelingFragment()
    }
}
