package com.keylogic.mindi.Share

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import com.keylogic.mindi.R

object AppShare {

    fun shareInstagram(context: Context) {
        val arr = getMsgAndLink(context)
        val message = arr[0]
        val link = arr[1]
        val shareText = "$message\n$link"
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
            setPackage("com.instagram.android")
        }

        try {
            context.startActivity(sendIntent)
        } catch (e: ActivityNotFoundException) {
            val playStoreIntent = Intent(Intent.ACTION_VIEW).apply {
                data = getPlatformLink(context, R.string.instagram_package)
            }
            context.startActivity(playStoreIntent)
        }
    }

    fun shareFacebook(context: Context) {
        val shareText = getMsgAndLink(context)
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
            setPackage("com.facebook.orca")
        }

        try {
            context.startActivity(sendIntent)
        } catch (e: ActivityNotFoundException) {
            val playStoreIntent = Intent(Intent.ACTION_VIEW).apply {
                data = getPlatformLink(context, R.string.facebook_package)
            }
            context.startActivity(playStoreIntent)
        }
    }

    fun shareWhatsApp(context: Context) {
        val shareText = getMsgAndLink(context)

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
            setPackage("com.whatsapp")
        }

        try {
            context.startActivity(sendIntent)
        } catch (e: ActivityNotFoundException) {
            val playStoreIntent = Intent(Intent.ACTION_VIEW).apply {
                data = getPlatformLink(context, R.string.whatsapp_package)
            }
            context.startActivity(playStoreIntent)
        }
    }

    fun shareGeneric(context: Context) {
        val shareText = getMsgAndLink(context)

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        context.startActivity(Intent.createChooser(sendIntent, context.getString(R.string.share_via)))
    }

    private fun getMsgAndLink(context: Context): String {
        val message = context.getString(R.string.app_share_msg)
        val link = context.getString(R.string.app_link)
        return "$message\n$link"
    }

    private fun getPlatformLink(context: Context, id: Int): Uri {
        val base = context.getString(R.string.app_share_base)
        val link = context.getString(id)
        return "$base$link".toUri()
    }

}