package com.oxeai.health.worker

import androidx.health.connect.client.records.ExerciseSessionRecord
import java.time.Duration

class MoveMinutesCalculator {

    fun calculateTotalMoveMinutes(sessions: List<ExerciseSessionRecord>): Long {
        return sessions.sumOf { session ->
            Duration.between(session.startTime, session.endTime).toMinutes()
        }
    }

    fun calculateMoveMinutesWithDetails(sessions: List<ExerciseSessionRecord>): Map<String, Any> {
        val totalMinutes = calculateTotalMoveMinutes(sessions)
        val sessionDetails = sessions.map { session ->
            val duration = Duration.between(session.startTime, session.endTime).toMinutes()
            mapOf(
                "type" to getExerciseTypeName(session.exerciseType),
                "duration" to duration,
                "startTime" to session.startTime,
                "endTime" to session.endTime
            )
        }

        return mapOf(
            "totalMoveMinutes" to totalMinutes,
            "sessionCount" to sessions.size,
            "sessions" to sessionDetails
        )
    }

    private fun getExerciseTypeName(type: Int): String {
        return when (type) {
            ExerciseSessionRecord.EXERCISE_TYPE_RUNNING -> "Running"
            ExerciseSessionRecord.EXERCISE_TYPE_WALKING -> "Walking"
            ExerciseSessionRecord.EXERCISE_TYPE_BIKING -> "Biking"
            ExerciseSessionRecord.EXERCISE_TYPE_SWIMMING_POOL -> "Swimming"
            ExerciseSessionRecord.EXERCISE_TYPE_YOGA -> "Yoga"
            ExerciseSessionRecord.EXERCISE_TYPE_STRENGTH_TRAINING -> "Strength Training"
            ExerciseSessionRecord.EXERCISE_TYPE_HIGH_INTENSITY_INTERVAL_TRAINING -> "HIIT"
            else -> "Other Exercise"
        }
    }
}

