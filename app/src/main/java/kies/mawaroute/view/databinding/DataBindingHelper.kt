package kies.mawaroute.view.databinding

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.squareup.picasso.Picasso

object DataBindingHelper {

    @JvmStatic
    @BindingAdapter("imageSrc")
    fun imageSrc(imageView: ImageView, imageUrl: String) {
        imageView.setImageResource(android.R.color.transparent)
        if (imageUrl.isNotEmpty()) {
            Picasso.with(imageView.context).load(imageUrl).into(imageView)
        }
    }
}
