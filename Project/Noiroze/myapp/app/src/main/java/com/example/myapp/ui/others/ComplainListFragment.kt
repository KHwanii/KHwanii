package com.example.myapp.ui.others

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapp.R

import com.example.myapp.databinding.FragmentComplainListBinding
import com.example.myapp.ui.others.ViewModel.ComplainListAdapter
import com.example.myapp.ui.others.ViewModel.ComplainListViewModel
import com.example.myapp.ui.others.service.ComplainResult


class ComplainListFragment : Fragment() {

    private var _binding : FragmentComplainListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ComplainListViewModel by viewModels()
    private val adapter : ComplainListAdapter by lazy {
        ComplainListAdapter() { CompList ->
            onItemClick(CompList)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentComplainListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireContext().getSharedPreferences("LoginData", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", null)
        // Log.d("ComplainListFragment", "${token}")

        // ViewModel 설정
        viewModel.compList.observe(viewLifecycleOwner, Observer { compList ->
            adapter.updateData(compList.results)
            // Log.d("ComplainListAdapter", "${compList.created_date}")
        })

        viewModel.getCompList(token, 1)

        // RecyclerView 어댑터 및 레이아웃 매니저 설정
        binding.complainListView.adapter = this.adapter
        binding.complainListView.layoutManager = LinearLayoutManager(requireContext())


    }      // onViewCreated


    fun onItemClick(eachCompList: ComplainResult) {
        val bundle = Bundle()
        bundle.putString("title", eachCompList.title)
        bundle.putString("author", eachCompList.author)
        bundle.putString("created_date", eachCompList.created_date)
        bundle.putString("content", eachCompList.content)
        // Log.d("ComplainListFragment", "$bundle")
        findNavController().navigate(R.id.action_complainListFragment_to_complainDetailFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}