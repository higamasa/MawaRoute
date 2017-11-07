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
import kies.mawaroute.model.Restaurant


class HomeViewModel : ViewModel(), LifecycleObserver {

    private val compositeDisposable = CompositeDisposable()

    var restaurantViewModels = ObservableArrayList<RestaurantItemViewModel>()

    val errorVisibility: BehaviorSubject<Int> = BehaviorSubject.createDefault<Int>(View.GONE)
    val restaurantListVisibility: BehaviorSubject<Int> = BehaviorSubject.createDefault<Int>(View.VISIBLE)

    val isGpsPermissionValid: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)

    override fun onCleared() {
        errorVisibility.onComplete()
        isGpsPermissionValid.onComplete()
        restaurantListVisibility.onComplete()
        compositeDisposable.clear()
    }

    fun start(location: Location) {
        isGpsPermissionValid.subscribe { value ->
            restaurantListVisibility.onNext(if (value) View.VISIBLE else View.GONE)
            errorVisibility.onNext(if (value) View.GONE else View.VISIBLE)
            if (value) {
                val query = GnaviQuery()
                        .location(location.latitude, location.longitude)
                        .hitPerPage(100)
                        .build()
                compositeDisposable.add(getNearbyRestaurant(query))
            }
        }
    }

    private fun getNearbyRestaurant(query: Map<String, String>): Disposable {
        return GnaviClient.service.restaurantSearch(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { resp -> convertToViewModel(resp.restaurants) }
                .subscribeBy(
                        onSuccess = this::updateViewModels,
                        onError = {
                            restaurantListVisibility.onNext(View.GONE)
                            errorVisibility.onNext(View.VISIBLE)
                        }
                )
    }

    private fun convertToViewModel(restaurants: List<Restaurant>): List<RestaurantItemViewModel> {
        return restaurants.map { restaurant -> RestaurantItemViewModel(restaurant) }
    }

    private fun updateViewModels(RestaurantViewModels: List<RestaurantItemViewModel>) {
        this.restaurantViewModels.clear()
        this.restaurantViewModels.addAll(RestaurantViewModels)
    }
}
