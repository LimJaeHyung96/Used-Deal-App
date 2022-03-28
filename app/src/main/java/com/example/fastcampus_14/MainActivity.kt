package com.example.fastcampus_14

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.fastcampus_14.chatlist.ChatListFragment
import com.example.fastcampus_14.home.HomeFragment
import com.example.fastcampus_14.mypage.MyPageFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val homeFragment = HomeFragment()
        val chatListFragment = ChatListFragment()
        val myPageFragment = MyPageFragment()


        replacementFragment(homeFragment) //초기엔 아무 것도 없어서 home 을 붙여줌

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> replacementFragment(homeFragment)
                R.id.chatList -> replacementFragment(chatListFragment)
                R.id.myPage -> replacementFragment(myPageFragment)
            }
            true
        }
    }

    private fun replacementFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction() //작업 시작
            .apply {
                replace(R.id.frameLayout, fragment)
                commit() //작업 끝
            }
    }
}