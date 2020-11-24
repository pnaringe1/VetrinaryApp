package com.i.vetrinarykotlinapp.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.LruCache
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.i.vetrinarykotlinapp.R
import java.net.HttpURLConnection
import java.net.URL
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object Util {

    private var imageCache: LruCache<String, Bitmap>
    private var executorService: ExecutorService =
        Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
    private val uiHandler: Handler = Handler(Looper.getMainLooper())

    init {
        val maxMemory: Long = Runtime.getRuntime().maxMemory() / 1024
        val cacheSize: Int = (maxMemory / 4).toInt()

        imageCache = object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String?, bitmap: Bitmap?): Int {
                return (bitmap?.rowBytes ?: 0) * (bitmap?.height ?: 0) / 1024
            }
        }
    }

    /**
     * This method is to display Image from imageUrl in imageView
     */
    fun displayImage(url: String, imageView: ImageView) {
        val cached = imageCache.get(url)
        if (cached != null) {
            updateImageView(
                imageView,
                cached
            )
            return
        }

        imageView.tag = url
        executorService.submit {
            val bitmap: Bitmap? =
                downloadImage(url)
            if (bitmap != null) {
                if (imageView.tag == url) {
                    updateImageView(
                        imageView,
                        bitmap
                    )
                }
                imageCache.put(url, bitmap)
            }
        }
    }

    private fun updateImageView(imageView: ImageView, bitmap: Bitmap) {
        uiHandler.post {
            imageView.setImageBitmap(bitmap)
        }
    }

    private fun downloadImage(mainUrl: String): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val url = URL(mainUrl)
            val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
            bitmap = BitmapFactory.decodeStream(conn.inputStream)
            conn.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return bitmap
    }

    /**
     * This method gives Alert Message with OK button
     */
    fun messageDialog(context: Context?, message: String?) {
        val dialog = AlertDialog.Builder(context)
        dialog.setMessage(message)
        dialog.setPositiveButton(
            R.string.ok
        ) { dialog, which -> dialog.dismiss() }
        dialog.show()
    }

    /**
     * This method is to match the current time with working time
     */
    fun isWorkingHours(workingHrs: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val delimiter = " "
            val parts = workingHrs.split(delimiter)
            parts.let {
                if (it.size > 3) {
                    val startTime = it[1]
                    val endTime = it[3]
                    !isWeekend() && isTimeNow(startTime, endTime)
                }
            }
        }

        return false
    }

    /**
     * This Method is compare start time and end time
     */
    private fun isTimeNow(startTime: String?, endTime: String?): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("H:m", Locale.US)
                val start = LocalTime.parse(startTime, formatter)
                val end = LocalTime.parse(endTime, formatter)
                val now = LocalTime.now()
                if (start.isBefore(end)) return now.isAfter(start) && now.isBefore(end)
                return if (now.isBefore(start)) now.isBefore(start) && now.isBefore(end) else now.isAfter(
                    start
                ) && now.isAfter(end)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return false

    }

    /**
     * This method is to check current date is weekend or not
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun isWeekend(): Boolean {
        val date: LocalDate = LocalDate.now()
        val day: DayOfWeek = DayOfWeek.of(date.get(ChronoField.DAY_OF_WEEK))
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY
    }


}
