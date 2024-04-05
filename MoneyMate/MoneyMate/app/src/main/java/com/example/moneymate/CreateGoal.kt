package com.example.moneymate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.moneymate.models.GoalModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateGoal : AppCompatActivity() {

    private lateinit var edtName: EditText
    private lateinit var edtAmount: EditText
    private lateinit var edtSaved: EditText
    private lateinit var edtDate: EditText
    private lateinit var addBtn: Button

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_goal)

        edtName = findViewById(R.id.edtName)
        edtAmount = findViewById(R.id.edtAmount)
        edtSaved = findViewById(R.id.edtSaved)
        edtDate = findViewById(R.id.edtDate)
        addBtn = findViewById(R.id.addBtn)

        //dbRef = FirebaseDatabase.getInstance().getReference("Goals")
        val auth = FirebaseAuth.getInstance()
        val authId = auth.currentUser!!.uid
        database = FirebaseDatabase.getInstance().getReference("Goals").child(authId)

        addBtn.setOnClickListener{
            saveGoalData()
        }
    }

    private fun saveGoalData() {
        // Getting Values
        val name = edtName.text.toString()
        val amount = edtAmount.text.toString()
        val saved = edtSaved.text.toString()
        val date = edtDate.text.toString()

        if(name.isEmpty()){
            edtName.error = "Please Enter the Name"
        }
        if(amount.isEmpty()){
            edtAmount.error = "Please Enter the Amount"
        }
//        if(saved.isEmpty()){
//            edtSaved.error = "Please Enter the Saved Amount"
//        }
        if(date.isEmpty()){
            edtDate.error = "Please Enter the Desired Date"
        }

        // Create Unique Id
        val goalId = database.push().key!!

        val goal = GoalModel(goalId, name, amount, saved, date)

        database.child(goalId).setValue(goal).addOnCompleteListener{
            Toast.makeText(this, "Goal Added Successfully", Toast.LENGTH_LONG).show()

            edtName.text.clear()
            edtAmount.text.clear()
            edtSaved.text.clear()
            edtDate.text.clear()

        }.addOnFailureListener {err ->
            Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
        }
    }
}