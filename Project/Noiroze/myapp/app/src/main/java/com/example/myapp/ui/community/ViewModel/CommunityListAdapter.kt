package com.example.myapp.ui.community.ViewModel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.example.myapp.databinding.ItemCommunityListBinding
import com.example.myapp.ui.community.service.CommunityResult
import com.example.myapp.ui.community.service.getTimeDifference

// 어댑터에 아이템클릭 리스너 추가
class CommunityListAdapter(private val itemClickListener: (CommunityResult) -> Unit) : RecyclerView.Adapter<CommunityListAdapter.ViewHolder>() {
    private var commuList : List<CommunityResult> = emptyList()              // boardList 변수 초기화
    
    override fun getItemCount() = commuList.size                    // 게시판 갯수 카운트

    inner class ViewHolder(val binding: ItemCommunityListBinding) : // 뷰바인딩 적용
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): CommunityListAdapter.ViewHolder {
        val binding = ItemCommunityListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommunityListAdapter.ViewHolder, position: Int) {
        val eachBoardList = commuList[position]                             // 보드리스트
        holder.binding.boardCategory.text = eachBoardList.category
        holder.binding.boardTitle.text = eachBoardList.title
        holder.binding.boardAuthor.text = eachBoardList.author
        holder.binding.boardCreated.text = getTimeDifference(eachBoardList.createdDate)
        holder.binding.boardLike.text = eachBoardList.like.size.toString()

        holder.itemView.setOnClickListener {
            itemClickListener(eachBoardList)
        }
    }

    fun updateData(newList: List<CommunityResult>) {
        this.commuList = newList
        notifyDataSetChanged()
    }
}
