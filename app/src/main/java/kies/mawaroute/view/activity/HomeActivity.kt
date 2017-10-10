package kies.mawaroute.view.activity

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ObservableList
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.ViewGroup
import kies.mawaroute.R
import kies.mawaroute.databinding.ActivityHomeBinding
import kies.mawaroute.databinding.ItemShopBinding
import kies.mawaroute.view.customview.BindingHolder
import kies.mawaroute.view.customview.ObservableListRecyclerAdapter
import kies.mawaroute.viewmodel.HomeViewModel
import kies.mawaroute.viewmodel.ShopItemViewModel

class HomeActivity : AppCompatActivity() {

    private val binding: ActivityHomeBinding by lazy {
        DataBindingUtil.setContentView<ActivityHomeBinding>(this, R.layout.activity_home)
    }

    private val viewModel: HomeViewModel by lazy {
        ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewModel = viewModel
        lifecycle.addObserver(viewModel)

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

