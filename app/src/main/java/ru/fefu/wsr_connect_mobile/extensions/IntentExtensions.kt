package ru.fefu.wsr_connect_mobile.extensions

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.view.View
import com.bumptech.glide.Glide
import ru.fefu.wsr_connect_mobile.BASE_URL
import ru.fefu.wsr_connect_mobile.R
import java.io.ByteArrayOutputStream
import java.net.URL

fun Intent.createBitmapFromResult(activity: Activity): Bitmap? {
    val intentBundle = this.extras
    val intentUri = this.data
    var bitmap: Bitmap? = null
    if (intentBundle != null) {
        bitmap = (intentBundle.get("data") as? Bitmap)?.apply {
            compress(Bitmap.CompressFormat.JPEG, 75, ByteArrayOutputStream())
        }
    }

    if (bitmap == null && intentUri != null) {
        intentUri.let { bitmap = BitmapUtils.decodeBitmap(intentUri, activity) }
    }
    return bitmap
}