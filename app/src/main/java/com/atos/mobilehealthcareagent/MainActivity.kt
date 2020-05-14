package com.atos.mobilehealthcareagent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import com.atos.mobilehealthcareagent.fragments.RegistrationBasicInfo

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.framelayout_for_fragments, RegistrationBasicInfo())
            .commit()

    }


    fun openSecondFragment(view: View) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.framelayout_for_fragments, RegistrationBasicInfo())
            .commit()

    }


}
