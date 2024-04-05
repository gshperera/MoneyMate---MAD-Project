package com.example.moneymate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymate.adapters.GoalAdapter
import com.example.moneymate.models.GoalModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class DisplayGoals : AppCompatActivity() {

    private lateinit var goalRecyclerView: RecyclerView
    private lateinit var addIcon: ImageView
    private lateinit var goalList: ArrayList<GoalModel>
    private lateinit var database: DatabaseReference
    private lateinit var tvGoalData: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_goals)

        addIcon = findViewById(R.id.addIcon)

        addIcon.setOnClickListener {
            val intent = Intent(this, CreateGoal::class.java)
            startActivity(intent)
        }

        goalRecyclerView = findViewById(R.id.rvGoals)
        goalRecyclerView.layoutManager = LinearLayoutManager(this)
        goalRecyclerView.setHasFixedSize(true)
        tvGoalData = findViewById(R.id.progressBar)

        goalList = arrayListOf<GoalModel>()

        getGoalsData()

    }

    private fun getGoalsData() {
        goalRecyclerView.visibility = View.GONE
        tvGoalData.visibility = View.VISIBLE

        val auth = FirebaseAuth.getInstance()
        val authId = auth.currentUser!!.uid
        database = FirebaseDatabase.getInstance().getReference("Goals").child(authId)

        //dbRef = FirebaseDatabase.getInstance().getReference("Goals")


        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                goalList.clear()
                if(snapshot.exists()){
                    for(goalSnap in snapshot.children){
                        val goalData = goalSnap.getValue(GoalModel::class.java)
                        goalList.add(goalData!!)
                    }
                    val mAdapter = GoalAdapter(goalList)
                    goalRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : GoalAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@DisplayGoals, GoalDetails::class.java)

                            //put extra data here
                            intent.putExtra("goalId", goalList[position].goalId)
                            intent.putExtra("goalName", goalList[position].goalName)
                            intent.putExtra("goalAmount", goalList[position].goalAmount)
                            intent.putExtra("goalSaved", goalList[position].goalSaved)
                            intent.putExtra("goalDate", goalList[position].goalDate)
                            startActivity(intent)

                            Toast.makeText(this@DisplayGoals, "You clicked on ${goalList[position].goalName}", Toast.LENGTH_SHORT).show()
                        }

                    })

                    goalRecyclerView.visibility = View.VISIBLE
                    tvGoalData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}