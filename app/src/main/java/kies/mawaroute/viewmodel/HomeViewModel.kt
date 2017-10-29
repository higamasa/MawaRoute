package kies.mawaroute.viewmodel

import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableArrayList
import android.location.Location
import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
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

    val isGpsPermissionValid: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)

    override fun onCleared() {
        errorVisibility.onComplete()
        shopListVisibility.onComplete()
        isGpsPermissionValid.onComplete()
        compositeDisposable.clear()
    }

    fun start(location: Location) {
        isGpsPermissionValid.subscribe { value ->
            shopListVisibility.onNext(if (value) View.VISIBLE else View.GONE)
            errorVisibility.onNext(if (value) View.GONE else View.VISIBLE)
            if (value) {
                val query = GnaviQuery()
                        .location(location.latitude, location.longitude)
                        .hitPerPage(100)
                        .build()
                compositeDisposable.add(getNearbyShops(query))
            }
        }
    }

    private fun getNearbyShops(query: Map<String, String>): Disposable {
        return GnaviClient.service.restaurantSearch(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { resp -> convertToViewModel(resp.shops) }
                .subscribeBy(
                        onSuccess = this::updateViewModels,
                        onError = {
                            shopListVisibility.onNext(View.GONE)
                            errorVisibility.onNext(View.VISIBLE)
                        }
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
