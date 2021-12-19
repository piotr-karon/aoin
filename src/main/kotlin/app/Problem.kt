package app

import app.Item

data class Problem(
    val weightLimit: Int,
    val items: Set<Item>,
    val fileName: String
)
