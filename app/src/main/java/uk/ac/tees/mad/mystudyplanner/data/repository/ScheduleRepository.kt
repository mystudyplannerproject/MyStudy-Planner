package uk.ac.tees.mad.mystudyplanner.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import uk.ac.tees.mad.mystudyplanner.data.model.StudySchedule

class ScheduleRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private var listener: ListenerRegistration? = null

    private fun userSchedulesRef() =
        firestore.collection("users")
            .document(auth.currentUser!!.uid)
            .collection("schedules")

    fun observeSchedules(onDataChange: (List<StudySchedule>) -> Unit) {

        if (listener != null) return

        listener = userSchedulesRef()
            .addSnapshotListener { snapshot, error ->

                if (error != null) return@addSnapshotListener

                if (snapshot != null) {

                    val schedules = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(StudySchedule::class.java)
                    }

                    onDataChange(schedules)
                }
            }
    }

    fun clearListener() {
        listener?.remove()
        listener = null
    }

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
