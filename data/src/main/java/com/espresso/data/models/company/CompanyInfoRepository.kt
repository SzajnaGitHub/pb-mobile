package com.espresso.data.models.company

import com.espresso.data.RetrofitClient
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

object CompanyInfoRepository {
    private val subject = BehaviorSubject.create<InfoModel>()
    private val service = RetrofitClient.getInstance()

    fun info(fromService: Boolean = false): Single<InfoModel> = if (subject.hasValue() && !fromService) Single.just(subject.value)
    else service.getCompanyInfo()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSuccess(subject::onNext)
        .flatMap { Single.just(it) }
}
