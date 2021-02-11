package mesto.ridom.mestoridom.viewmodel

import android.content.res.Resources
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mesto.ridom.mestoridom.R
import mesto.ridom.mestoridom.adapters.PlaceCategory

class PlaceCategoryViewModel() : ViewModel() {

    lateinit var resources: Resources

    private val placeCategories: MutableLiveData<List<PlaceCategory>> by lazy {
        MutableLiveData<List<PlaceCategory>>().apply {
            value = loadPlaceCategories()
        }
    }

    fun getPaceCategories(): LiveData<List<PlaceCategory>> = placeCategories

    private fun loadPlaceCategories(): List<PlaceCategory> {
        return listOf(PlaceCategory("Выход", 0xFF53D9EC.toInt(), ResourcesCompat.getDrawable(resources, R.drawable.ic_log_out, null)!!),
                PlaceCategory("Лифт", 0xFFC03D3D.toInt(), ResourcesCompat.getDrawable(resources, R.drawable.ic_lift, null)!!),
                PlaceCategory("Туалет", 0xFFAA78C2.toInt(), ResourcesCompat.getDrawable(resources, R.drawable.ic_bathroom, null)!!),
                PlaceCategory("Еда", 0xFFFFB13B.toInt(), ResourcesCompat.getDrawable(resources, R.drawable.ic_caffe, null)!!),
                PlaceCategory("ATM", 0xFF0FAA15.toInt(), ResourcesCompat.getDrawable(resources, R.drawable.ic_atm, null)!!),
                PlaceCategory("Покупки", 0xFFCC1E66.toInt(), ResourcesCompat.getDrawable(resources, R.drawable.ic_shopping_bag, null)!!),
                PlaceCategory("Досуг", 0xFFA880FF.toInt(), ResourcesCompat.getDrawable(resources, R.drawable.ic_gift, null)!!),
                PlaceCategory("Услуги", 0xFFFF7A00.toInt(), ResourcesCompat.getDrawable(resources, R.drawable.ic_servises, null)!!),
                PlaceCategory("Офис", 0xFF85C2CC.toInt(), ResourcesCompat.getDrawable(resources, R.drawable.ic_office, null)!!),
                PlaceCategory("Другое", 0xFFD8DDEA.toInt(), ResourcesCompat.getDrawable(resources, R.drawable.ic_another, null)!!)
        )
    }
}