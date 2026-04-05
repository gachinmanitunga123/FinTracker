package com.example.myfinteck

class TransactionRepository(private val dao: TransactionDao) {

    val allTransactions = dao.getAllTransactions()
    //income and expenses
    val totalIncome = dao.getTotalIncome()
    val totalExpense = dao.getTotalExpense()

    val expenses = dao.getExpenses()
    val categoryData = dao.getExpenseByCategory()
    val recentTransactions = dao.getRecentExpenses()
    suspend fun insert(transaction: TransactionEntity) {
        dao.insert(transaction)
    }
    suspend fun delete(transaction: TransactionEntity) {
        dao.delete(transaction)
    }
    fun search(query: String) = dao.searchTransactions(query)
}
