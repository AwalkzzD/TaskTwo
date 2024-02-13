package com.example.tasktwo.data.models

import android.graphics.Bitmap
import androidx.databinding.ObservableField

data class VideoModel(
    val videoFilePath: String,
    val uploadStatus: String,
    var uploadProgress: ObservableField<Int> = ObservableField<Int>(),
    val videoThumbnail: Bitmap

//     = ObservableField<Int>()
)
