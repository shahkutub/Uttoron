package com.uttoron.model

data class QuizeResponse(
    val questions: List<Question>
)

data class Question(
    val options: List<Option>,
    val question: String,
    var isAnswered: Boolean,
    var isRightAnswere: Boolean
)

data class Option(
    val answer_status: Boolean,
    var isSelected: Boolean,

    val name: String
)