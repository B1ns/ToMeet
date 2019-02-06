package com.example.cardview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tomeet.R
import com.example.tomeet.data.CardViewItem

import java.util.ArrayList

class test : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val cardViewItems = ArrayList<CardViewItem>()

    init {
        cardViewItems.add(CardViewItem(R.drawable.three, "정빈", "나이 18세"))
        cardViewItems.add(CardViewItem(R.drawable.two, "이건호", "나이 19세"))
        cardViewItems.add(CardViewItem(R.drawable.one, "배현빈", "나이 21세"))
    }



    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {

        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.cardview_item, viewGroup, false)
        return RowCell(view)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        (viewHolder as RowCell).imageView.setImageResource(cardViewItems[position].imageView)
        viewHolder.title.text = cardViewItems[position].title
        viewHolder.subtitle.text = cardViewItems[position].subtitle
    }

    override fun getItemCount(): Int {
        return cardViewItems.size
    }

    private inner class RowCell(view: View) : RecyclerView.ViewHolder(view) {

        var imageView: ImageView = view.findViewById(R.id.cardView_ImageView)
        var title: TextView = view.findViewById(R.id.cardView_title)
        var subtitle: TextView = view.findViewById(R.id.cardView_subtitle)

    }
}
