package com.example.roomdb01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.example.roomdb01.R
import com.example.roomdb01.dialog.NoteCreateDialog
import com.google.android.material.bottomappbar.BottomAppBar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //nav_Graph에서 정의한 Destination action들을 수행하기 위한 NavController 가져오기
        val controller = findNavController(R.id.my_nav_host_fragment)

        /* DestinationChangedListener를 통해 destination이 바뀔 때 동적으로 아이콘 변경 */
        controller.addOnDestinationChangedListener { controller, destination, arguments ->

            /* destination에 따라 fab 아이콘을 동적으로 변경 */
            when (destination.id) {
                R.id.listFragment -> {
                    /* fabAlignmentMode: appbar의 정렬 위치를 설정
                     - Bottom appbar의 위치를 변경(중앙으로)*/
                    bottom_app_bar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                    fab_add_note.setImageResource(R.drawable.ic_plus)//아이콘 변경
                    /* List 화면에서 fab_add_note 버튼(+)을 누르면 새로 생성을 위한 CreateDialog 이동*/
                    fab_add_note.setOnClickListener {
                        NoteCreateDialog().show(
                            supportFragmentManager,//프래그먼트 매니저
                            null
                        )
                    }
                }//end of  R.id.listFragment
                R.id.detailFragment -> {
                    /* Bottom appbar의 위치를 변경(끝으로)*/
                    bottom_app_bar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                    fab_add_note.setImageResource(R.drawable.ic_back)//아이콘 변경
                    /* detail 화면에서 fab_add_note 버튼(undo)을 누르면 detailFragment를
                       BackStack에서 pop 시킨다
                       - 이전 list 화면으로 돌아감
                    */
                    fab_add_note.setOnClickListener { controller.popBackStack() }
                }//end of R.id.detailFragment
            }//end of when
        }//end of  controller.addOnDestinationChangedListener
    }//end of onCreate

    override fun onDestroy() {
        super.onDestroy()
    }
}

