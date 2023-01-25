package com.cs191016.assignment1.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cs191016.assignment1.R
import com.cs191016.assignment1.ui.RecordDetailActivity
import com.cs191016.assignment1.ui.records.RecordAdapter
import com.cs191016.assignment1.ui.records.RecordsModel
import kotlinx.coroutines.launch
import java.io.Serializable


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Lookup the recyclerview in activity layout
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        val rvRecords = view.findViewById(R.id.rvRecords) as RecyclerView
        // Initialize records
        val recordsModel: RecordsModel =
            ViewModelProvider(requireActivity())[RecordsModel::class.java]
//      Create adapter passing in the sample user data
        lifecycleScope.launch {
            if (recordsModel.records.value == null) {
                recordsModel.loadRecords(context!!)
            }
            // Create adapter passing in the sample user data
            val adapter = RecordAdapter(
                recordsModel.records.value!!,
                ::onRecordClickHandler,
                ::onRecordDeleted,
                ::onRecordUpdated,
                context!!
            )
            // Attach the adapter to the recyclerview to populate items
            rvRecords.adapter = adapter
            // Set layout manager to position the items
            rvRecords.layoutManager = LinearLayoutManager(view.context)
        }
        // That's all!
        return view
    }

    private fun onRecordClickHandler(position: Int) {
        activity?.let {
            val intent = Intent(it, RecordDetailActivity::class.java)
            intent.putExtra(
                "record",
                ViewModelProvider(requireActivity())[RecordsModel::class.java].records.value!![position] as Serializable
            )
            it.startActivityFromFragment(this, intent, 1)
        }
    }

    private fun onRecordDeleted(position: Int) {
        ViewModelProvider(requireActivity())[RecordsModel::class.java].deleteRecord(position, context!!)
    }
    private fun onRecordUpdated(position: Int) {
        ViewModelProvider(requireActivity())[RecordsModel::class.java].markFavorite(position, context!!)
    }
}