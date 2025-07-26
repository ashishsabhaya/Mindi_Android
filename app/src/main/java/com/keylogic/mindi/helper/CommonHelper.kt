package com.keylogic.mindi.helper

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.keylogic.mindi.custom.StrokeTextView
import com.keylogic.mindi.enums.DeviceType
import com.keylogic.mindi.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class CommonHelper {
    companion object {
        val INSTANCE = CommonHelper()
        var currCFTheme = "_free0"
        var currCBTheme = R.drawable.cards_0
        var isAdsFree = false
        var isNewUser = true
        var isNewDay = true
        var lastSavedDate = 0L
        var isMusicEnabled = true
        var isSoundEnabled = false
        var isVibrationEnabled = true
        var deviceType = DeviceType.NONE
    }

    fun setScaleOnTouch(
        view: View,
        scaleDown: Float = 0.9f,
        duration: Long = 100,
        onclick: () -> Unit
    ) {
        var count = 0
        view.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    count = 0
                    v.animate().scaleX(scaleDown).scaleY(scaleDown).setDuration(duration).start()
                    true // Consume all touch events
                }
                MotionEvent.ACTION_MOVE -> {
                    count++
                    false // Consume all touch events
                }
                MotionEvent.ACTION_UP -> {
                    v.animate().scaleX(1f).scaleY(1f).setDuration(duration)
                        .withEndAction {
                            if (count < 10) {
                                v.performClick()
                                onclick()
                            }
                        }
                        .start()
                    return@setOnTouchListener true // Consume the touch so performClick isn't called twice
                }
//
//                MotionEvent.ACTION_CANCEL -> {
//                    v.animate().scaleX(1f).scaleY(1f).setDuration(duration).start()
//                }
                else -> {
                    false
                }
            }
//            true // Consume all touch events
        }
    }

    fun checkConnection(context: Context, toast: Boolean): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)

        val check = capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true

        if (!check && toast) {
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
            }
        }

        return check
    }

    fun isTimePassed(currentTime: Long, targetTime: Long): Boolean {
        return currentTime > targetTime
    }

    @SuppressLint("DefaultLocale")
    fun formatChip(num: Long, inGlobalFormat: Boolean = false): String {
        if (inGlobalFormat) {
            return when {
                num < 1_000L -> num.toString()
                num < 1_000_000L -> {
                    // Thousands
                    String.format("%.1fk", num / 1_000.0)
                        .trimEnd('0').trimEnd('.')
                }
                num < 1_000_000_000L -> {
                    // Millions
                    String.format("%.2fm", num / 1_000_000.0)
                        .trimEnd('0').trimEnd('.')
                }
                else -> {
                    // Billions
                    String.format("%.2fb", num / 1_000_000_000.0)
                        .trimEnd('0').trimEnd('.')
                }
            }
        }
        else {
            return when {
                num < 1_000L -> num.toString()
                num < 1_00_000L -> {
                    // Thousands
                    String.format("%.1fk", num / 1_000.0)
                        .trimEnd('0').trimEnd('.')
                }
                num < 1_00_00_000L -> {
                    // Lakhs
                    String.format("%.1fl", num / 1_00_000.0)
                        .trimEnd('0').trimEnd('.')
                }
                else -> {
                    // Crores
                    String.format("%.2fcr", num / 1_00_00_000.0)
                        .trimEnd('0').trimEnd('.')
                }
            }
        }
    }

    fun setUpCursorVisibility(defaultTxt : String, editLayout: TextInputLayout, editText: TextInputEditText, textView: StrokeTextView,
                              onUpdate: (String) -> Unit) {
        textView.text = defaultTxt

        setScaleOnTouch(textView, onclick = {
            textView.visibility = View.GONE
            editLayout.visibility = View.VISIBLE
            editText.isCursorVisible = true
            editText.requestFocus()
            showKeyboard(editText)
        })

        editText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                finishEditingName(editLayout, editText, textView, onUpdate)
            }
        }

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                editText.clearFocus() // triggers focus listener
                true
            } else {
                false
            }
        }
    }

    private fun finishEditingName(
        editLayout: TextInputLayout,
        editText: TextInputEditText,
        textView: StrokeTextView,
        onUpdate: (String) -> Unit
    ) {
        val newName = editText.text.toString().trim()
        onUpdate(newName)

        textView.text = newName.ifEmpty {
            editLayout.context.getString(R.string.enter_your_name)
        }

        editLayout.visibility = View.GONE
        textView.visibility = View.VISIBLE
        editText.isCursorVisible = false
        hideKeyboard(editText)
    }

    private fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun showKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    fun getChip(count: Long): String {
        return formatChip(count)
    }

    fun getTotalChip(): String {
        return formatChip(ProfileHelper.totalChips)
    }

    fun getLeftTime(endTimeMillis: Long): String {
        val currentTimeMillis = System.currentTimeMillis()

        if (currentTimeMillis > endTimeMillis) {
            return "Expired"
        }

        val currentCal = Calendar.getInstance()
        val endCal = Calendar.getInstance().apply { timeInMillis = endTimeMillis }

        // Reset times to midnight to compare days only
        currentCal.set(Calendar.HOUR_OF_DAY, 0)
        currentCal.set(Calendar.MINUTE, 0)
        currentCal.set(Calendar.SECOND, 0)
        currentCal.set(Calendar.MILLISECOND, 0)

        endCal.set(Calendar.HOUR_OF_DAY, 0)
        endCal.set(Calendar.MINUTE, 0)
        endCal.set(Calendar.SECOND, 0)
        endCal.set(Calendar.MILLISECOND, 0)

        val diffMillis = endCal.timeInMillis - currentCal.timeInMillis
        val daysLeft = (diffMillis / (1000 * 60 * 60 * 24)).toInt()

        return when {
            daysLeft > 1 -> "$daysLeft days"
            daysLeft == 1 -> "1 day"
            daysLeft == 0 -> "Last day"
            else -> "Expired"
        }
    }


    fun getItemSelected(): String {
        return "Selected"
    }

}