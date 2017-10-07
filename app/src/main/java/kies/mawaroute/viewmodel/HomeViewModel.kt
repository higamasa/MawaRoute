package kies.mawaroute.viewmodel

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableArrayList
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kies.mawaroute.api.GnaviClient
import kies.mawaroute.api.GnaviQuery
import kies.mawaroute.model.Shop

class HomeViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    var shopViewModels = ObservableArrayList<ShopItemViewModel>()

    override fun onCleared() {
        compositeDisposable.clear()
    }

    fun start() {
        val query = GnaviQuery().name("らーめん").hitPerPage(100).build()
        compositeDisposable.add(GnaviClient.service.restaurantSearch(query)
                .subscribeOn(Schedulers.io())
                .map { resp -> convertToViewModel(resp.shops) }
                .subscribeBy(
                        onSuccess = this::updateViewModels,
                        onError = {}
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
