package com.example.calbard.ui.main

import java.sql.Timestamp

data class Picture (
    val img: String,
    val name: String,
    val taken: Timestamp,
    val author: String
)