package kies.mawaroute.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ObservableList
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.ViewGroup
import kies.mawaroute.R
import kies.mawaroute.databinding.ActivityHomeBinding
import kies.mawaroute.databinding.ItemShopBinding
import kies.mawaroute.util.GpsUtil
import kies.mawaroute.view.customview.BindingHolder
import kies.mawaroute.view.customview.ObservableListRecyclerAdapter
import kies.mawaroute.viewmodel.HomeViewModel
import kies.mawaroute.viewmodel.ShopItemViewModel
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions

@RuntimePermissions
class HomeActivity : AppCompatActivity() {

    private val binding: ActivityHomeBinding by lazy {
        DataBindingUtil.setContentView<ActivityHomeBinding>(this, R.layout.activity_home)
    }

    private val viewModel: HomeViewModel by lazy {
        ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }

    private val gpsUtil by lazy { GpsUtil(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewModel = viewModel

        // Permissionチェック
        getLocationWithPermissionCheck()

        viewModel.shopListVisibility.subscribe { value -> binding.shopList.visibility = value }
        viewModel.errorVisibility.subscribe { value -> binding.errorView.visibility = value }

        setSupportActionBar(binding.toolbar)

        val divider = DividerItemDecoration(this, 1)
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider))

        binding.shopList.apply {
            adapter = ShopListAdapter(context, viewModel.shopViewModels)
            setHasFixedSize(true)
            addItemDecoration(divider)
            layoutManager = LinearLayoutManager(context)
        }
        // 位置情報の受取
        gpsUtil.myCurrentLocation.subscribe(
                viewModel::start,
                { e -> Log.e("HomeActivity", "Location:", e); }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        gpsUtil.clean()
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @NeedsPermission(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    fun getLocation() {
        viewModel.isGpsPermissionValid.onNext(true)
        gpsUtil.accept()
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    @OnNeverAskAgain(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    fun showDeniedForLocation() {
        Snackbar.make(binding.root, "位置情報が取得できませんでした", Snackbar.LENGTH_LONG).show()
        gpsUtil.reject()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GpsUtil.LOCATION_CODE && resultCode == Activity.RESULT_OK) {
            gpsUtil.accept()
        } else {
            gpsUtil.reject()
        }
    }

    inner class ShopListAdapter(context: Context, list: ObservableList<ShopItemViewModel>) :
            ObservableListRecyclerAdapter<ShopItemViewModel, BindingHolder<ItemShopBinding>>(context, list) {

        override fun onBindViewHolder(holder: BindingHolder<ItemShopBinding>, position: Int) {
            holder.binding.apply {
                viewModel = getItem(position)
                executePendingBindings()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<ItemShopBinding> {
            return BindingHolder(context, parent, R.layout.item_shop)
        }
    }
}
