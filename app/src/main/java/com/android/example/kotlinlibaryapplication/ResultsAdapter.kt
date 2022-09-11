package com.android.example.kotlinlibaryapplication

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Space
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class ResultsAdapter(private var context: Context?, cardsModelArrayList: ArrayList<ResultsModel>?) :
    RecyclerView.Adapter<ResultsAdapter.ViewHolder>() {

    private var resultsModelArrayList: ArrayList<ResultsModel>? = cardsModelArrayList
    private var card: CardView? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultsAdapter.ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.results_layout, parent, false)
        card = view.findViewById(R.id.results_card)
        return ResultsAdapter.ViewHolder(view)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cdnName: TextView
        val cdnSpeed: TextView
        val price: TextView
        val weight: TextView
        val cdnId: TextView
        val cdnError: TextView
        var space: Space
//        val cdnUrl: TextView

        init {
            cdnName = itemView.findViewById(R.id.cdnName)
            cdnSpeed = itemView.findViewById(R.id.cdnSpeed)
            price = itemView.findViewById(R.id.price)
            weight = itemView.findViewById(R.id.weight)
            cdnId = itemView.findViewById(R.id.cdnId)
            cdnError = itemView.findViewById(R.id.cdnError)
            space = itemView.findViewById(R.id.spaceError)
//            cdnUrl = itemView.findViewById(R.id.cdnUrl)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: ResultsModel = resultsModelArrayList!![position]
        holder.cdnName.text = context!!.getString(R.string.cdn_name, model.cdnName)
        holder.cdnSpeed.text = context!!.getString(R.string.cdn_speed, model.cdnSpeed.toString())
        holder.price.text = context!!.getString(R.string.cdn_price, model.cdnPrice.toString())
        holder.weight.text = context!!.getString(R.string.cdn_weight, model.cdnWeight.toString())
        holder.cdnId.text = context!!.getString(R.string.cdn_ID, model.cdnId.toString())
        if(!model.error.isNullOrEmpty()) {
            holder.cdnError.text = context!!.getString(R.string.cdn_Error, model.error.toString())
            card!!.background.setColorFilter(Color.parseColor("#E32636"), PorterDuff.Mode.SRC_ATOP)
            holder.cdnName.setTextColor(Color.parseColor("#ffffff"))
            holder.cdnSpeed.setTextColor(Color.parseColor("#ffffff"))
            holder.price.setTextColor(Color.parseColor("#ffffff"))
            holder.weight.setTextColor(Color.parseColor("#ffffff"))
            holder.cdnId.setTextColor(Color.parseColor("#ffffff"))
            holder.cdnError.setTextColor(Color.parseColor("#ffffff"))
        } else{
            holder.cdnError.visibility = View.GONE
            holder.space.visibility = View.GONE
        }
//        holder.cdnUrl.text = context!!.getString(R.string.cdn_Url, model.cdnUrl)
    }

    override fun getItemCount(): Int {
        return resultsModelArrayList!!.size
    }
}