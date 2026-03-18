package uk.ac.tees.mad.mystudyplanner.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import uk.ac.tees.mad.mystudyplanner.presentation.home.StudySessionUiState

class HomeRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private var listener: ListenerRegistration? = null

    fun observeTodaySchedules(
        onResult: (List<StudySessionUiState>) -> Unit
    ) {
        val userId = auth.currentUser?.uid ?: return

        listener?.remove()

        listener = firestore.collection("users")
            .document(userId)
            .collection("schedules")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot == null) return@addSnapshotListener

                val sessions = snapshot.documents.mapNotNull { doc ->
                    val subject = doc.getString("subject") ?: return@mapNotNull null
                    val start = doc.getString("startTime") ?: return@mapNotNull null
                    val end = doc.getString("endTime") ?: return@mapNotNull null

                    StudySessionUiState(
                        subject = subject,
                        startTime = start,
                        endTime = end
                    )
                }.sortedBy { it.startTime }

                val updatedSessions = markNextSession(sessions)
                onResult(updatedSessions)
            }
    }

    private fun markNextSession(
        sessions: List<StudySessionUiState>
    ): List<StudySessionUiState> {
        if (sessions.isEmpty()) return sessions

        return sessions.mapIndexed { index, session ->
            session.copy(isNext = index == 0)
        }
    }

    fun clearListener() {
        listener?.remove()
        listener = null
    }
}