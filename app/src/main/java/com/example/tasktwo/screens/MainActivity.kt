package com.example.tasktwo.screens

import android.graphics.drawable.BitmapDrawable
import android.media.MediaMetadataRetriever
import android.os.Build
import android.os.Bundle
import android.os.ext.SdkExtensions
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasktwo.data.models.VideoModel
import com.example.tasktwo.databinding.ActivityMainBinding
import com.example.tasktwo.screens.adapter.VideoDataAdapter
import java.io.File
import java.io.FileOutputStream
import kotlin.math.min

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var videoDataAdapter: VideoDataAdapter
    private lateinit var file: File
    private val TAG = "MainActivity"


    private val galleryLauncher =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && SdkExtensions.getExtensionVersion(
                Build.VERSION_CODES.R
            ) >= 2
        ) {
            this.registerForActivityResult(
                ActivityResultContracts.PickMultipleVisualMedia(
                    min(
                        MediaStore.getPickImagesMaxLimit(), 10
                    )
                )
            ) {
                try {
                    val videoList: MutableList<VideoModel> = mutableListOf()
                    val mmdR = MediaMetadataRetriever()
                    for (uri in it) {
                        file = File(applicationContext.filesDir, "video.mp4")
                        contentResolver.openInputStream(uri)?.copyTo(FileOutputStream(file))
                        mmdR.setDataSource(file.path)
                        Log.d("TAG", "uri: ${file.path}")
                        videoList.add(
                            VideoModel(
                                file.name,
                                ObservableField("UPLOADING..."),
                                ObservableField(0),
                                BitmapDrawable(resources, mmdR.frameAtTime!!)
                            )
                        )
                    }
                    videoDataAdapter.addData(videoList)
                    videoDataAdapter.notifyDataSetChanged()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            TODO("SdkExtensions.getExtensionVersion(R) < 2")
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        binding.videoUploadRecyclerView.layoutManager = LinearLayoutManager(this)

        binding.uploadFiles.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && SdkExtensions.getExtensionVersion(
                    Build.VERSION_CODES.R
                ) >= 2
            ) {
                galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly))
            } else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun initRecyclerView() {
        videoDataAdapter = VideoDataAdapter()
        binding.videoUploadRecyclerView.adapter = videoDataAdapter
    }
}