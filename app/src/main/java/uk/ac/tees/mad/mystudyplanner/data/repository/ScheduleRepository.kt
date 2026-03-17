package uk.ac.tees.mad.mystudyplanner.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import uk.ac.tees.mad.mystudyplanner.data.model.StudySchedule

class ScheduleRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private fun userSchedulesRef() =
        firestore.collection("users")
            .document(auth.currentUser!!.uid)
            .collection("schedules")

    fun addSchedule(schedule: StudySchedule, onResult: (Boolean) -> Unit) {
        val doc = userSchedulesRef().document()
        doc.set(schedule.copy(id = doc.id))
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    fun updateSchedule(schedule: StudySchedule, onResult: (Boolean) -> Unit) {
        userSchedulesRef()
            .document(schedule.id)
            .set(schedule)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    fun deleteSchedule(scheduleId: String, onResult: (Boolean) -> Unit) {
        userSchedulesRef()
            .document(scheduleId)
            .delete()
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }
}