package com.alex.newstime.bus

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

object RxBus {

    private var publishSubject = BehaviorSubject.create<Any>()

    // ----------------------------------------------------------------------------

    fun publish(value: Any) { publishSubject.onNext(value) }

    // ----------------------------------------------------------------------------

    fun <T> listen(type: Class<T>): Observable<T> = publishSubject.ofType(type)
}