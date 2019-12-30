package com.aesthomic.readinglog.util

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.RectF
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.aesthomic.readinglog.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import kotlin.math.min

/**
 * Place a new image file by creating new File
 * with title, date and data type on the name
 */
fun createImageFile(activity: Activity): File {
    val timeStamp = getTime()
    val pictureName = activity.resources
        .getString(R.string.picture_file_format, timeStamp)
    val storageDir = activity.getExternalFilesDir(
        Environment.DIRECTORY_PICTURES)

    return File.createTempFile(
        pictureName,
        ".jpg",
        storageDir)
}

/**
 * Write bitmap to a file by compressing to 50%
 *
 * @param bitmap : A source that contains an image
 * @param destination: A file that will be written
 */
suspend fun writeBitmapFile (bitmap: Bitmap, destination: File) {
    withContext(Dispatchers.IO) {
        var out: FileOutputStream? = null

        try {
            out =  FileOutputStream(destination)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out)
        } catch (e: Exception) {
            Log.e("Error", "Failed to save image due to: $e")
        } finally {
            out?.close()
        }
    }
}

/**
 * Uri that has successfully obtained will be decoded to a bitmap
 * There will be some resizing of bitmap on the process of decoding
 */
fun decodeUriBitmap (context: Context, uri: Uri,
                     reqWidth: Int, reqHeight: Int): Bitmap {
    val options = BitmapFactory.Options()

    options.inJustDecodeBounds = true
    BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri), null, options)

    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
    options.inJustDecodeBounds = false

    var bitmap = BitmapFactory.decodeStream(
        context.contentResolver.openInputStream(uri), null, options)
        ?: BitmapFactory.decodeResource(context.resources, R.drawable.ic_photo)

    bitmap = cropSquareBitmap(bitmap)
    return resizeBitmap(bitmap, reqWidth, reqHeight)
}

/**
 * Calculate the suit inSampleSize for resizing an image
 */
fun calculateInSampleSize(options: BitmapFactory.Options,
                          reqWidth: Int, reqHeight: Int): Int {
    val width = options.outWidth
    val height = options.outHeight
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {
        val halfWidth = width / 2
        val halfHeight = height / 2

        while ((halfHeight / inSampleSize) > reqHeight &&
            (halfWidth / inSampleSize) > reqWidth) {
            inSampleSize *= 2
        }
    }

    return inSampleSize
}

/**
 * Crop a bitmap to make width and height become equal
 */
fun cropSquareBitmap (bitmap: Bitmap) : Bitmap {
    val dimension = min(bitmap.width, bitmap.height)
    return ThumbnailUtils.extractThumbnail(bitmap, dimension, dimension)
}

/**
 * Resize the bitmap to the requested width and height
 */
fun resizeBitmap (bitmap: Bitmap, reqWidth: Int, reqHeight: Int): Bitmap {
    val matrix = Matrix()

    matrix.setRectToRect(
        RectF(0F, 0F, bitmap.width.toFloat(), bitmap.height.toFloat()),
        RectF(0F, 0F, reqWidth.toFloat(), reqHeight.toFloat()),
        Matrix.ScaleToFit.CENTER)

    return Bitmap.createBitmap(bitmap, 0, 0,
        bitmap.width, bitmap.height, matrix, true)
}
