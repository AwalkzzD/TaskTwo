package com.example.tasktwo.data.models

import android.graphics.drawable.BitmapDrawable
import androidx.databinding.ObservableField

data class VideoModel(
    val videoFilePath: String,
    val uploadStatus: ObservableField<String> = ObservableField<String>(),
    val uploadProgress: ObservableField<Int> = ObservableField<Int>(),
    val videoThumbnail: BitmapDrawable
)
