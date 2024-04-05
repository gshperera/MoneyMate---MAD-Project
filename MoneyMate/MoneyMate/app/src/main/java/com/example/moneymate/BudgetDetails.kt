package com.example.moneymate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.moneymate.models.BudgetModel
import com.google.firebase.database.FirebaseDatabase

class BudgetDetails : AppCompatActivity() {
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget_details)

        val budgetName : TextView = findViewById(R.id.budget_name)
        val budgetAmount : TextView = findViewById(R.id.amount)
        val budgetPeriod : TextView = findViewById(R.id.timePeriod)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)

        // Get data from RecycleView
        val bundle : Bundle?= intent.extras

        val budgetId = bundle!!.getString("budgetId")
        val name = bundle.getString("budgetName")
        val amount = bundle.getString("budgetAmount")
        val period = bundle.getString("budgetPeriod")
        val category = bundle.getString("budgetCat")

        // set data into new activity
        budgetName.text = name
        budgetAmount.text = amount
        budgetPeriod.text = period

        btnUpdate.setOnClickListener{
            openUpdateDialog(
                budgetId.toString(),
                name.toString(),
                amount.toString(),
                period.toString(),
                category.toString()
            )
        }

        btnDelete.setOnClickListener{
            deleteRecord(
                budgetId.toString()
            )
        }
    }

    private fun openUpdateDialog(budgetId: String, budgetName: String, budgetAmount: String, budgetPeriod: String, budgetCategory: String){

        val updateDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val updateDialogView = inflater.inflate(R.layout.update_budget, null)

        updateDialog.setView(updateDialogView)

        val b_name = updateDialogView.findViewById<EditText>(R.id.updateName)
        val b_period = updateDialogView.findViewById<EditText>(R.id.updatePeriod)
        val b_amount = updateDialogView.findViewById<EditText>(R.id.updateAmount)
        val b_category = updateDialogView.findViewById<EditText>(R.id.updateCategory)
        val b_updateBtn = updateDialogView.findViewById<Button>(R.id.btnUpdateBudget)

        b_name.setText(budgetName)
        b_period.setText(budgetPeriod)
        b_amount.setText(budgetAmount)
        b_category.setText(budgetCategory)

        updateDialog.setTitle("Updating $budgetName Recorde")

        val alertDialog = updateDialog.create()
        alertDialog.show()

        b_updateBtn.setOnClickListener{
            updateBudgetData(
                budgetId,
                b_name.text.toString(),
                b_period.text.toString(),
                b_amount.text.toString(),
                b_category.text.toString()
            )

            Toast.makeText(applicationContext, "Budget is Updated", Toast.LENGTH_LONG).show()

            // Setting updated data to textViews
            val name = b_name.text.toString()
            val amount = b_amount.text.toString()
            val period = b_period.text.toString()
            val category = b_category.text.toString()

            alertDialog.dismiss()
        }
    }

    private fun updateBudgetData(id: String, name: String, period: String, amount: String, category: String){
        val database = FirebaseDatabase.getInstance().getReference("Budget").child(id)
        val updatedBudget = BudgetModel(id, name, period, amount, category)
        database.setValue(updatedBudget)
    }

    private fun deleteRecord(id: String){
        val database = FirebaseDatabase.getInstance().getReference("Budget").child(id)
        val delTask = database.removeValue()

        delTask.addOnSuccessListener {
            Toast.makeText(this, "Budget Data Deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, DisplayBudget::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener { error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }
}