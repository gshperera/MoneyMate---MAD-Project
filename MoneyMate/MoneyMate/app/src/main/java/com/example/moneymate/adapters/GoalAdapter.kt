package com.example.moneymate.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymate.R
import com.example.moneymate.models.GoalModel


// FETCH
class GoalAdapter (private val goalList: ArrayList<GoalModel>) :
    RecyclerView.Adapter<GoalAdapter.ViewHolder>(){

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.goal_list_item, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentGoal = goalList[position]
        holder.tvGoalName.text = currentGoal.goalName
        holder.tvGoalAmount.text = currentGoal.goalAmount
    }

    override fun getItemCount(): Int {
        return goalList.size
    }

    class ViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val tvGoalName : TextView = itemView.findViewById(R.id.goalName)
        val tvGoalAmount : TextView = itemView.findViewById(R.id.goalAmount)

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
    }

}