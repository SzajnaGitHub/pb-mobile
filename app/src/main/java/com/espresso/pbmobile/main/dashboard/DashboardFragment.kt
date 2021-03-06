package com.espresso.pbmobile.main.dashboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.espresso.data.RetrofitClient
import com.espresso.data.models.profile.ProfileRepository
import com.espresso.data.models.profile.UserProfile
import com.espresso.data.store.Store
import com.espresso.pbmobile.R
import com.espresso.pbmobile.admin.AdminConsoleActivity
import com.espresso.pbmobile.admin.state.WarningPopupActivity
import com.espresso.pbmobile.auth.AuthActivity
import com.espresso.pbmobile.databinding.FragmentDashboardBinding
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding
    private lateinit var store: Store
    private lateinit var delegate: Delegate
    private val service = RetrofitClient.getInstance()
    private val disposables = CompositeDisposable()
    private var canOpenWarning = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        delegate = context as Delegate
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        store = Store(requireContext())
        setupProfile()
        setupBinding()
        setupNextAvailableTerm()
        canOpenWarning = store.userType == UserProfile.TYPE_OWNER
        if (canOpenWarning) getStationState()
        return binding.root
    }

    private fun setupProfile() {
        ProfileRepository.profile(store.userId, fromService = true)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { binding.loading = true }
            .subscribe {
                binding.model = it
                binding.loading = false
            }
            .let(disposables::add)
    }

    private fun setupNextAvailableTerm() {
        service.getNearestReservationTerm()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { binding.loading = true }
            .subscribe { model ->
                binding.date = model.date
            }
            .let(disposables::add)
    }

    private fun setupBinding() {
        binding.refuelButton.clicks()
            .subscribe { delegate.openRefuelingFragment() }
            .let(disposables::add)

        binding.loginView.loginButton.clicks()
            .subscribe { startActivity(AuthActivity.createIntent(requireContext())) }
            .let(disposables::add)

        binding.pointsView.exchangeButton.clicks()
            .subscribe { delegate.openPointsFragment() }
            .let(disposables::add)

        binding.carWashButton.clicks()
            .subscribe { delegate.openCarWashFragment() }
            .let(disposables::add)

        binding.adminButton.clicks()
            .subscribe { startActivity(AdminConsoleActivity.createIntent(requireContext())) }
            .let(disposables::add)
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    private fun getStationState() {
        service.getStationState()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { items ->
                items.forEach {
                    if (it.productQuantityPercent <= 10.0 && canOpenWarning) {
                            canOpenWarning = false
                            startActivity(WarningPopupActivity.createIntent(requireContext(), it))
                        }
                }
            }
            .let(disposables::add)
    }

    interface Delegate {
        fun openRefuelingFragment()
        fun openPointsFragment()
        fun openCarWashFragment()
    }

    companion object {
        fun createInstance() = DashboardFragment()
    }
}
