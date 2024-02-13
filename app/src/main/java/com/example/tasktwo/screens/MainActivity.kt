package com.example.tasktwo.screens

import android.media.MediaMetadataRetriever
import android.os.Build
import android.os.Bundle
import android.os.ext.SdkExtensions
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasktwo.R
import com.example.tasktwo.data.models.VideoModel
import com.example.tasktwo.databinding.ActivityMainBinding
import com.example.tasktwo.screens.adapter.GenericDataAdapter
import java.io.File
import java.io.FileOutputStream
import kotlin.math.min

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var genericDataAdapter: GenericDataAdapter<VideoModel>
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
                                file.name.toString(),
                                "Uploading...",
                                ObservableField(0),
                                mmdR.frameAtTime!!
                            )
                        )
                    }
                    genericDataAdapter.addData(videoList)
                    genericDataAdapter.notifyDataSetChanged()
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
        genericDataAdapter = GenericDataAdapter(this, R.layout.video_list_item) { item, itemView ->
            val fileName: TextView = itemView.findViewById(R.id.fileName)
            val videoThumbnail: ImageView = itemView.findViewById(R.id.videoThumbnail)
            val uploadProgress: ProgressBar =
                itemView.findViewById(R.id.uploadProgress)
            val uploadStatus: TextView = itemView.findViewById(R.id.uploadStatus)

            with(item) {
                fileName.text = this.videoFilePath
                uploadStatus.text = this.uploadStatus
                videoThumbnail.setImageBitmap(this.videoThumbnail)
                var i = this.uploadProgress.get()
                Log.d(TAG, "initRecyclerView: $i")
                Thread(Runnable {
                    while (i!! < 100) {
                        i += 1
                        this.uploadProgress.set(i)
                        Log.d(TAG, "initRecyclerView: after: " + this.uploadProgress.get())
                        /*Handler(Looper.getMainLooper()).post(Runnable {
                            uploadProgress.progress = i
                        })*/
                        try {
                            Thread.sleep(1000)
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }
                    }
                }).start()
            }
        }
        binding.videoUploadRecyclerView.adapter = genericDataAdapter
    }
}