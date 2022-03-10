package com.example.core.utils

import com.example.core.domain.model.Games

object DataDummy {

    fun generateGameList(): List<Games> {
        val list = mutableListOf<Games>()
        for (i in 0..9) {
            list.add(
                Games(
                    i,
                    "Games $i",
                    "Image $i",
                    i.toDouble(),
                    "Lorem Ipsum $i",
                    "Adventure, Science",
                    false
                )
            )
        }
        return list
    }

}