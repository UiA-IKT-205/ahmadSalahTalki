package com.example.superpiano.data

data class Note(val value:String, val start:Long, val end:Long){
    override fun toString(): String {
        val time:Long = (end - start)/ 1000000L // fra nano skunder til miliskunder
        return "$value,$time "
    }
}
