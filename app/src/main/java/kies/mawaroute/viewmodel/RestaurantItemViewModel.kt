package kies.mawaroute.viewmodel

import android.databinding.BaseObservable
import kies.mawaroute.model.Restaurant

class RestaurantItemViewModel(val restaurant: Restaurant, val distance : Int) : BaseObservable()
