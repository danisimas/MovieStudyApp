package com.example.moviestudyapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ProgressBar
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class SplashActivity : Activity() {

    internal lateinit var pb: ProgressBar
    internal var counter = 0

    val a = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_telainicio)
        prog()
    }

    fun prog() {


        pb = findViewById<View>(R.id.progressBar) as ProgressBar

        val t = Timer()
        val ts = object : TimerTask() {


            override fun run() {
                counter++
                pb.progress = counter
                if (counter == 100)
                    if (a.currentUser == null) {
                        val intent = Intent(this@SplashActivity, IntroActivity::class.java)
                            startActivity(intent)
                        finish()

                    } else {
                        val intent = Intent(this@SplashActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
            }

        }

        t.schedule(ts, 200, 20)
    }
}

