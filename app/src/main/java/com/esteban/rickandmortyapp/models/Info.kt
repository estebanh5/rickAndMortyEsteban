package com.esteban.rickandmortyapp.models

import java.io.Serializable

data class Info(
    val count: Int,
    val next: String,
    val pages: Int,
    val prev: Any
): Serializable