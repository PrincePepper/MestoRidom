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
        return listOf(PlaceCategory("Выход", 0xFF85C2CC.toInt(), ResourcesCompat.getDrawable(resources, R.drawable.ic_log_out, null)!!),
                PlaceCategory("Еда", 0xFFFFF6E8.toInt(), ResourcesCompat.getDrawable(resources, R.drawable.ic_coffee, null)!!),
                PlaceCategory("Туалет", 0xFFF1FFF0.toInt(), ResourcesCompat.getDrawable(resources, R.drawable.ic_group_15, null)!!),
                PlaceCategory("Лифт", 0xFFD7FAFF.toInt(), ResourcesCompat.getDrawable(resources, R.drawable.ic_select_o, null)!!),
                PlaceCategory("Выход", 0xFF85C2CC.toInt(), ResourcesCompat.getDrawable(resources, R.drawable.ic_log_out, null)!!),
                PlaceCategory("Еда", 0xFFFFF6E8.toInt(), ResourcesCompat.getDrawable(resources, R.drawable.ic_coffee, null)!!),
                PlaceCategory("Туалет", 0xFFF1FFF0.toInt(), ResourcesCompat.getDrawable(resources, R.drawable.ic_group_15, null)!!),
                PlaceCategory("Лифт", 0xFFD7FAFF.toInt(), ResourcesCompat.getDrawable(resources, R.drawable.ic_select_o, null)!!),
                PlaceCategory("Выход", 0xFF85C2CC.toInt(), ResourcesCompat.getDrawable(resources, R.drawable.ic_log_out, null)!!),
                PlaceCategory("Еда", 0xFFFFF6E8.toInt(), ResourcesCompat.getDrawable(resources, R.drawable.ic_coffee, null)!!),
                PlaceCategory("Туалет", 0xFFF1FFF0.toInt(), ResourcesCompat.getDrawable(resources, R.drawable.ic_group_15, null)!!),
                PlaceCategory("Лифт", 0xFFD7FAFF.toInt(), ResourcesCompat.getDrawable(resources, R.drawable.ic_select_o, null)!!))
    }
}