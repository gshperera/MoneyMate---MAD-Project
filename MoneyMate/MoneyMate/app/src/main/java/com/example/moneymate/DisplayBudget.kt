package com.example.moneymate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymate.adapters.BudgetAdapter
import com.example.moneymate.models.BudgetModel
import com.google.firebase.database.*

class DisplayBudget : AppCompatActivity() {
    private lateinit var budgetRecyclerView: RecyclerView
    private lateinit var tvBudgetData: TextView
    private lateinit var budgetList: ArrayList<BudgetModel>
    private lateinit var addIcon: ImageView
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_budget)

        addIcon = findViewById(R.id.addIcon)

        addIcon.setOnClickListener {
            val intent = Intent(this, CreateBudget::class.java)
            startActivity(intent)
        }

        budgetRecyclerView = findViewById(R.id.rvBudget)
        budgetRecyclerView.layoutManager = LinearLayoutManager(this)
        budgetRecyclerView.setHasFixedSize(true)
        tvBudgetData = findViewById(R.id.tvLoadingData)

        budgetList = arrayListOf<BudgetModel>()

        getBudgetData()
    }

    private fun getBudgetData(){
        budgetRecyclerView.visibility = View.GONE
        tvBudgetData.visibility = View.VISIBLE

        database = FirebaseDatabase.getInstance().getReference("Budget")

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                budgetList.clear()
                if(snapshot.exists()){
                    for(budgetSnap in snapshot.children){
                        val budget = budgetSnap.getValue(BudgetModel::class.java)
                        budgetList.add(budget!!)
                    }
                    val budgetAdapter = BudgetAdapter(budgetList)
                    budgetRecyclerView.adapter = budgetAdapter

                    budgetAdapter.setOnItemClickListener(object : BudgetAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@DisplayBudget, BudgetDetails::class.java)
//
//                            //put extras
                            intent.putExtra("budgetId", budgetList[position].budgetId)
                            intent.putExtra("budgetName", budgetList[position].name)
                            intent.putExtra("budgetAmount", budgetList[position].amount)
                            intent.putExtra("budgetPeriod", budgetList[position].period)
                            intent.putExtra("budgetCat", budgetList[position].category)
                            startActivity(intent)

                            Toast.makeText(this@DisplayBudget, "You clicked on ${budgetList[position].name}", Toast.LENGTH_SHORT).show()
                        }

                    })

                    budgetRecyclerView.visibility = View.VISIBLE
                    tvBudgetData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}