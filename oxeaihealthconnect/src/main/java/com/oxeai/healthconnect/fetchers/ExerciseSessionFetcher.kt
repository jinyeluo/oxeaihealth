package com.oxeai.healthconnect.fetchers

import android.content.Context
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.response.ReadRecordsResponse
import com.oxeai.healthconnect.models.DataConfidence
import com.oxeai.healthconnect.models.DataSource
import com.oxeai.healthconnect.models.Exercise
import com.oxeai.healthconnect.models.ExerciseData
import com.oxeai.healthconnect.models.ExerciseType
import com.oxeai.healthconnect.models.Metadata
import java.util.UUID

class ExerciseSessionFetcher(context: Context, userId: UUID) :
    HealthDataFetcher<ExerciseSessionRecord>(context, userId, ExerciseSessionRecord::class) {

    override fun processRecords(response: ReadRecordsResponse<ExerciseSessionRecord>): ExerciseData {
        val exerciseData = ExerciseData(
            userId = userId,
            timestamp = endTime,
            source = DataSource.GOOGLE,
            metadata = Metadata(
                devices = getDeviceModels(response),
                confidence = DataConfidence.HIGH
            )
        )
        response.records.forEach { record ->
            exerciseData.measurements.add(
                Exercise(
                    // a lot of exercise's details are skipped here
                    exerciseType = record.exerciseType.fromInt(),
                    startTime = record.startTime,
                    endTime = record.endTime,
                    notes = record.notes,
                    title = record.title
                )
            )
        }
        return exerciseData
    }

    companion object {
        fun Int.fromInt(): ExerciseType {
            return when (this) {
                ExerciseSessionRecord.EXERCISE_TYPE_BADMINTON -> ExerciseType.BADMINTON
                ExerciseSessionRecord.EXERCISE_TYPE_BASEBALL -> ExerciseType.BASEBALL
                ExerciseSessionRecord.EXERCISE_TYPE_BASKETBALL -> ExerciseType.BASKETBALL
                ExerciseSessionRecord.EXERCISE_TYPE_BIKING -> ExerciseType.BIKING
                ExerciseSessionRecord.EXERCISE_TYPE_BIKING_STATIONARY -> ExerciseType.BIKING_STATIONARY
                ExerciseSessionRecord.EXERCISE_TYPE_BOOT_CAMP -> ExerciseType.BOOT_CAMP
                ExerciseSessionRecord.EXERCISE_TYPE_BOXING -> ExerciseType.BOXING
                ExerciseSessionRecord.EXERCISE_TYPE_CALISTHENICS -> ExerciseType.CALISTHENICS
                ExerciseSessionRecord.EXERCISE_TYPE_CRICKET -> ExerciseType.CRICKET
                ExerciseSessionRecord.EXERCISE_TYPE_DANCING -> ExerciseType.DANCING
                ExerciseSessionRecord.EXERCISE_TYPE_ELLIPTICAL -> ExerciseType.ELLIPTICAL
                ExerciseSessionRecord.EXERCISE_TYPE_EXERCISE_CLASS -> ExerciseType.EXERCISE_CLASS
                ExerciseSessionRecord.EXERCISE_TYPE_FENCING -> ExerciseType.FENCING
                ExerciseSessionRecord.EXERCISE_TYPE_FOOTBALL_AMERICAN -> ExerciseType.FOOTBALL_AMERICAN
                ExerciseSessionRecord.EXERCISE_TYPE_FOOTBALL_AUSTRALIAN -> ExerciseType.FOOTBALL_AUSTRALIAN
                ExerciseSessionRecord.EXERCISE_TYPE_FRISBEE_DISC -> ExerciseType.FRISBEE_DISC
                ExerciseSessionRecord.EXERCISE_TYPE_GOLF -> ExerciseType.GOLF
                ExerciseSessionRecord.EXERCISE_TYPE_GUIDED_BREATHING -> ExerciseType.GUIDED_BREATHING
                ExerciseSessionRecord.EXERCISE_TYPE_GYMNASTICS -> ExerciseType.GYMNASTICS
                ExerciseSessionRecord.EXERCISE_TYPE_HANDBALL -> ExerciseType.HANDBALL
                ExerciseSessionRecord.EXERCISE_TYPE_HIGH_INTENSITY_INTERVAL_TRAINING -> ExerciseType.HIGH_INTENSITY_INTERVAL_TRAINING
                ExerciseSessionRecord.EXERCISE_TYPE_HIKING -> ExerciseType.HIKING
                ExerciseSessionRecord.EXERCISE_TYPE_ICE_HOCKEY -> ExerciseType.ICE_HOCKEY
                ExerciseSessionRecord.EXERCISE_TYPE_ICE_SKATING -> ExerciseType.ICE_SKATING
                ExerciseSessionRecord.EXERCISE_TYPE_MARTIAL_ARTS -> ExerciseType.MARTIAL_ARTS
                ExerciseSessionRecord.EXERCISE_TYPE_PADDLING -> ExerciseType.PADDLING
                ExerciseSessionRecord.EXERCISE_TYPE_PARAGLIDING -> ExerciseType.PARAGLIDING
                ExerciseSessionRecord.EXERCISE_TYPE_PILATES -> ExerciseType.PILATES
                ExerciseSessionRecord.EXERCISE_TYPE_RACQUETBALL -> ExerciseType.RACQUETBALL
                ExerciseSessionRecord.EXERCISE_TYPE_ROCK_CLIMBING -> ExerciseType.ROCK_CLIMBING
                ExerciseSessionRecord.EXERCISE_TYPE_ROLLER_HOCKEY -> ExerciseType.ROLLER_HOCKEY
                ExerciseSessionRecord.EXERCISE_TYPE_ROWING -> ExerciseType.ROWING
                ExerciseSessionRecord.EXERCISE_TYPE_ROWING_MACHINE -> ExerciseType.ROWING_MACHINE
                ExerciseSessionRecord.EXERCISE_TYPE_RUGBY -> ExerciseType.RUGBY
                ExerciseSessionRecord.EXERCISE_TYPE_RUNNING -> ExerciseType.RUNNING
                ExerciseSessionRecord.EXERCISE_TYPE_RUNNING_TREADMILL -> ExerciseType.RUNNING_TREADMILL
                ExerciseSessionRecord.EXERCISE_TYPE_SAILING -> ExerciseType.SAILING
                ExerciseSessionRecord.EXERCISE_TYPE_SCUBA_DIVING -> ExerciseType.SCUBA_DIVING
                ExerciseSessionRecord.EXERCISE_TYPE_SKATING -> ExerciseType.SKATING
                ExerciseSessionRecord.EXERCISE_TYPE_SKIING -> ExerciseType.SKIING
                ExerciseSessionRecord.EXERCISE_TYPE_SNOWBOARDING -> ExerciseType.SNOWBOARDING
                ExerciseSessionRecord.EXERCISE_TYPE_SNOWSHOEING -> ExerciseType.SNOWSHOEING
                ExerciseSessionRecord.EXERCISE_TYPE_SOCCER -> ExerciseType.SOCCER
                ExerciseSessionRecord.EXERCISE_TYPE_SOFTBALL -> ExerciseType.SOFTBALL
                ExerciseSessionRecord.EXERCISE_TYPE_SQUASH -> ExerciseType.SQUASH
                ExerciseSessionRecord.EXERCISE_TYPE_STAIR_CLIMBING -> ExerciseType.STAIR_CLIMBING
                ExerciseSessionRecord.EXERCISE_TYPE_STAIR_CLIMBING_MACHINE -> ExerciseType.STAIR_CLIMBING_MACHINE
                ExerciseSessionRecord.EXERCISE_TYPE_STRENGTH_TRAINING -> ExerciseType.STRENGTH_TRAINING
                ExerciseSessionRecord.EXERCISE_TYPE_STRETCHING -> ExerciseType.STRETCHING
                ExerciseSessionRecord.EXERCISE_TYPE_SURFING -> ExerciseType.SURFING
                ExerciseSessionRecord.EXERCISE_TYPE_SWIMMING_OPEN_WATER -> ExerciseType.SWIMMING_OPEN_WATER
                ExerciseSessionRecord.EXERCISE_TYPE_SWIMMING_POOL -> ExerciseType.SWIMMING_POOL
                ExerciseSessionRecord.EXERCISE_TYPE_TABLE_TENNIS -> ExerciseType.TABLE_TENNIS
                ExerciseSessionRecord.EXERCISE_TYPE_TENNIS -> ExerciseType.TENNIS
                ExerciseSessionRecord.EXERCISE_TYPE_VOLLEYBALL -> ExerciseType.VOLLEYBALL
                ExerciseSessionRecord.EXERCISE_TYPE_WALKING -> ExerciseType.WALKING
                ExerciseSessionRecord.EXERCISE_TYPE_WATER_POLO -> ExerciseType.WATER_POLO
                ExerciseSessionRecord.EXERCISE_TYPE_WEIGHTLIFTING -> ExerciseType.WEIGHTLIFTING
                ExerciseSessionRecord.EXERCISE_TYPE_WHEELCHAIR -> ExerciseType.WHEELCHAIR
                ExerciseSessionRecord.EXERCISE_TYPE_OTHER_WORKOUT -> ExerciseType.OTHER_WORKOUT
                ExerciseSessionRecord.EXERCISE_TYPE_YOGA -> ExerciseType.YOGA
                else -> ExerciseType.OTHER_WORKOUT
            }
        }
    }
}