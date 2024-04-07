package com.example.themovieapp.utils

fun toDate(date: String): String{
    val dateList = date.split("-")
    val monthNumber = dateList[1].toInt()
    val month = when(monthNumber){
        1 -> "Jan"
        2 -> "Feb"
        3 -> "Mar"
        4 -> "Apr"
        5 -> "May"
        6 -> "Jun"
        7 -> "Jul"
        8 -> "Aug"
        9 -> "Sep"
        10 -> "Oct"
        11 -> "Nov"
        else -> "Dec"
    }
    return "$month ${dateList.get(2)}, ${dateList.get(0)}"

}