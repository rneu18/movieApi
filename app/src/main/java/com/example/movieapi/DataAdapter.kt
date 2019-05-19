package com.example.movieapi

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.movie_list.view.*


class DataAdapter(private val dataList: ArrayList<String>, private val listener: MainActivity) : RecyclerView.Adapter<DataAdapter.ViewHolder>() {

    interface Listener {

        fun onItemClick(position: Int)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(dataList[position], listener, position)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_list, parent, false)

        return ViewHolder(view)
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        fun bind(myData: String, listener: MainActivity, position: Int) {

          try {
              itemView.my_movies_list.text = myData

              itemView.setOnClickListener{ listener.onItemClick(position) }
          }catch (e:Exception){

          }

        }
    }
}

