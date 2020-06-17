package com.example.roomdb01.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roomdb01.R
import com.example.roomdb01.room.AppDatabase
import com.example.roomdb01.list.NoteAdapter
import kotlinx.android.synthetic.main.fragment_list.view.*

// ListFragment 선언
class ListFragment : Fragment() {

    /* noteDao를 Lazy 키워드를 이용하여 처음 호출될때 초기화하도록 설정 */
    val noteDao by lazy { AppDatabase.getDatabase(requireContext()).noteDao() }
    
    //어댑터 생성
    val noteAdapter = NoteAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //fragment_list 인플레이션
        val rootView = inflater.inflate(R.layout.fragment_list, container, false)

        /* 어댑터 초기화*/
        rootView.list_notes.adapter = noteAdapter
        rootView.list_notes.layoutManager = LinearLayoutManager(requireContext())

        return rootView// 생성한 fragment_list 뷰 반환
        
    }//end of onCreateView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* ListFragment에서 LiveData observe
           - DB에서 트랜잭션(Transaction)이 발생하면 NoteAdapter에 데이터 변경을 알려 UI를 갱신
           - DetailFragment에서 노트를 수정 또는 삭제할 경우 ListFragment UI를 자동으로 갱신
             (DB에서 트랜잭션을 observe하지 않으면 DetailFragment에서 발생한 DB 변경 사항을
              ListFragment에서는 알 수 없음)
         */
        noteDao.selectNotes().observe(viewLifecycleOwner, Observer {
            noteAdapter.notes = it//어댑터에 변경된 note 전달
            noteAdapter.notifyDataSetChanged()//어댑터에 변경 공지
        })
    }//end of onViewCreated
    override fun onStart() {
        super.onStart()
    }
}

