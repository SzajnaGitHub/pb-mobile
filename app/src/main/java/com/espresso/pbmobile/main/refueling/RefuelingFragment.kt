package com.espresso.pbmobile.main.refueling

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.espresso.pbmobile.R
import com.espresso.pbmobile.databinding.FragmentRefuelingBinding
import com.espresso.pbmobile.history.HistoryActivity
import com.espresso.pbmobile.history.HistoryActivityViewModel
import com.espresso.pbmobile.history.RefuelHistoryItemModel
import com.espresso.pbmobile.main.refueling.RefuelFragmentViewModel.Companion.REFUEL_MAX_VALUE
import com.espresso.pbmobile.main.refueling.RefuelItemModel.Companion.ITEM_REFUEL_95
import com.espresso.pbmobile.main.refueling.RefuelItemModel.Companion.ITEM_REFUEL_98
import com.espresso.pbmobile.main.refueling.RefuelItemModel.Companion.ITEM_REFUEL_LPG
import com.espresso.pbmobile.main.refueling.RefuelItemModel.Companion.ITEM_REFUEL_ON
import com.espresso.pbmobile.utlis.AnimationListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.roundToInt
import kotlin.random.Random

class RefuelingFragment : Fragment() {
    private lateinit var binding: FragmentRefuelingBinding
    private val disposables = CompositeDisposable()
    private var itemList: List<RefuelItemModel>? = null
    private var state = FuelingState.INACTION
    private val fuelPercentageSubject = BehaviorSubject.create<Int>()
    private val initialRefuelValue = Random.nextInt(0, 50)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRefuelingBinding.inflate(inflater, container, false)
        setupViewModel()
        subscribeToRefuelSubject()
        setupBindings()
        setupRecycler()
        setupDetailsModel()
        fuelPercentageSubject.onNext(initialRefuelValue)
        return binding.root
    }

    private fun setupDetailsModel(model: RefuelItemDetailsModel = RefuelItemDetailsModel()) {
        binding.detailsModel = model
    }

    private fun setupViewModel() {
        itemList = listOf(
            RefuelItemModel(false, "95", ITEM_REFUEL_95, 4.66, ::handleItemClick),
            RefuelItemModel(false, "98", ITEM_REFUEL_98, 4.91, ::handleItemClick),
            RefuelItemModel(false, "ON", ITEM_REFUEL_ON, 4.77, ::handleItemClick),
            RefuelItemModel(false, "LPG", ITEM_REFUEL_LPG, 2.01, ::handleItemClick)
        )

        binding.distributorView.items = itemList
        binding.model = RefuelFragmentViewModel(
            state = state,
            initialRefuelValue = initialRefuelValue
        )
    }

    private fun subscribeToRefuelSubject() {
        fuelPercentageSubject.subscribe { value ->
            if (value == REFUEL_MAX_VALUE) {
                binding.refuelButton.text = "Tankuj"
                state = FuelingState.FULL_TANK
                binding.carImage.clearAnimation()
                binding.refuelButton.isEnabled = false
            }
            binding.refuelPercentage.text = "${value}%"
            updateDetailsModel(value)
        }.let(disposables::add)
    }

    private fun updateDetailsModel(actualValue: Int) {
        val model = binding.detailsModel
        val realValue = (actualValue - initialRefuelValue) / 2.0
        val price = BigDecimal(model?.pricePerUnit?.times(realValue) ?: 0.0).setScale(2, RoundingMode.HALF_UP)
        binding.detailsModel = model?.copy(capacity = realValue, price = price.toDouble())
    }

    private fun handleItemClick(model: RefuelItemModel) {
        binding.detailsModel = binding.detailsModel?.copy(pricePerUnit = model.pricePerUnit)
        updateViewModel {
            copy(
                detailsModel = RefuelItemDetailsModel(pricePerUnit = model.pricePerUnit),
                activeItem = model
            )
        }
        itemList?.map { it.copy(isClicked = it.id == model.id) }?.let { items ->
            (binding.distributorView.refuelRecycler.adapter as RefuelRecyclerAdapter).updateItems(items)
        }
    }

    private fun updateViewModel(model: RefuelFragmentViewModel.() -> RefuelFragmentViewModel) {
        binding.model?.let(model).let(binding::setModel)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupBindings() {
        binding.refuelButton.setOnClickListener {
            when (state) {
                FuelingState.INACTION -> {
                    binding.refuelButton.text = "Stop"
                    binding.historyButton.visibility = View.GONE
                    binding.payButton.isEnabled = false
                    state = FuelingState.FUELING
                    fuelPercentageSubject.value?.let { if (it < REFUEL_MAX_VALUE) refuelAnimation() }
                }
                FuelingState.FUELING -> {
                    binding.refuelButton.text = "Tankuj"
                    state = FuelingState.INACTION
                    binding.carImage.clearAnimation()
                    binding.historyButton.visibility = View.VISIBLE
                    binding.payButton.isEnabled = true
                }
                FuelingState.FULL_TANK -> {
                }
            }
        }
        binding.payButton.setOnClickListener {}
        binding.historyButton.setOnClickListener {
            startActivity(
                HistoryActivity.createIntent(
                    requireContext(), HistoryActivityViewModel(
                        items = listOf(
                            RefuelHistoryItemModel("a", "b", "c", "d"),
                            RefuelHistoryItemModel("a", "b", "c", "d"),
                            RefuelHistoryItemModel("a", "b", "c", "d")
                        ),
                        type = HistoryActivity.TYPE_REFUEL
                    )
                )
            )
        }
    }

    private fun refuelAnimation() {
        if (state == FuelingState.FUELING) {
            binding.carImage.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.scale_up).apply {
                setAnimationListener(AnimationListener(onAnimationEnd = {
                    binding.carImage.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.scale_down).apply {
                        setAnimationListener(AnimationListener(onAnimationEnd = {
                            if (state == FuelingState.FUELING) {
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
        binding.distributorView.refuelRecycler.apply {
            layoutManager = GridLayoutManager(requireContext(), 4, GridLayoutManager.VERTICAL, false)
            itemList?.let { adapter = RefuelRecyclerAdapter(it).apply { setHasStableIds(true) } }
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    super.getItemOffsets(outRect, view, parent, state)
                    val marginSmall = resources.getDimension(R.dimen.margin_small).roundToInt()
                    val marginMedium = resources.getDimension(R.dimen.margin_medium).roundToInt()
                    outRect.set(marginMedium, marginSmall, marginMedium, marginSmall)
                }
            })
        }
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    companion object {
        fun createInstance() = RefuelingFragment()
    }
}
