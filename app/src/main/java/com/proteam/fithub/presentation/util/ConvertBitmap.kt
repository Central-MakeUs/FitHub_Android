package com.proteam.fithub.presentation.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.net.toUri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.io.File

object ConvertBitmap {
    suspend fun List<String>.ConvertWhenList(context : Context) : List<String> {
        val result = mutableListOf<String>()
        val hi = CoroutineScope(Dispatchers.Default).async {
            for (i in this@ConvertWhenList) {
                val job = async(Dispatchers.IO) {
                    i.resizeImage(context)
                }.also { it.join() }

                result.add(job.await())
            }
            result
        }
        return hi.await()
    }

    fun String.ConvertWhenSingle(context : Context) : String {
        return this.resizeImage(context)
    }

    private fun String.resizeImage(context : Context) : String {
        return File(Bitmap.createScaledBitmap(this.convertUriToBitmap(context), 512, 512, true).convertToUri(context).toString()).path
    }

    private fun String.convertUriToBitmap(context: Context) : Bitmap {
        return if(Build.VERSION.SDK_INT >= 28) ImageDecoder.decodeBitmap(ImageDecoder.createSource(File(this)))
        else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, this.toUri())
        }
    }

    private fun Bitmap.convertToUri(context : Context) : Uri {
        return Uri.parse(MediaStore.Images.Media.insertImage(context.contentResolver, this, this.toString(), null))
    }

    fun Uri.deletePic(context: Context) {
        context.contentResolver.delete(this, null, null)
    }




}