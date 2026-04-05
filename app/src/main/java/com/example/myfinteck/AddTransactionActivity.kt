package com.example.myfinteck

import android.R
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.myfinteck.databinding.ActivityAddTransactionBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.*

class AddTransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddTransactionBinding
    private lateinit var viewModel: TransactionViewModel
    private var selectedType = "expense"
    //data variable
    private var selectedDate: Long = System.currentTimeMillis()


    @SuppressLint("ResourceAsColor", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[TransactionViewModel::class.java]

       // val category = binding.etCategory.text.toString()

        val format = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        binding.etDate.setText(format.format(Date()))

        //date picker
        binding.etDate.setOnClickListener {

            Log.d("DATE_DEBUG", "Clicked")

            val calendar = Calendar.getInstance()

           /* val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)*/


            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->


                    val selectedCal = Calendar.getInstance()
                    selectedCal.set(selectedYear, selectedMonth, selectedDay)

                    selectedDate = selectedCal.timeInMillis

                    val format = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                    binding.etDate.setText(format.format(selectedCal.time))

                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            //disable future dates
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

            datePickerDialog.show()
        }


        val categories = listOf(
            "Food",
            "Shopping",
            "Transport",
            "Housing",
            "Entertainment",
            "Utilities",
            "Others 📦"
        )

        val adapter = ArrayAdapter(
            this,
            R.layout.simple_dropdown_item_1line,
            categories
        )

        binding.etCategory.setAdapter(adapter)

        binding.etCategory.setOnClickListener {
            binding.etCategory.showDropDown()
        }

        //binding.etCategory.setAdapter(adapter)

        //income button
        binding.btnIncome.setOnClickListener {
            selectedType = "income"
            binding.tiCategory.visibility = View.GONE
            binding.btnIncome.setBackgroundColor(getColor(com.example.myfinteck.R.color.white))
            binding.btnExpenses.setBackgroundColor(getColor(com.example.myfinteck.R.color.light_grey))

        }

        //expenses button
        binding.btnExpenses.setOnClickListener {
            selectedType = "expense"
            binding.tiCategory.visibility = View.VISIBLE
            binding.btnExpenses.setBackgroundColor(getColor(com.example.myfinteck.R.color.white))
            binding.btnIncome.setBackgroundColor(getColor(com.example.myfinteck.R.color.light_grey))
        }

        var category = if(selectedType == "income") {
            "Income"
        } else {
            binding.etCategory.text.toString()
        }

       /*var category_list = binding.etCategory.text.toString()
        if (category_list.isEmpty()) {
            Toast.makeText(this, "Select category", Toast.LENGTH_SHORT).show()
            return
        }*/

        //save button
        binding.btnSave.setOnClickListener {

            val title = binding.etTitle.text.toString()
            val amount = binding.etAmount.text.toString().toDouble()
            category = binding.etCategory.text.toString()

            val transaction = TransactionEntity(
                title = title,
                amount = amount,
                type = selectedType,
                category = category,
                date  = selectedDate
            )

            viewModel.insert(transaction)
            finish()
        }
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    @SuppressLint("GestureBackNavigation")
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}