package layout

import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class HtmlImage (
    activity: AppCompatActivity,
    view: ImageView,
    imageURL: String?) {

    init {
        Glide.with(activity).load(imageURL).into(view)
    }
}