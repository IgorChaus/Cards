package com.chaus.cards

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.chaus.cards.view.MenuViewScreen

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null){
            launchMenuScreen()
        }
    }

    private fun launchMenuScreen(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_activity, MenuViewScreen.getInstance())
            .commit()
    }
}