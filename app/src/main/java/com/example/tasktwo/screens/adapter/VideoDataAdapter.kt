package com.example.tasktwo.screens.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tasktwo.data.models.VideoModel
import com.example.tasktwo.databinding.VideoListItemBinding

class VideoDataAdapter : RecyclerView.Adapter<VideoDataAdapter.ViewHolder>() {
    private var dataList: MutableList<VideoModel> = emptyList<VideoModel>().toMutableList()
    private lateinit var binding: VideoListItemBinding

    class ViewHolder(private val binding: VideoListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(video: VideoModel) {
            binding.item = video
            var i = video.uploadProgress.get()
            Thread(Runnable {
                while (i!! < 100) {
                    i += 5
                    video.uploadProgress.set(i)

                    try {
                        Thread.sleep(1000)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                        video.uploadStatus.set("FAILED")
                        binding.uploadStatus.setTextColor(Color.RED)
                    }
                }
                video.uploadStatus.set("UPLOADED")
                binding.uploadStatus.setTextColor(Color.GREEN)
            }).start()
        }
    }

    fun addData(data: List<VideoModel>) {
        this.dataList.addAll(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = VideoListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val video = dataList[position]
        holder.bind(video)


    }
}