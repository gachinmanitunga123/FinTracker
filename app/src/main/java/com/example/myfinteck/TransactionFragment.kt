package com.example.myfinteck

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfinteck.databinding.TransactionFragmentBinding

class TransactionFragment : Fragment() {

    private var _binding: TransactionFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TransactionViewModel
    private lateinit var adapter : TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TransactionFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[TransactionViewModel::class.java]

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

            //viewModel.delete(transaction)
        }

        binding.etSearch.addTextChangedListener {

            val query = it.toString()

            if (query.isEmpty()) {
                viewModel.allTransactions.observe(viewLifecycleOwner) {
                    adapter.submitList(it)
                }
            } else {
                viewModel.search(query).observe(viewLifecycleOwner) {
                    adapter.submitList(it)
                }
            }
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        viewModel.expenses.observe(requireActivity()) {
            //Log.d("DATA_CHECK", "List size: ${list.size}")
            Log.d("CHECK", "Expenses size: ${it.size}")
            adapter.submitList(it)
        }

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(requireContext(), AddTransactionActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}