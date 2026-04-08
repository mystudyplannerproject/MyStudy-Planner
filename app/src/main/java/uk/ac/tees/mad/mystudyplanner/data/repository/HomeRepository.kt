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

        val sorted = sessions.sortedBy {
            formatter.parse("${it.selectedDate} ${it.startTime}")?.time
                ?: Long.MAX_VALUE
        }

        val nextId = sorted.firstOrNull { session ->
            val endTime = formatter.parse(
                "${session.selectedDate} ${session.endTime}"
            )?.time ?: 0
            endTime > now
        }?.id

        return sorted.map { session ->

            val endTimeMillis = formatter.parse(
                "${session.selectedDate} ${session.endTime}"
            )?.time ?: 0

            session.copy(
                isNext = session.id == nextId,
                isCompleted = endTimeMillis < now
            )
        }
    }

    fun clearListener() {
        listener?.remove()
        listener = null
    }
}