package com.example.myapp.ui.decibel

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapp.R
import com.example.myapp.databinding.FragmentRecordDetailBinding
import kotlinx.coroutines.NonCancellable.start

class RecordDetailFragment : Fragment() {

    private var _binding : FragmentRecordDetailBinding? = null
    private val binding get() = _binding!!

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRecordDetailBinding.inflate(inflater, container, false)
        return binding.root
    }   // onCreateView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val soundFileUrl = arguments?.getString("sound_file")
        soundFileUrl?.let {
            prepareMediaPlayer(it)
        }

        val progressBar = binding.progressBar
        val playPauseButton = binding.playPauseButton

        playPauseButton.setOnClickListener {
            if (mediaPlayer == null) {                                  // MediaPlayer 초기화
                progressBar.visibility = View.VISIBLE                     // ProgressBar 보이게 설정
                val audioUrl = arguments?.getString("sound_file")    // 이전 프래그먼트에서 전달받은 URL
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(audioUrl)                             // 오디오 스트림의 위치 설정
                    setOnPreparedListener {
                        progressBar.visibility = View.INVISIBLE         // 준비가 끝나면 ProgressBar를 숨김
                        start()                                         // 오디오를 재생
                    }
                    setOnCompletionListener {                           // 오디오 재생이 완료되면 MediaPlayer를 해제, 버튼의 텍스트 변경
                        release()
                        mediaPlayer = null
                        playPauseButton.text = "재생"
                    }
                    prepareAsync()                                      // MediaPlayer를 준비. (비동기)
                }
                playPauseButton.text = "일시정지"
            }
            else if (mediaPlayer!!.isPlaying) {                         // 오디오가 재생 중이라면 일시정지
                mediaPlayer?.pause()
                playPauseButton.text = "다시 재생"
            }
            else {    // 오디오가 일시정지 상태라면 다시 재생
                mediaPlayer?.start()
                playPauseButton.text = "일시정지"
            }
        }
    } // onViewCreated

    private fun prepareMediaPlayer(soundFileUrl: String) {
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            setDataSource(soundFileUrl)
            setOnPreparedListener {
                start()
            }
            prepareAsync()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        mediaPlayer?.release()
        mediaPlayer = null

        _binding = null
    }


}