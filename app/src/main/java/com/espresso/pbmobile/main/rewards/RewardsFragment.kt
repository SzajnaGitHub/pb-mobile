package com.espresso.pbmobile.main.rewards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.espresso.data.RetrofitClient
import com.espresso.data.models.profile.ProfileRepository
import com.espresso.data.store.Store
import com.espresso.pbmobile.R
import com.espresso.pbmobile.databinding.FragmentRewardsBinding
import com.espresso.pbmobile.databinding.ItemTabRewardBinding
import com.espresso.pbmobile.utlis.RecyclerMarginDecorator
import com.google.android.material.tabs.TabLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RewardsFragment : Fragment() {
    private lateinit var binding: FragmentRewardsBinding
    private lateinit var store: Store
    private var points = 0
    private val service = RetrofitClient.getInstance()
    private val disposables = CompositeDisposable()
    private val list = arrayListOf<ItemTabRewardBinding>()
    private val rewardsPointsList = listOf(
        RewardsItemModel(1, "Benzyna/on", 100, ::handleRewardsClick),
        RewardsItemModel(2, "lpg", 50, ::handleRewardsClick),
        RewardsItemModel(3, "Myjnia", 300, ::handleRewardsClick),
        RewardsItemModel(4, "Myjnia z woskiem", 400, ::handleRewardsClick)
    )
    private val itemPointsList = listOf(
        RewardsItemModel(1, "Benzyna/on", 2),
        RewardsItemModel(2, "lpg", 1),
        RewardsItemModel(3, "Myjnia", 5),
        RewardsItemModel(4, "Myjnia z woskiem", 10)
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rewards, container, false)
        store = Store(requireContext())
        setupProfile()
        setupRecycler()
        setupTabLayout()
        return binding.root
    }

    private fun setupProfile(fromService: Boolean = false) {
        ProfileRepository.profile(store.userId, fromService)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                points = it.points
                binding.points = points
            }
            .let(disposables::add)
    }

    private fun setupRecycler() {
        binding.items = rewardsPointsList
        binding.rewardsRecycler.addItemDecoration(RecyclerMarginDecorator(requireContext()))
    }

    private fun handleRewardsClick(model: RewardsItemModel) {
        if (points > model.points) {
            getReward(model.id)
        } else {
            Toast.makeText(requireContext(), "Masz za mało puntków", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getReward(productId: Long) {
        service.getReward(store.userId, productId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe{binding.loading = true}
            .doAfterTerminate { binding.loading = false }
            .subscribe {
                Toast.makeText(requireContext(), "Nagroda dodana pomyślnie", Toast.LENGTH_SHORT).show()
                setupProfile(fromService = true)
            }
            .let(disposables::add)
    }

    private fun setupTabLayout() {
        val modelList = listOf(
            RewardsTabModel(title = "nagrody", isClicked = true),
            RewardsTabModel(title = "przyznania")
        )

        (0 until binding.tabLayout.tabCount)
            .mapNotNull(binding.tabLayout::getTabAt)
            .forEachIndexed { index, tab ->
                val tabBinding = ItemTabRewardBinding.inflate(LayoutInflater.from(requireContext()))
                tabBinding.model = modelList[index]
                list.add(tabBinding)
                tab.customView = tabBinding.root
            }


        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {}
            override fun onTabUnselected(p0: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.position?.let { handleOnTabClick(it) }

            }

        })
    }


    private fun handleOnTabClick(position: Int) {
        when (position) {
            0 -> binding.items = rewardsPointsList
            1 -> binding.items = itemPointsList
        }
        list.forEachIndexed { index, binding ->
            binding.model = binding.model?.copy(isClicked = index == position)
        }
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    companion object {
        fun createInstance() = RewardsFragment()
    }
}
