package com.example.tasktwo.screens.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.example.tasktwo.data.models.VideoModel
import com.example.tasktwo.databinding.VideoListItemBinding

class GenericDataAdapter<T : Any>(
    private val context: Activity,
    @LayoutRes val layoutID: Int,
    private val bindFunction: (item: T, itemView: View) -> Unit
) : RecyclerView.Adapter<GenericDataAdapter.ViewHolder>() {

    private var dataList: MutableList<T> = emptyList<T>().toMutableList()
    private lateinit var binding: VideoListItemBinding

    fun addData(data: List<T>) {
        this.dataList.addAll(data)
    }

    fun getData(): List<T> {
        return dataList.toList()
    }

    class ViewHolder(private val binding: VideoListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(video: VideoModel) {
            binding.item = video
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {/*val itemView = LayoutInflater.from(context).inflate(layoutID, parent, false)
        return ViewHolder(itemView)*/

        binding = VideoListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]

        /*bindFunction(item, binding.root)*/
        Log.d("TAG", "onBindViewHolder: Called")
    }
}