package kies.mawaroute.viewmodel

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableArrayList
import android.view.View
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import kies.mawaroute.api.GnaviClient
import kies.mawaroute.api.GnaviQuery
import kies.mawaroute.model.Shop

class HomeViewModel : ViewModel(), LifecycleObserver {

    private val compositeDisposable = CompositeDisposable()

    var shopViewModels = ObservableArrayList<ShopItemViewModel>()

    val errorVisibility: BehaviorSubject<Int> = BehaviorSubject.createDefault<Int>(View.GONE)
    val shopListVisibility: BehaviorSubject<Int> = BehaviorSubject.createDefault<Int>(View.VISIBLE)

    override fun onCleared() {
        errorVisibility.onComplete()
        shopListVisibility.onComplete()
        compositeDisposable.clear()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun start() {
        val query = GnaviQuery().name("らーめん").hitPerPage(100).build()
        compositeDisposable.add(GnaviClient.service.restaurantSearch(query)
                .subscribeOn(Schedulers.io())
                .map { resp -> convertToViewModel(resp.shops) }
                .subscribeBy(
                        onSuccess = this::updateViewModels,
                        onError = {
                            shopListVisibility.onNext(View.GONE)
                            errorVisibility.onNext(View.VISIBLE)
                        }
                )
        )
    }

    private fun convertToViewModel(shops: List<Shop>): List<ShopItemViewModel> {
        return shops.map { shop -> ShopItemViewModel(shop) }
    }

    private fun updateViewModels(shopViewModels: List<ShopItemViewModel>) {
        this.shopViewModels.clear()
        this.shopViewModels.addAll(shopViewModels)
    }
}
