package com.example.roomdb01.dialog

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.roomdb01.room.NoteEntity
import com.example.roomdb01.R
import com.example.roomdb01.room.AppDatabase
import com.kroegerama.imgpicker.BottomSheetImagePicker
import com.kroegerama.imgpicker.ButtonType
import kotlinx.android.synthetic.main.dialog_note_create.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/* Create 다이얼로그 프래그먼트
   - BottomSheetImagePicker.OnImagesSelectedListener 구현
   - ImagePicker에서 사진을 선택하면 onImagesSelected() 콜백 함수 실행
*/
class NoteCreateDialog : DialogFragment(), BottomSheetImagePicker.OnImagesSelectedListener {

    /* note 객체 생성 및 초기화. */
    private var note =
        NoteEntity(
            noteContent = "",
            noteTitle = "",
            noteImage = null
        )

    //noteDao 참조
    private val dao by lazy { AppDatabase.getDatabase(requireContext()).noteDao() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /* 추가/수정 다이얼로그 위한 레이아웃(dialog_note_create.xml) inflate */
        val rootView = inflater.inflate(R.layout.dialog_note_create, container, false)

        /* 사진을 누를경우 사진을 선택하게해주고 onImagesSelect로 받게되는 라이브러리를 사용*/
        rootView.img_profile.setOnClickListener {
            //사용한 플러그인
            //https://github.com/kroegerama/bottomsheet-imagepicker
            BottomSheetImagePicker.Builder("빌드")
                .cameraButton(ButtonType.Button)//카메라 버튼 표시
                .galleryButton(ButtonType.Button)//갤러리 버튼 표시
                .singleSelectTitle(R.string.select_title) //제목글
                .requestTag("single")//1개만 선택하게할 경우 single
                .show(childFragmentManager)
        }
        return rootView
    }//end of onCreateView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* 만약 Tag값(Note id)이 있으면 생성이 아니라 수정
           - 따라서 수정을 위해 데이터를 불러와야 함
        */
        tag?.toLongOrNull()?.let { noteId ->
            viewLifecycleOwner.lifecycleScope.launch {
                /* 미리 변수를 선언 */
                var savedNote: NoteEntity? = null
                /* 노트를 쿼리.*/
                withContext(Dispatchers.IO) {
                    savedNote = dao.selectNote(noteId)
                }
                /* 노트가 존재한다면 note를 변경*/
                savedNote?.let {
                    note = it //쿼리한 노트 객체를 note에 저장
                    view.txt_title.setText(it.noteTitle)
                    view.txt_content.setText(it.noteContent)
                    it.noteImage?.let { noteImage ->
                        view.img_profile.setImageURI(Uri.parse(noteImage))
                    }
                }

            }
        }//end of let

        //수정하고 저장하기 버튼을 클릭한 경우(DB에 수정사항 저장)
        view.btn_save.setOnClickListener {
            //입력한 noteTitle, noteContent 가져와서 변수에 할당
            note.noteTitle = view.txt_title.text.toString()
            note.noteContent = view.txt_content.text.toString()

            /* 제목과 내용이 다 있는지를 검증 */
            if (note.noteTitle.isBlank() && note.noteContent.isBlank()) {
                Toast.makeText(requireContext(), "제목과 내용을 입력해주세요", Toast.LENGTH_LONG).show()
            } else {
                /* 자동 스코프에 맞추어 코루틴을 실행*/
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    val note = NoteEntity(
                        noteIdx = note.noteIdx,
                        noteTitle = note.noteTitle,
                        noteContent = note.noteContent,
                        noteImage = note.noteImage
                    )
                    dao.insertNotes(note)//DB에 저장
                    dismiss()//다이얼로드 종료
                }
            }
        }//end of view.btn_save.setOnClickListener
    }//end of onViewCreated

    //이미지 피커에서 사진을 선택했을 때
    override fun onImagesSelected(uris: List<Uri>, tag: String?) {
        /* 사진이 선택되었을때 uris는 array 길이가 1개인 형태로 들어온다 */
        if (uris.isNotEmpty()) {
            /* 추후 디비저장을 위해 변수를 할당*/
            Log.d("image" , "$uris")
            note.noteImage = uris[0].toString()//path를 db에 저장
            view?.img_profile?.setImageURI(uris[0]);//사진을 뷰에 설정
        }
    }//end of onImagesSelected
}