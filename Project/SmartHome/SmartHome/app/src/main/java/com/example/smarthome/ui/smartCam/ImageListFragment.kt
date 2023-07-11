package com.example.smarthome.ui.smartCam

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.smarthome.R

import com.example.smarthome.databinding.FragmentImgListBinding
import com.example.smarthome.databinding.ItemImgFileBinding
import com.example.smarthome.ui.smartCam.data.Images
import com.example.smarthome.ui.smartCam.data.Result
import com.example.smarthome.ui.smartCam.service.Img

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImageListFragment : Fragment() {

    private var imageList = mutableListOf<Result>()

    // private var imageList = mutableListOf<Result>()   // 이미지들에 대한 list var로 해야지 이미지 추가 가능
    // val로 하면 마지막으로 가져온 이미지만 유지돼

    private var _binding: FragmentImgListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentImgListBinding.inflate(inflater, container, false)   // 인플레이트 : xml 파일을 메모리에 객체로 생성하는 과정
        // ui를 정의하기 위해 xml 파일 사용, 액티비티나 프래그먼트에서 레이아웃을 inflate해서 실제 ui 객체로 변환
        return binding.root   // view를 반환
    }    // onCreateView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.listView.apply {                  // recyclerView 초기화
            adapter = ImageListAdapter()                              // RecyclerView에 adapter(MyAdapter) 연결
            layoutManager = LinearLayoutManager(requireContext())   // 레이아웃 매니저 사용
        }

        // api 호출 기본 골격
        // retrofit으로 서버에서 image 목록을 가져와
        val imageFileList = Img.service.requestImageList(page=1)   // imageFileList변수를 선언후, Img.service.requestImageList 에서 첫번째 페이지를 가져오기.
        imageFileList.enqueue(object : Callback<Images> {          // retrofit2의 Callback 으로 <Images> 에서 데이터들을 Queue 로 가져옴
                @SuppressLint("NotifyDataSetChanged")          // 경고 무시 코드.
                override fun onResponse(call: Call<Images>, response: Response<Images>) {     // Images의 데이터를 Call 하고, Response로 응답을 확인
                    if(response.isSuccessful) {          // 성공했을 때. 즉, 네트워크 응답으로 데이터가 앱으로 전달될 때.
                        val images = response.body()                      // images 라는 변수를 만들고, 여기에 응답에서 가져온 이미지 데이터를 할당
                        imageList.clear()                                // 이미지 목록 초기화
                        imageList.addAll(images!!.results)                // 이미지를 추가할 때마다 ImageList에 이미지 추가. images!!.results는 이미지 데이터의 결과물들을 말함.

                        if (isAdded && _binding != null) {   // 프래그먼트가 실행 및 활성화되고(액티비티에 추가되었는지 확인), 바인딩이 null이 아닐 때
                            binding.listView.adapter?.notifyDataSetChanged()  // adapter에 변경사항이 있음을 알려줌
                        }
                    }
                }

                override fun onFailure(call: Call<Images>, t: Throwable) {
                    Log.e("네트워크 요청 실패", t.toString())    // Log의 error 출력
                }
            })
    }    // onViewCreated


    // 내부 클래스 Adapter 정의 -> 이미지 파일 리스트 리사이클러 뷰 어댑터. 어뎁터는 뷰홀더라는 것을 필요로 하고, onCreate와 onBind를 반드시 사용해야 함.
    inner class ImageListAdapter(): RecyclerView.Adapter <ImageListAdapter.SmartCamViewHolder>() {

        inner class SmartCamViewHolder(val binding: ItemImgFileBinding) :
            RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmartCamViewHolder {    // 어댑터에 사용할 SmartCamㅍVewHolder 생성
            val binding = ItemImgFileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return SmartCamViewHolder(binding)                  // ItemImgFileBinding 에 붙이기.
        }

        override fun getItemCount() : Int = imageList.size

        override fun onBindViewHolder(holder: SmartCamViewHolder, position: Int) {  // 각 항목에 해당하는 데이터를 ViewHolder에 바인딩
            val imageList = imageList[position]
            holder.binding.fileName.text = imageList.fileName

            Glide.with(holder.itemView.context)           // glide로 itemView에 이미지파일을 붙이기
                .load(imageList.imgFile)                         // glide로 이미지 로드하고
                .diskCacheStrategy(DiskCacheStrategy.ALL)  // 디스크 캐시를 설정 후,
                .into(holder.binding.imagePreView)         // 로드한 이미지 붙이기

            holder.itemView.setOnClickListener {       // 목록에서 항목 클릭시, 여기서 _는 itemView의 View인데, 사용하지 않으므로 _로 처리.
                onItemClick(imageList, position)                    // onItemClick 함수 사용. it은 위쪽의 imageList의 Result, position은 아이템의 위치.
            }
        }
    }  // 여기까지 MyAdapter 클래스

    // 목록에서 항목을 클릭했을 때 호출되는 함수
    fun onItemClick(imgFile: Result, position: Int) {   // Result에서 가져온 imgFile이 목표, 몇번째 파일인지 position으로 확인을 함.
        val imgUrl = imgFile.imgFile      // 클릭한 항목의 url을 추출하여 imgUrl로 선언
        val bundle = Bundle()         // 클릭한 항목에 대해 응답으로 전해줄 데이터 덩어리를 Bundle객체로 생성함.
        bundle.putString(ARG_URL, imgUrl)   // 프래그먼트 간에 데이터 전달을 위해 Bundle에 ARG_URL과 imgUrl 설정
        findNavController().navigate(R.id.action_ImageListFragment_to_imageDetailFragment, bundle)
    }


    override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
    }

}