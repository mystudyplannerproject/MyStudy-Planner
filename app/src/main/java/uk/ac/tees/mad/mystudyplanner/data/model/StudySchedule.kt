package uk.ac.tees.mad.mystudyplanner.data.model

import com.google.firebase.Timestamp

data class StudySchedule(
    val id: String = "",
    val subject: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val selectedDate: String = "",
    val createdAt: Timestamp = Timestamp.now()
)