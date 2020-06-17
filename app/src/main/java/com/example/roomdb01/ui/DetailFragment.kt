package com.example.roomdbex01.ui

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.roomdb01.R
import com.example.roomdb01.room.AppDatabase
import com.example.roomdb01.dialog.NoteCreateDialog
import kotlinx.android.synthetic.main.fragment_detail.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/* DetailFragment 선언  */
class DetailFragment : Fragment() {

    //noteDao 참조
    private val dao by lazy { AppDatabase.getDatabase(requireContext()).noteDao() }

    //fragment_detail 뷰를 생성(인플레이션)하여 반환
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }//end of onCreateView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* arguments에서 NOTE_ID 추출
           - NOTE_ID: 선택한 Note의 primary key
         */
        val noteId = arguments?.getLong("NOTE_ID") ?: kotlin.run { throw Error("NOTE_ID가 없습니다.") }

        /* DetailFragment에서 LiveData observe
           - DB에서 트랜잭션(Transaction)이 발생하면 UI를 갱신
        */
        dao.selectLiveNote(noteId).observe(viewLifecycleOwner, Observer {
            view.txt_detail_title.setText(it.noteTitle)
            view.txt_detail_content.setText(it.noteContent)
            it.noteImage?.let { uri -> view.img_profile.setImageURI(Uri.parse(uri)) }
        })

        /* Note 수정버튼 클릭하면 */
        view.btn_detail_edit.setOnClickListener {
            /* Note DialogFragment를 출력
            *  supportFragmntManager: Activity에서 Fragment를 만들 때
            *  childFragmentManager:  Fragment에서 다른 Fragment를 만들 때(DetailFragment에서 DialFragment 만들 때)
            *  */
            NoteCreateDialog().show(childFragmentManager, noteId.toString())
        }

        /* Note 삭제버튼 */
        view.btn_detail_delete.setOnClickListener {
            /* 삭제*/
            lifecycleScope.launch(Dispatchers.IO){
                dao.deleteNote(noteId)
            }
            //viewModel.deleteNote(noteId, requireContext())
            /*뒤로가기
            * popBackStack(): 뒤로가기
            * */
            findNavController().popBackStack()
        }
    }//end of onViewCreated
}
