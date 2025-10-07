package com.oxeai.healthconnect.fetchers

import android.content.Context
import androidx.health.connect.client.records.ExerciseSegment
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
                    exerciseType = record.exerciseType.toExerciseType(),
                    startTime = record.startTime,
                    endTime = record.endTime,
                    notes = record.notes,
                    title = record.title,
                    segments = record.segments.map {
                        com.oxeai.healthconnect.models.ExerciseSegment(
                            startTime = it.startTime,
                            endTime = it.endTime,
                            segmentType = it.segmentType.toSegmentType(),
                            repetitions = it.repetitions
                        )
                    },
                    laps = record.laps.map {
                        com.oxeai.healthconnect.models.ExerciseLap(
                            startTime = it.startTime,
                            endTime = it.endTime,
                            length = it.length?.inMeters?.let { length ->
                                com.oxeai.healthconnect.models.SimpleMeasurement(
                                    length, "m"
                                )
                            }
                        )
                    },
                )
            )
        }
        return exerciseData
    }

    companion object {
        fun Int.toSegmentType(): com.oxeai.healthconnect.models.ExerciseSegmentType {
            return when (this) {
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_ARM_CURL -> com.oxeai.healthconnect.models.ExerciseSegmentType.ARM_CURL
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_BACK_EXTENSION -> com.oxeai.healthconnect.models.ExerciseSegmentType.BACK_EXTENSION
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_BALL_SLAM -> com.oxeai.healthconnect.models.ExerciseSegmentType.BALL_SLAM
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_BARBELL_SHOULDER_PRESS -> com.oxeai.healthconnect.models.ExerciseSegmentType.BARBELL_SHOULDER_PRESS
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_BENCH_PRESS -> com.oxeai.healthconnect.models.ExerciseSegmentType.BENCH_PRESS
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_BENCH_SIT_UP -> com.oxeai.healthconnect.models.ExerciseSegmentType.BENCH_SIT_UP
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_BIKING -> com.oxeai.healthconnect.models.ExerciseSegmentType.BIKING
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_BIKING_STATIONARY -> com.oxeai.healthconnect.models.ExerciseSegmentType.BIKING_STATIONARY
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_BURPEE -> com.oxeai.healthconnect.models.ExerciseSegmentType.BURPEE
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_CRUNCH -> com.oxeai.healthconnect.models.ExerciseSegmentType.CRUNCH
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_DEADLIFT -> com.oxeai.healthconnect.models.ExerciseSegmentType.DEADLIFT
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_DOUBLE_ARM_TRICEPS_EXTENSION -> com.oxeai.healthconnect.models.ExerciseSegmentType.DOUBLE_ARM_TRICEPS_EXTENSION
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_DUMBBELL_CURL_LEFT_ARM -> com.oxeai.healthconnect.models.ExerciseSegmentType.DUMBBELL_CURL_LEFT_ARM
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_DUMBBELL_CURL_RIGHT_ARM -> com.oxeai.healthconnect.models.ExerciseSegmentType.DUMBBELL_CURL_RIGHT_ARM
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_DUMBBELL_FRONT_RAISE -> com.oxeai.healthconnect.models.ExerciseSegmentType.DUMBBELL_FRONT_RAISE
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_DUMBBELL_LATERAL_RAISE -> com.oxeai.healthconnect.models.ExerciseSegmentType.DUMBBELL_LATERAL_RAISE
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_DUMBBELL_ROW -> com.oxeai.healthconnect.models.ExerciseSegmentType.DUMBBELL_ROW
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_DUMBBELL_TRICEPS_EXTENSION_LEFT_ARM -> com.oxeai.healthconnect.models.ExerciseSegmentType.DUMBBELL_TRICEPS_EXTENSION_LEFT_ARM
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_DUMBBELL_TRICEPS_EXTENSION_RIGHT_ARM -> com.oxeai.healthconnect.models.ExerciseSegmentType.DUMBBELL_TRICEPS_EXTENSION_RIGHT_ARM
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_DUMBBELL_TRICEPS_EXTENSION_TWO_ARM -> com.oxeai.healthconnect.models.ExerciseSegmentType.DUMBBELL_TRICEPS_EXTENSION_TWO_ARM
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_ELLIPTICAL -> com.oxeai.healthconnect.models.ExerciseSegmentType.ELLIPTICAL
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_FORWARD_TWIST -> com.oxeai.healthconnect.models.ExerciseSegmentType.FORWARD_TWIST
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_FRONT_RAISE -> com.oxeai.healthconnect.models.ExerciseSegmentType.FRONT_RAISE
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_HIGH_INTENSITY_INTERVAL_TRAINING -> com.oxeai.healthconnect.models.ExerciseSegmentType.HIGH_INTENSITY_INTERVAL_TRAINING
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_HIP_THRUST -> com.oxeai.healthconnect.models.ExerciseSegmentType.HIP_THRUST
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_HULA_HOOP -> com.oxeai.healthconnect.models.ExerciseSegmentType.HULA_HOOP
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_JUMPING_JACK -> com.oxeai.healthconnect.models.ExerciseSegmentType.JUMPING_JACK
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_JUMP_ROPE -> com.oxeai.healthconnect.models.ExerciseSegmentType.JUMP_ROPE
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_KETTLEBELL_SWING -> com.oxeai.healthconnect.models.ExerciseSegmentType.KETTLEBELL_SWING
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_LATERAL_RAISE -> com.oxeai.healthconnect.models.ExerciseSegmentType.LATERAL_RAISE
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_LAT_PULL_DOWN -> com.oxeai.healthconnect.models.ExerciseSegmentType.LAT_PULL_DOWN
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_LEG_CURL -> com.oxeai.healthconnect.models.ExerciseSegmentType.LEG_CURL
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_LEG_EXTENSION -> com.oxeai.healthconnect.models.ExerciseSegmentType.LEG_EXTENSION
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_LEG_PRESS -> com.oxeai.healthconnect.models.ExerciseSegmentType.LEG_PRESS
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_LEG_RAISE -> com.oxeai.healthconnect.models.ExerciseSegmentType.LEG_RAISE
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_LUNGE -> com.oxeai.healthconnect.models.ExerciseSegmentType.LUNGE
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_MOUNTAIN_CLIMBER -> com.oxeai.healthconnect.models.ExerciseSegmentType.MOUNTAIN_CLIMBER
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_OTHER_WORKOUT -> com.oxeai.healthconnect.models.ExerciseSegmentType.OTHER_WORKOUT
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_PAUSE -> com.oxeai.healthconnect.models.ExerciseSegmentType.PAUSE
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_PILATES -> com.oxeai.healthconnect.models.ExerciseSegmentType.PILATES
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_PLANK -> com.oxeai.healthconnect.models.ExerciseSegmentType.PLANK
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_PULL_UP -> com.oxeai.healthconnect.models.ExerciseSegmentType.PULL_UP
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_PUNCH -> com.oxeai.healthconnect.models.ExerciseSegmentType.PUNCH
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_REST -> com.oxeai.healthconnect.models.ExerciseSegmentType.REST
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_ROWING_MACHINE -> com.oxeai.healthconnect.models.ExerciseSegmentType.ROWING_MACHINE
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_RUNNING -> com.oxeai.healthconnect.models.ExerciseSegmentType.RUNNING
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_RUNNING_TREADMILL -> com.oxeai.healthconnect.models.ExerciseSegmentType.RUNNING_TREADMILL
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_SHOULDER_PRESS -> com.oxeai.healthconnect.models.ExerciseSegmentType.SHOULDER_PRESS
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_SINGLE_ARM_TRICEPS_EXTENSION -> com.oxeai.healthconnect.models.ExerciseSegmentType.SINGLE_ARM_TRICEPS_EXTENSION
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_SIT_UP -> com.oxeai.healthconnect.models.ExerciseSegmentType.SIT_UP
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_SQUAT -> com.oxeai.healthconnect.models.ExerciseSegmentType.SQUAT
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_STAIR_CLIMBING -> com.oxeai.healthconnect.models.ExerciseSegmentType.STAIR_CLIMBING
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_STAIR_CLIMBING_MACHINE -> com.oxeai.healthconnect.models.ExerciseSegmentType.STAIR_CLIMBING_MACHINE
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_STRETCHING -> com.oxeai.healthconnect.models.ExerciseSegmentType.STRETCHING
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_SWIMMING_BACKSTROKE -> com.oxeai.healthconnect.models.ExerciseSegmentType.SWIMMING_BACKSTROKE
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_SWIMMING_BREASTSTROKE -> com.oxeai.healthconnect.models.ExerciseSegmentType.SWIMMING_BREASTSTROKE
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_SWIMMING_BUTTERFLY -> com.oxeai.healthconnect.models.ExerciseSegmentType.SWIMMING_BUTTERFLY
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_SWIMMING_FREESTYLE -> com.oxeai.healthconnect.models.ExerciseSegmentType.SWIMMING_FREESTYLE
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_SWIMMING_MIXED -> com.oxeai.healthconnect.models.ExerciseSegmentType.SWIMMING_MIXED
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_SWIMMING_OPEN_WATER -> com.oxeai.healthconnect.models.ExerciseSegmentType.SWIMMING_OPEN_WATER
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_SWIMMING_OTHER -> com.oxeai.healthconnect.models.ExerciseSegmentType.SWIMMING_OTHER
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_SWIMMING_POOL -> com.oxeai.healthconnect.models.ExerciseSegmentType.SWIMMING_POOL
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_UPPER_TWIST -> com.oxeai.healthconnect.models.ExerciseSegmentType.UPPER_TWIST
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_WALKING -> com.oxeai.healthconnect.models.ExerciseSegmentType.WALKING
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_WEIGHTLIFTING -> com.oxeai.healthconnect.models.ExerciseSegmentType.WEIGHTLIFTING
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_WHEELCHAIR -> com.oxeai.healthconnect.models.ExerciseSegmentType.WHEELCHAIR
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_YOGA -> com.oxeai.healthconnect.models.ExerciseSegmentType.YOGA
                else -> com.oxeai.healthconnect.models.ExerciseSegmentType.UNKNOWN
            }
        }

        fun Int.toExerciseType(): ExerciseType {
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