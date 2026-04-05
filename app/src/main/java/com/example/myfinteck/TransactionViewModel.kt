package com.example.myfinteck

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TransactionViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TransactionRepository
    val allTransactions: LiveData<List<TransactionEntity>>

    init {
        val dao = AppDatabase.getDatabase(application).transactionDao()
        repository = TransactionRepository(dao)
        allTransactions = repository.allTransactions

    }

    //income and expenses
    val totalIncome: LiveData<Double> = repository.totalIncome
    val totalExpense: LiveData<Double> = repository.totalExpense

    val expenses: LiveData<List<TransactionEntity>> = repository.expenses

    val categoryData: LiveData<List<CategoryTotal>> = repository.categoryData

    val recentTransactions: LiveData<List<TransactionEntity>> = repository.recentTransactions

    fun insert(transaction: TransactionEntity) = viewModelScope.launch {
        repository.insert(transaction)
    }
    fun delete(transaction: TransactionEntity) = viewModelScope.launch {
        repository.delete(transaction)
    }

    fun search(query: String) = repository.search(query)
}
