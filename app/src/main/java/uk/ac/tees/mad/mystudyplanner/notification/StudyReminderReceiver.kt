package uk.ac.tees.mad.mystudyplanner.notification

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import uk.ac.tees.mad.mystudyplanner.R

class StudyReminderReceiver : BroadcastReceiver() {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(context: Context, intent: Intent) {

        val subject = intent.getStringExtra("subject") ?: "Study Time"

        val notification = NotificationCompat.Builder(context, "study_reminder_channel")
            .setSmallIcon(R.drawable.ic_mystudyplanner_logo)
            .setContentTitle("Study Reminder")
            .setContentText("Time to study $subject")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        NotificationManagerCompat.from(context)
            .notify(subject.hashCode(), notification)
    }
}
