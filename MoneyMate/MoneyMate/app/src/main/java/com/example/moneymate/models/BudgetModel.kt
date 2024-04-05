package com.example.moneymate.models

data class BudgetModel(
    var budgetId: String? = null,
    var name: String? = null,
    var period: String? = null,
    var amount: String? = null,
    var category: String? = null
)
