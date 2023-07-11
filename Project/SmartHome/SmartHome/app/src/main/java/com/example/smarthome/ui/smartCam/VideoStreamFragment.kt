package com.example.smarthome.ui.smartCam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.smarthome.R
import com.example.smarthome.databinding.FragmentVideoStreamBinding

class VideoStreamFragment : Fragment() {

    private lateinit var webView: WebView
    private lateinit var btnImageList: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_video_stream, container, false)

        webView = view.findViewById(R.id.webView)
        btnImageList = view.findViewById(R.id.btnImageList)

        webView.settings.javaScriptEnabled = true // 자바스크립트 허용

        // WebView 설정
        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = WebViewClient()

        // URL 로드
        val url = "http://192.168.45.15:8000/mjpeg/?mode=stream"
        webView.loadUrl(url)

        // 버튼 클릭 시 ImageListFragment로 이동
        btnImageList.setOnClickListener {
            findNavController().navigate(R.id.action_VideoStreamFragment_to_ImageListFragment)
        }

        return view
    }
}