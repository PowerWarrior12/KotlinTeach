package com.example.kotlinteach

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.delay

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
    private suspend fun process(){
        for (i in 1..5){
            delay(500L)
            Log.i("MyLog","HELLO : $i")
        }
        Log.i("MyLog","Process was ready")
    }



}