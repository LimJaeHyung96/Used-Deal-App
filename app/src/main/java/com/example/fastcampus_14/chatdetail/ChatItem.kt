package com.example.fastcampus_14.chatdetail

data class ChatItem(
    val senderId : String,
    val message: String
) {
    constructor() : this ("","")
}
