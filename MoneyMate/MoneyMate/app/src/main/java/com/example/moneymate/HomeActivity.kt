package com.example.moneymate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class HomeActivity : AppCompatActivity() {

    private lateinit var btnBudgets: Button
    private lateinit var btnGoals: Button
    private lateinit var btnDebts: Button
    private lateinit var btnShoppingLists: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btnBudgets = findViewById(R.id.btnBudgets)
        btnGoals = findViewById(R.id.btnGoals)
        btnDebts = findViewById(R.id.btnDebts)
        btnShoppingLists = findViewById(R.id.btnShoppingLists)


        btnBudgets.setOnClickListener {
            val intent = Intent(this, DisplayBudget::class.java)
            startActivity(intent)
        }

        btnGoals.setOnClickListener {
            val intent = Intent(this, DisplayGoals::class.java)
            startActivity(intent)
        }

//        btnDebts.setOnClickListener {
//            val intent = Intent(this, DisplayGoalsActivity::class.java)
//            startActivity(intent)
//        }

//        btnShoppingLists.setOnClickListener {
//            val intent = Intent(this, DisplayGoalsActivity::class.java)
//            startActivity(intent)
//        }
    }
}