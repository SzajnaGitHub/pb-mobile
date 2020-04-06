package com.espresso.data.models.refuel

import com.espresso.data.RetrofitClient
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

object RefuelProductsRepo {
    private val service = RetrofitClient.getInstance()
    private val subject = BehaviorSubject.create<List<RefuelProduct>>()

    fun products(fromService: Boolean = false): Single<List<RefuelProduct>> = if (subject.hasValue() && !fromService) Single.just(subject.value) else
        service.getRefuelProducts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess(subject::onNext)
            .flatMap { Single.just(it) }
}
