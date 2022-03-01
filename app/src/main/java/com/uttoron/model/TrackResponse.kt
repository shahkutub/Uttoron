package com.uttoron.model

class TrackResponse : ArrayList<TrackResponseItem>()

data class TrackResponseItem(
    val created_at: Any,
    val id: Int,
    val track_no: Int,
    val updated_at: String
)