package gov.bd.mpportal.utils

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.widget.NestedScrollView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.content.ContextCompat

import android.os.Environment
import java.io.File


var LANGUAGE = "list_language"
var LANGUAGE_DEFAULT = "bn"


val baseUrl = "http://uttoron.nanoit.biz/"

fun datePickerDialoge(tvDate: TextView, context: Context) : String {
    var date = ""
    val newCalendar = Calendar.getInstance()
    val StartTime = DatePickerDialog(
        context!!,
        { view, year, monthOfYear, dayOfMonth ->
            val newDate = Calendar.getInstance()
            newDate[year, monthOfYear] = dayOfMonth
            val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
           // tvDate.setText(AppConstant.getDigitBanglaFromEnglish(dateFormatter.format(newDate.time)))
            date = dateFormatter.format(newDate.time)
        },
        newCalendar[Calendar.YEAR],
        newCalendar[Calendar.MONTH],
        newCalendar[Calendar.DAY_OF_MONTH]
    )
    StartTime.show()

    return date
}

fun datePickerDialoge(context: Context, minusYear: Int = 0, onDateSelected: (String) -> Unit) {
    val newCalendar = Calendar.getInstance()
    val StartTime = DatePickerDialog(
        context!!,
        { view, year, monthOfYear, dayOfMonth ->
            val newDate = Calendar.getInstance()
            newDate[year, monthOfYear] = dayOfMonth
            val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
            onDateSelected(dateFormatter.format(newDate.time))
        },
        newCalendar[Calendar.YEAR] - minusYear,
        newCalendar[Calendar.MONTH],
        newCalendar[Calendar.DAY_OF_MONTH]
    )
    StartTime.show()
}

fun timePickerDialoge(tvDate: TextView, context: Context) {
    val cal = Calendar.getInstance()
    val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        //tvDate.text = AppConstant.getDigitBanglaFromEnglish(SimpleDateFormat("HH:mm aa").format(cal.time))
    }
    TimePickerDialog(
        context,
        timeSetListener,
        cal.get(Calendar.HOUR_OF_DAY),
        cal.get(Calendar.MINUTE),
        true
    ).show()
}


fun showHide(view: View) {
    view.visibility = if (view.visibility == View.VISIBLE) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

fun show(view: View) {
    view.visibility = if (view.visibility == View.GONE) {
        View.VISIBLE
    } else {
        View.VISIBLE
    }
}

fun hide(view: View) {
    view.visibility = if (view.visibility == View.VISIBLE) {
        View.GONE
    } else {
        View.GONE
    }
}

fun scroll(nestedScrollView: NestedScrollView) {

    nestedScrollView?.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
        if (scrollY > oldScrollY) {
            Log.e("Scroll", "Scroll DOWN")
        }
        if (scrollY < oldScrollY) {
            Log.i("Scroll", "Scroll UP")
        }
        if (scrollY == 0) {
            Log.i("Scroll", "TOP SCROLL")
        }
        if (scrollY == v.measuredHeight - v.getChildAt(0).measuredHeight) {
            Log.i("Scroll", "BOTTOM SCROLL")
        }
    })
}


@RequiresApi(Build.VERSION_CODES.O)
fun dateDiffer(date1: String, date2: String): Int {
    var daysBetween = 0
    val sdf = SimpleDateFormat("dd-MM-yyyy")

    try {
        val d1: java.util.Date? = sdf.parse(date1)
        val d2: Date? = sdf.parse(date2)

        val diff: Long = d2!!.getTime() - d1!!.getTime()

        daysBetween = java.util.concurrent.TimeUnit.DAYS.convert(
            diff,
            java.util.concurrent.TimeUnit.MILLISECONDS
        ).toInt()

        daysBetween = daysBetween+1
//           daysBetween= Duration.between(date1, date2).toDays().toInt()
//           println("Days: $daysBetween")
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return daysBetween
}


fun getRootDirPath(context: Context): String? {
    return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
        val file: File = ContextCompat.getExternalFilesDirs(
            context.applicationContext,
            null
        )[0]
        file.getAbsolutePath()
    } else {
        context.applicationContext.filesDir.absolutePath
    }
}

fun getProgressDisplayLine(currentBytes: Long, totalBytes: Long): String? {
    return getBytesToMBString(currentBytes) + "/" + getBytesToMBString(totalBytes)
}

private fun getBytesToMBString(bytes: Long): String {
    return java.lang.String.format(Locale.ENGLISH, "%.2fMb", bytes / (1024.00 * 1024.00))
}
