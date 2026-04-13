package uk.ac.tees.mad.mystudyplanner.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import uk.ac.tees.mad.mystudyplanner.presentation.components.StudySessionUiState
import java.text.SimpleDateFormat
import java.util.*

class HomeRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private var listener: ListenerRegistration? = null

    fun deleteSchedule(scheduleId: String) {
        val userId = auth.currentUser?.uid ?: return

        firestore.collection("users")
            .document(userId)
            .collection("schedules")
            .document(scheduleId)
            .delete()
    }

    fun observeTodaySchedules(
        onResult: (List<StudySessionUiState>) -> Unit
    ) {

        val userId = auth.currentUser?.uid ?: return

        val today = SimpleDateFormat(
            "dd MMM yyyy",
            Locale.getDefault()
        ).format(Date())

        listener?.remove()

        listener = firestore.collection("users")
            .document(userId)
            .collection("schedules")
            .whereEqualTo("selectedDate", today)
            .addSnapshotListener { snapshot, _ ->

                if (snapshot == null) return@addSnapshotListener

                val sessions = snapshot.documents.mapNotNull { doc ->

                    StudySessionUiState(
                        id = doc.getString("id") ?: doc.id,
                        subject = doc.getString("subject") ?: "",
                        startTime = doc.getString("startTime") ?: "",
                        endTime = doc.getString("endTime") ?: "",
                        selectedDate = doc.getString("selectedDate") ?: ""
                    )
                }

                onResult(markNextSession(sessions))
            }
    }

    private fun markNextSession(
        sessions: List<StudySessionUiState>
    ): List<StudySessionUiState> {

        if (sessions.isEmpty()) return sessions

        val now = System.currentTimeMillis()

        val formatter = SimpleDateFormat(
            "dd MMM yyyy hh:mm a",
            Locale.getDefault()
        )

        val mapped = sessions.map { session ->

            val startMillis = formatter.parse(
                "${session.selectedDate} ${session.startTime}"
            )?.time ?: Long.MAX_VALUE

            val endMillis = formatter.parse(
                "${session.selectedDate} ${session.endTime}"
            )?.time ?: 0L

            val isCompleted = endMillis < now

            Triple(session, startMillis, isCompleted)
        }

        val active = mapped.filter { !it.third }
        val completed = mapped.filter { it.third }

        val sortedActive = active.sortedBy { it.second }

        val nextId = sortedActive.firstOrNull()?.first?.id

        val finalActive = sortedActive.map {
            it.first.copy(
                isNext = it.first.id == nextId,
                isCompleted = false
            )
        }

        val finalCompleted = completed
            .sortedBy { it.second }
            .map {
                it.first.copy(
                    isNext = false,
                    isCompleted = true
                )
            }

        return finalActive + finalCompleted
    }

    fun clearListener() {
        listener?.remove()
        listener = null
    }
}