package com.example.fastcampus_14.chatlist

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fastcampus_14.DBKey.Companion.CHILD_CHAT
import com.example.fastcampus_14.DBKey.Companion.DB_USERS
import com.example.fastcampus_14.R
import com.example.fastcampus_14.chatdetail.ChatRoomActivity
import com.example.fastcampus_14.databinding.FragmentChatListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatListFragment :  Fragment(R.layout.fragment_chat_list) {

    private var binding : FragmentChatListBinding? = null
    private lateinit var chatListAdapter : ChatListAdapter
    private val chatRoomList = mutableListOf<ChatListItem>()
    private lateinit var chatDB: DatabaseReference

    private val auth : FirebaseAuth by lazy {
        Firebase.auth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentChatListBinding = FragmentChatListBinding.bind(view)
        binding = fragmentChatListBinding

        chatRoomList.clear()

        chatListAdapter = ChatListAdapter(onItemClicked = { chatListItem ->
            context?.let{ context ->
                val intent = Intent(context, ChatRoomActivity::class.java)
                intent.putExtra("chatKey", chatListItem.key)
                startActivity(intent)
            }
        })

        fragmentChatListBinding.chatListRecyclerView.layoutManager = LinearLayoutManager(context)
        fragmentChatListBinding.chatListRecyclerView.adapter = chatListAdapter

        if(auth.currentUser == null) {
            return
        }

       chatDB = Firebase.database.reference.child(DB_USERS).child(auth.currentUser!!.uid).child(CHILD_CHAT)

        chatDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val model = it.getValue(ChatListItem::class.java)
                    model ?: return
                    chatRoomList.add(model)
                }

                chatListAdapter.submitList(chatRoomList)
                chatListAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    override fun onResume() {
        super.onResume()

        chatListAdapter.notifyDataSetChanged()
    }
}