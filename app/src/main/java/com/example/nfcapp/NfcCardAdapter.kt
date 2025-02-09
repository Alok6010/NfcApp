package com.example.nfcapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NfcCardAdapter(private val nfcCards: List<NfcCard>) :
    RecyclerView.Adapter<NfcCardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = nfcCards[position]
        holder.text1.text = "Card ID: ${card.id}"
        holder.text2.text = "Location: ${card.location}, Time: ${card.timestamp}"
    }

    override fun getItemCount(): Int = nfcCards.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text1: TextView = itemView.findViewById(android.R.id.text1)
        val text2: TextView = itemView.findViewById(android.R.id.text2)
    }
}