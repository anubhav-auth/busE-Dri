package com.example.buse_dri

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object DatabaseHolder {
    lateinit var database: DatabaseReference

    init {
        database = FirebaseDatabase.getInstance().getReference("locations")
    }
}