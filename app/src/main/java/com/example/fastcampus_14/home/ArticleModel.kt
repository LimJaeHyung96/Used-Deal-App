package com.example.fastcampus_14.home

data class ArticleModel(
    val sellerId: String,
    val title: String,
    val createdAt: Long,
    val price: String,
    val imageUrl: String
){
    //Firebase realtime database 에 그대로 Model 클래스를 사용하기 위해선 빈 생성자가 필수로 있어야 한다
    constructor() : this("", "", 0, "", "")
}