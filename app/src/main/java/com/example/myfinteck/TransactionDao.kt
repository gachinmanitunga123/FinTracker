package com.example.myfinteck

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TransactionDao {

    @Insert
    suspend fun insert(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): LiveData<List<TransactionEntity>>

    @Delete
    suspend fun delete(transaction: TransactionEntity)

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'expense'")
    fun getTotalExpense(): LiveData<Double>


    //income and expenses
    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'income'")
    fun getTotalIncome(): LiveData<Double>

    @Query("SELECT * FROM transactions WHERE type = 'expense' ORDER BY date DESC")
    fun getExpenses(): LiveData<List<TransactionEntity>>

    @Query("""
    SELECT category, SUM(amount) as total 
    FROM transactions 
    WHERE type = 'expense' 
    GROUP BY category
""")
    fun getExpenseByCategory(): LiveData<List<CategoryTotal>>

    @Query("""
    SELECT * FROM transactions 
    WHERE type = 'expense' 
    ORDER BY date DESC 
    LIMIT 2
""")
    fun getRecentExpenses(): LiveData<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE title LIKE '%' || :query || '%' ORDER BY date DESC")
    fun searchTransactions(query: String): LiveData<List<TransactionEntity>>

}