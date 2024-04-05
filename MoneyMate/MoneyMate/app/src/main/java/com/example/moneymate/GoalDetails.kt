package com.example.moneymate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.moneymate.models.GoalModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class GoalDetails : AppCompatActivity() {

    private lateinit var updateBtn: Button
    private lateinit var deleteBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal_details)

        val goalName : TextView = findViewById(R.id.goalName)
        val goalAmount : TextView = findViewById(R.id.amount)
        val goalSaved : TextView = findViewById(R.id.savedAmount)
        val goalDate : TextView = findViewById(R.id.estimatedDate)

        updateBtn = findViewById(R.id.updateBtn)
        deleteBtn = findViewById(R.id.deleteBtn)

        // Get data from RecycleView
        val bundle : Bundle?= intent.extras

        val goalId = bundle!!.getString("goalId")
        val name = bundle.getString("goalName")
        val amount = bundle.getString("goalAmount")
        val saved = bundle.getString("goalSaved")
        val date = bundle.getString("goalDate")

        // set data into new activity
        goalName.text = name
        goalAmount.text = amount
        goalSaved.text = saved
        goalDate.text = date

        updateBtn.setOnClickListener{
            openUpdateDialog(
                goalId.toString(),
                name.toString(),
                amount.toString(),
                saved.toString(),
                date.toString()
            )
        }

        deleteBtn.setOnClickListener{
            deleteRecord(
                goalId.toString()
            )
        }

    }

    private fun openUpdateDialog(goalId: String, goalName: String, goalAmount: String, goalSaved: String, goalDate: String) {

        val updateDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val updateDialogView = inflater.inflate(R.layout.update_goal, null)

        updateDialog.setView(updateDialogView)

        val name = updateDialogView.findViewById<EditText>(R.id.edtName)
        val amount = updateDialogView.findViewById<EditText>(R.id.edtAmount)
        val saved = updateDialogView.findViewById<EditText>(R.id.edtSaved)
        val date = updateDialogView.findViewById<EditText>(R.id.edtDate)
        val updateBtn = updateDialogView.findViewById<Button>(R.id.UpdateBtn)

        name.setText(goalName)
        amount.setText(goalAmount)
        saved.setText(goalSaved)
        date.setText(goalDate)

        updateDialog.setTitle("Updating $goalName Recorde")

        val alertDialog = updateDialog.create()
        alertDialog.show()

        updateBtn.setOnClickListener{
            updateGoalData(
                goalId,
                name.text.toString(),
                amount.text.toString(),
                saved.text.toString(),
                date.text.toString()
            )

            Toast.makeText(applicationContext, "Goal is Updated", Toast.LENGTH_LONG).show()

            // Setting updated data to textViews
            val name = name.text.toString()
            val amount = amount.text.toString()
            val saved = saved.text.toString()
            val date = date.text.toString()

            alertDialog.dismiss()
        }
    }

    private fun updateGoalData(id: String, name: String, amount: String, saved: String, date: String) {
        val auth = FirebaseAuth.getInstance()
        val authId = auth.currentUser!!.uid
        val database = FirebaseDatabase.getInstance().getReference("Goals").child(authId)
        //val database = FirebaseDatabase.getInstance().getReference("Goals").child(id)
        val updatedGoal = GoalModel(id, name, amount, saved, date)
        database.setValue(updatedGoal)
    }

    private fun deleteRecord(toString: String) {
        val auth = FirebaseAuth.getInstance()
        val authId = auth.currentUser!!.uid
        val database = FirebaseDatabase.getInstance().getReference("Goals").child(authId)

        val delTask = database.removeValue()

        delTask.addOnSuccessListener {
            Toast.makeText(this, "Goal Data Deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, DisplayGoals::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener { error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }

    }
}