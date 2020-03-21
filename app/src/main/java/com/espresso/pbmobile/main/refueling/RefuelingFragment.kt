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
import com.espresso.pbmobile.AnimationListener
import com.espresso.pbmobile.R
import com.espresso.pbmobile.databinding.FragmentRefuelingBinding
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

    private enum class FuelingState { INACTION, FUELING, FULL_TANK }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRefuelingBinding.inflate(inflater, container, false)
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

    private fun setupRecycler() {
        itemList = listOf(
            RefuelItemModel(false, "95", "item_refuel_95", 4.66, ::handleItemClick),
            RefuelItemModel(false, "98", "item_refuel_98", 4.91, ::handleItemClick),
            RefuelItemModel(false, "ON", "item_refuel_ON", 4.77, ::handleItemClick),
            RefuelItemModel(false, "LPG", "item_refuel_LPG", 2.01, ::handleItemClick)
        )

        binding.refuelRecycler.apply {
            layoutManager = GridLayoutManager(requireContext(), 4, GridLayoutManager.VERTICAL, false)
            itemList?.let { adapter = RefuelRecyclerAdapter(it) }
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    super.getItemOffsets(outRect, view, parent, state)
                    val marginSmall = resources.getDimension(R.dimen.margin_small).roundToInt()
                    outRect.set(marginSmall, marginSmall, marginSmall, marginSmall)
                }
            })
        }
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

        itemList?.map {
            it.copy(isClicked = it.id == model.id)
        }?.let {
            binding.refuelRecycler.adapter = RefuelRecyclerAdapter(it)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupBindings() {
        binding.refuelButton.setOnClickListener {
            when (state) {
                FuelingState.INACTION -> {
                    binding.refuelButton.text = "Stop"
                    state = FuelingState.FUELING
                    fuelPercentageSubject.value?.let { if (it < REFUEL_MAX_VALUE) refuelAnimation() }
                }
                FuelingState.FUELING -> {
                    binding.refuelButton.text = "Tankuj"
                    state = FuelingState.INACTION
                    binding.carImage.clearAnimation()
                }
                FuelingState.FULL_TANK -> {
                }
            }
        }
        binding.payButton.setOnClickListener {}
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

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    companion object {
        private const val REFUEL_MAX_VALUE = 100
        fun createInstance() = RefuelingFragment()
    }
}
