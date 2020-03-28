package com.espresso.data.models.profile

import com.espresso.data.RetrofitClientInstance
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

object ProfileRepository {
    private val subject = BehaviorSubject.create<UserProfile>()
    private val service = RetrofitClientInstance.getInstance()

    fun profile(id: Long): Observable<UserProfile> = if (subject.hasValue()) subject
    else service.profile(id)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSuccess(subject::onNext)
        .flatMapObservable { subject }
}
