package com.example.myapp.ui.others.ViewModel

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.databinding.ItemComplainListBinding
import com.example.myapp.login.monthDayChange
import com.example.myapp.ui.others.service.ComplainResult

// 민원접수 리사이클러뷰 어댑터. itemClickListener로 Detail로 이동 구현
class ComplainListAdapter(private val itemClickListener: (ComplainResult) -> Unit) : RecyclerView.Adapter<ComplainListAdapter.ViewHolder>() {

    private var compList : List<ComplainResult> = emptyList()       // compList 변수 초기화

    override fun getItemCount() = compList.size

    inner class ViewHolder(val binding: ItemComplainListBinding) :
            RecyclerView.ViewHolder(binding.root)                   // ViewHolder 설정

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemComplainListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }                                                               // ViewHolder에 바인딩 해주기

    override fun onBindViewHolder(holder: ComplainListAdapter.ViewHolder, position : Int) {
        // Log.d("ComplainListAdapter", "onBindViewHolder called")
        val eachCompList = compList[position]

        holder.binding.complainTitle.text = eachCompList.title
        holder.binding.complainCreated.text = monthDayChange(eachCompList.created_date)
        // Log.d("ComplainListAdapter", "${eachCompList.created_date}")

        holder.itemView.setOnClickListener {
            itemClickListener(eachCompList)
        }
    }

    fun updateData(newList: List<ComplainResult>) {
        this.compList = newList
        notifyDataSetChanged()
    }
}