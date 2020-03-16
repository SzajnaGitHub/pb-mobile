package com.espresso.pbmobile.main.refueling

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.espresso.pbmobile.AnimationListener
import com.espresso.pbmobile.R
import com.espresso.pbmobile.databinding.FragmentRefuelingBinding
import kotlin.math.roundToInt
import kotlin.random.Random

class RefuelingFragment : Fragment() {

    private lateinit var binding: FragmentRefuelingBinding
    private var itemList: List<RefuelItemModel>? = null
    private var x = 90
    private var pressed = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRefuelingBinding.inflate(inflater, container, false)
        setupRecycler()
        setupBindings()
        return binding.root
    }

    private fun setupRecycler() {
        itemList = listOf(
            RefuelItemModel(false, "95", "item_refuel_95", ::handleItemClick),
            RefuelItemModel(false, "98", "item_refuel_98", ::handleItemClick),
            RefuelItemModel(false, "ON", "item_refuel_ON", ::handleItemClick),
            RefuelItemModel(false, "LPG", "item_refuel_LPG", ::handleItemClick)
        )

        binding.refuelRecycler.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            itemList?.let { adapter = RefuelRecyclerAdapter(it) }
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

    private fun handleItemClick(model: RefuelItemModel) {
        itemList?.map {
            it.copy(isClicked = it.id == model.id)
        }?.let {
            binding.refuelRecycler.adapter = RefuelRecyclerAdapter(it)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupBindings() {
        binding.refuelPercentage.text = "$x%"
        binding.refuelButton.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    pressed = true
                    if (x < 100) refuelAnimation()
                    true
                }
                MotionEvent.ACTION_UP -> {
                    pressed = false
                    binding.refuelBall.clearAnimation()
                    binding.carImage.clearAnimation()
                    true
                }
                else -> false
            }
        }
    }

    private fun refuelAnimation() {
        val ballView = binding.refuelBall
        val destinationView = binding.carImage
        ballView.startAnimation(
            TranslateAnimation(
                0f,
                destinationView.x - ballView.x,
                0f,
                destinationView.measuredHeight / 2F
            ).apply {
                duration = Random.nextLong(250, 300)
                setAnimationListener(AnimationListener(onAnimationEnd = {
                    if (pressed && x < 100) {
                        destinationView.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.scale_up).apply {
                            x = (x + Random.nextInt(0, 3)).coerceAtMost(100)
                            binding.refuelPercentage.text = "$x%"
                            setAnimationListener(AnimationListener(onAnimationEnd = {
                                destinationView.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.scale_down).apply {
                                    setAnimationListener(AnimationListener(onAnimationEnd = {
                                        if (pressed && x < 100) refuelAnimation()
                                    }))
                                })
                            }))
                        })
                    }
                }))
            })
    }

    companion object {
        fun createInstance() = RefuelingFragment()
    }
}
