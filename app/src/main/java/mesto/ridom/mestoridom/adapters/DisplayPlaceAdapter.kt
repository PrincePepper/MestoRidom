package mesto.ridom.mestoridom.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.place_item_layout.view.*
import mesto.ridom.mestoridom.R

class DisplayPlaceAdapter() : RecyclerView.Adapter<DisplayPlaceAdapter.PlaceViewHolder>() {

    lateinit var data: List<Place>

    data class Place(val name: String, val position: Int)

    class PlaceViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun fill(place: Place) {
            view.apply {
                place_name.text = place.name
                distance_to_place.text = place.position.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder =
            PlaceViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.place_item_layout, parent, false))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) = holder.fill(data[position])
}