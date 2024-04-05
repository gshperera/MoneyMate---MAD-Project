package com.example.moneymate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.moneymate.models.BudgetModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.time.temporal.TemporalAmount

class CreateBudget : AppCompatActivity() {
    private lateinit var edtName: EditText
    private lateinit var edtPeriod: EditText
    private lateinit var edtAmount: EditText
    private lateinit var edtCategory: EditText
    private lateinit var addBtn: Button

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_budget)

        edtName = findViewById(R.id.edtName)
        edtPeriod = findViewById(R.id.edtPeriod)
        edtAmount = findViewById(R.id.edtAmount)
        edtCategory = findViewById(R.id.edtCategory)
        addBtn = findViewById(R.id.addBtn)

        database = FirebaseDatabase.getInstance().getReference("Budget")

        addBtn.setOnClickListener{
            saveBudgetData()
        }
    }

    private fun saveBudgetData(){
        // Getting Values
        val name = edtName.text.toString()
        val period = edtPeriod.text.toString()
        val amount = edtAmount.text.toString()
        val category = edtCategory.text.toString()

        if(name.isEmpty()){
            edtName.error = "Please Enter the Name"
        }
        if(period.isEmpty()){
            edtPeriod.error = "Please Enter the Period"
        }
        if(amount.isEmpty()){
            edtAmount.error = "Please Enter the Amount"
        }
        if(category.isEmpty()){
            edtCategory.error = "Please Enter the Category"
        }

        // Create Unique Id
        val budgetId = database.push().key!!

        val budget = BudgetModel(budgetId,name, period, amount, category)

        database.child(budgetId).setValue(budget).addOnCompleteListener{
            Toast.makeText(this, "Budget Added Successfully", Toast.LENGTH_LONG).show()

            edtName.text.clear()
            edtPeriod.text.clear()
            edtAmount.text.clear()
            edtCategory.text.clear()

        }.addOnFailureListener {err ->
            Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
        }
    }
}