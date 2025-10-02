package com.oxeai.health.worker

import androidx.health.connect.client.records.ExerciseSessionRecord
import java.time.Duration

class HeartPointsCalculator {

    fun calculateTotalHeartPoints(sessions: List<ExerciseSessionRecord>): Double {
        return sessions.sumOf { session ->
            val minutes = Duration.between(session.startTime, session.endTime).toMinutes()
            calculatePointsForExercise(session.exerciseType, minutes)
        }
    }

    fun calculateHeartPointsWithDetails(sessions: List<ExerciseSessionRecord>): Map<String, Any> {
        var totalPoints = 0.0
        val sessionDetails = sessions.map { session ->
            val minutes = Duration.between(session.startTime, session.endTime).toMinutes()
            val points = calculatePointsForExercise(session.exerciseType, minutes)
            totalPoints += points

            mapOf(
                "type" to getExerciseTypeName(session.exerciseType),
                "duration" to minutes,
                "points" to points,
                "intensity" to getIntensityLevel(session.exerciseType),
                "startTime" to session.startTime,
                "endTime" to session.endTime
            )
        }

        return mapOf(
            "totalHeartPoints" to totalPoints,
            "sessionCount" to sessions.size,
            "sessions" to sessionDetails
        )
    }

    private fun calculatePointsForExercise(exerciseType: Int, minutes: Long): Double {
        return when {
            isVigorousActivity(exerciseType) -> minutes * 2.0  // 2 points per minute
            isModerateActivity(exerciseType) -> minutes * 1.0  // 1 point per minute
            else -> 0.0
        }
    }

    private fun isVigorousActivity(exerciseType: Int): Boolean {
        return exerciseType in setOf(
            ExerciseSessionRecord.EXERCISE_TYPE_RUNNING,
            ExerciseSessionRecord.EXERCISE_TYPE_RUNNING_TREADMILL,
            ExerciseSessionRecord.EXERCISE_TYPE_BIKING,
            ExerciseSessionRecord.EXERCISE_TYPE_SWIMMING_POOL,
            ExerciseSessionRecord.EXERCISE_TYPE_HIGH_INTENSITY_INTERVAL_TRAINING,
            ExerciseSessionRecord.EXERCISE_TYPE_ROWING_MACHINE,
            ExerciseSessionRecord.EXERCISE_TYPE_STAIR_CLIMBING,
            ExerciseSessionRecord.EXERCISE_TYPE_ELLIPTICAL
        )
    }

    private fun isModerateActivity(exerciseType: Int): Boolean {
        return exerciseType in setOf(
            ExerciseSessionRecord.EXERCISE_TYPE_WALKING,
            ExerciseSessionRecord.EXERCISE_TYPE_YOGA,
            ExerciseSessionRecord.EXERCISE_TYPE_PILATES,
            ExerciseSessionRecord.EXERCISE_TYPE_STRENGTH_TRAINING,
            ExerciseSessionRecord.EXERCISE_TYPE_STRETCHING,
            ExerciseSessionRecord.EXERCISE_TYPE_BADMINTON,
            ExerciseSessionRecord.EXERCISE_TYPE_BASKETBALL,
            ExerciseSessionRecord.EXERCISE_TYPE_DANCING,
            ExerciseSessionRecord.EXERCISE_TYPE_GOLF
        )
    }

    private fun getIntensityLevel(exerciseType: Int): String {
        return when {
            isVigorousActivity(exerciseType) -> "Vigorous"
            isModerateActivity(exerciseType) -> "Moderate"
            else -> "Light"
        }
    }

    private fun getExerciseTypeName(type: Int): String {
        return when (type) {
            ExerciseSessionRecord.EXERCISE_TYPE_RUNNING -> "Running"
            ExerciseSessionRecord.EXERCISE_TYPE_RUNNING_TREADMILL -> "Treadmill Running"
            ExerciseSessionRecord.EXERCISE_TYPE_WALKING -> "Walking"
            ExerciseSessionRecord.EXERCISE_TYPE_BIKING -> "Biking"
            ExerciseSessionRecord.EXERCISE_TYPE_SWIMMING_POOL -> "Swimming"
            ExerciseSessionRecord.EXERCISE_TYPE_YOGA -> "Yoga"
            ExerciseSessionRecord.EXERCISE_TYPE_PILATES -> "Pilates"
            ExerciseSessionRecord.EXERCISE_TYPE_STRENGTH_TRAINING -> "Strength Training"
            ExerciseSessionRecord.EXERCISE_TYPE_STRETCHING -> "Stretching"
            ExerciseSessionRecord.EXERCISE_TYPE_HIGH_INTENSITY_INTERVAL_TRAINING -> "HIIT"
            ExerciseSessionRecord.EXERCISE_TYPE_ROWING_MACHINE -> "Rowing"
            ExerciseSessionRecord.EXERCISE_TYPE_STAIR_CLIMBING -> "Stair Climbing"
            ExerciseSessionRecord.EXERCISE_TYPE_ELLIPTICAL -> "Elliptical"
            ExerciseSessionRecord.EXERCISE_TYPE_BADMINTON -> "Badminton"
            ExerciseSessionRecord.EXERCISE_TYPE_BASKETBALL -> "Basketball"
            ExerciseSessionRecord.EXERCISE_TYPE_DANCING -> "Dancing"
            ExerciseSessionRecord.EXERCISE_TYPE_GOLF -> "Golf"
            else -> "Other Exercise"
        }
    }
}