package com.benny.resin_ku

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_info.*

class ActivityInfo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        supportActionBar?.hide()

        email.setOnClickListener{ v ->
            emailIntent()
        }
        github.setOnClickListener{ v ->
            githubIntent()
        }
        facebook.setOnClickListener { v ->
            facebookIntent()
        }
        instagram.setOnClickListener { v ->
            instagramIntent()
        }
    }

    private fun emailIntent() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "message/rfc822"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("m.b.fajri06@gmail.com"))
        intent.putExtra(Intent.EXTRA_SUBJECT, "Any subject if you want")
        intent.setPackage("com.google.android.gm")
        if (intent.resolveActivity(packageManager) != null) startActivity(intent) else Toast.makeText(
            this,
            "Gmail App is not installed",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun whatsappIntent() {
        val contact: String = "+6282335952153"
        val url = "https://api.whatsapp.com/send?phone=$contact"
        try{
            val pm: PackageManager = this.getPackageManager()
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }catch (e: PackageManager.NameNotFoundException){
            Toast.makeText(this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show()
        }
    }

    private fun instagramIntent() {
        val url = "https://www.instagram.com/"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    private fun facebookIntent() {
        val url = "https://facebook.com/muhammad.benny.779/"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    private fun githubIntent() {
        val url = "https://github.com/bennyfajri"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }
}