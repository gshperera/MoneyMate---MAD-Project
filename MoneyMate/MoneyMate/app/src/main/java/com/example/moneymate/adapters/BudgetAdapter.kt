package com.example.moneymate.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymate.R
import com.example.moneymate.models.BudgetModel

class BudgetAdapter (private val budgetList: ArrayList<BudgetModel>) : RecyclerView.Adapter<BudgetAdapter.ViewHolder>(){

    private lateinit var bListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener){
        bListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.budget_list_item, parent, false)
        return ViewHolder(itemView, bListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val curBudget = budgetList[position]
        holder.tvBudgetName.text = curBudget.name
        holder.tvBudgetPeriod.text = curBudget.period
        holder.tvBudgetAmount.text = curBudget.amount
    }

    override fun getItemCount(): Int {
        return budgetList.size
    }

    class ViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val tvBudgetName : TextView = itemView.findViewById(R.id.tvName)
        val tvBudgetPeriod : TextView = itemView.findViewById(R.id.tvPeriod)
        val tvBudgetAmount : TextView = itemView.findViewById(R.id.tvAmount)

        init {
            itemView.setOnClickListener{
                clickListener.onItemClick(adapterPosition)
            }
        }
    }


}