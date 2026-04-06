package com.example.myfinteck

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfinteck.databinding.DashboardFragmentBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class DashboardFragment : Fragment() {

    private lateinit var binding: DashboardFragmentBinding

    private lateinit var viewModel: TransactionViewModel
    private lateinit var adapter : TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DashboardFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[TransactionViewModel::class.java]

        var income = 0.0
        var expense = 0.0

        viewModel.totalIncome.observe(viewLifecycleOwner) {
            income = it ?: 0.0
            updateBalance(income, expense)
        }

        viewModel.totalExpense.observe(viewLifecycleOwner) {
            expense = it ?: 0.0
            updateBalance(income, expense)
        }

        // Pass delete function
        adapter = TransactionAdapter { transaction ->

            AlertDialog.Builder(requireActivity())
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete this transaction?")
                .setPositiveButton("Yes") { _, _ ->
                    viewModel.delete(transaction)
                }
                .setNegativeButton("No", null)
                .show()

        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        viewModel.recentTransactions.observe(requireActivity()) {
            //Log.d("DATA_CHECK", "List size: ${list.size}")
            Log.d("CHECK", "Expenses size: ${it.size}")
            adapter.submitList(it)
        }

        viewModel.totalIncome.observe(requireActivity()) {
            binding.tvAmount.text = "+₹${it ?: 0}"
        }

        viewModel.totalExpense.observe(requireActivity()) {
            binding.tvExeAmount.text = "-₹${it ?: 0}"
        }

        viewModel.categoryData.observe(requireActivity()) { list ->
            setupPieChart(list)
        }

        binding.tvSeeAll.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, TransactionFragment())
                .addToBackStack(null)
                .commit()        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun updateBalance(income: Double, expense: Double) {

        val balance = income - expense
        binding.tvCurrentBalance.text = "₹ $balance"

        if (income == 0.0) {
            binding.progressBar.progress = 0
            return
        }

        if (balance >= 0) {
            val percent = ((balance / income) * 100).toInt()

            binding.progressBar.progressDrawable =
                requireContext().getDrawable(R.drawable.progress_blue)

            binding.progressBar.progress = percent
        } else {
            binding.progressBar.progressDrawable =
                requireContext().getDrawable(R.drawable.progress_red)

            binding.progressBar.progress = 100
        }
    }

    private fun setupPieChart(data: List<CategoryTotal>) {

        val entries = ArrayList<PieEntry>()

        for (item in data) {
            entries.add(PieEntry(item.total.toFloat(), item.category))
        }

        val dataSet = PieDataSet(entries, "Expenses")

        dataSet.colors = listOf(
            Color.parseColor("#FF6384"),
            Color.parseColor("#36A2EB"),
            Color.parseColor("#FFCE56"),
            Color.parseColor("#4CAF50"),
            Color.parseColor("#C36241"),
            Color.parseColor("#7E354D")
        )

        val pieData = PieData(dataSet)
        pieData.setValueTextSize(12f)

        binding.pieChart.data = pieData
        binding.pieChart.description.isEnabled = false
        binding.pieChart.centerText = "Expenses"
        binding.pieChart.animateY(1000)
        binding.pieChart.invalidate()

        dataSet.setColors(
            Color.parseColor("#FF6384"),
            Color.parseColor("#36A2EB"),
            Color.parseColor("#FFCE56"),
            Color.parseColor("#4CAF50"),
            Color.parseColor("#C36241"),
            Color.parseColor("#7E354D")
        )

        binding.pieChart.setUsePercentValues(true)

        dataSet.valueTextSize = 10f
    }

}