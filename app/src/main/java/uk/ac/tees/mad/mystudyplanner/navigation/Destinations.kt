package uk.ac.tees.mad.mystudyplanner.navigation

import kotlinx.serialization.Serializable

@Serializable
object Splash

@Serializable
object Home

@Serializable
object Auth

@Serializable
data class Schedule(
    val scheduleId: String? = null
)