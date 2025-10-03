package com.oxeai.health

// MongoDB Schema Design for Wellness App (Kotlin Data Classes)
// Integrating Google Health and Apple Health Data
// Using MongoDB Kotlin Driver annotations

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.time.Instant
import java.time.LocalDate

@Serializable
data class Measurement(
    val value: Double,
    val unit: String
)

@Serializable
data class ConnectedPlatforms(
    val googleHealth: PlatformConnection,
    val appleHealth: PlatformConnection
)

@Serializable
data class PlatformConnection(
    val connected: Boolean,
    val lastSync: Instant? = null
)

// ============================================
// 2. ACTIVITY DATA (Hourly)
// ============================================
@Serializable
sealed class BaseHealthData {
    @BsonId
    val id: ObjectId = ObjectId()
    abstract val userId: String
    abstract val timestamp: Instant
    abstract val source: DataSource
    abstract val metadata: ActivityMetadata
    val createdAt: Instant = Instant.now()
    val updatedAt: Instant = Instant.now()
}

@Serializable
data class StepsData(
    override val userId: String,
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val steps: TrackedMetric
) : BaseHealthData()

@Serializable
data class DistanceData(
    override val userId: String,
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val distance: TrackedMeasurement
) : BaseHealthData()

@Serializable
data class ActiveCaloriesData(
    override val userId: String,
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val caloriesActive: TrackedMeasurement
) : BaseHealthData()

@Serializable
data class BasalCaloriesData(
    override val userId: String,
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val caloriesBasal: TrackedMeasurement
) : BaseHealthData()

@Serializable
data class FloorsClimbedData(
    override val userId: String,
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val floorsClimbed: TrackedMetric
) : BaseHealthData()

@Serializable
data class MoveMinutesData(
    override val userId: String,
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val moveMinutes: TrackedMetric
) : BaseHealthData()

@Serializable
data class StandHoursData(
    override val userId: String,
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val standHours: SimpleMetric
) : BaseHealthData()

@Serializable
data class HeartRateData(
    override val userId: String,
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val averageBpm: Double,
    val minBpm: Long,
    val maxBpm: Long
) : BaseHealthData()

@Serializable
data class TotalCaloriesBurnedData(
    override val userId: String,
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val totalCalories: Double
) : BaseHealthData()

@Serializable
data class SpeedData(
    override val userId: String,
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val averageSpeed: Double
) : BaseHealthData()

@Serializable
data class Vo2MaxData(
    override val userId: String,
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val vo2Max: Double
) : BaseHealthData()

@Serializable
data class BodyFatData(
    override val userId: String,
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val bodyFatPercentage: Double
) : BaseHealthData()

@Serializable
data class LeanBodyMassData(
    override val userId: String,
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val leanBodyMass: Double
) : BaseHealthData()

@Serializable
data class WeightData(
    override val userId: String,
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val weight: TrackedMetric
) : BaseHealthData()

@Serializable
data class HeightData(
    override val userId: String,
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val height: TrackedMetric
) : BaseHealthData()

@Serializable
data class BoneMassData(
    override val userId: String,
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val boneMass: TrackedMetric
) : BaseHealthData()

@Serializable
data class BloodPressureData(
    override val userId: String,
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val systolic: TrackedMetric,
    val diastolic: TrackedMetric
) : BaseHealthData()

enum class DataSource {
    GOOGLE, APPLE, MERGED
}

data class TrackedMetric(
    val count: Int,
    val sources: List<String>
)

data class TrackedMeasurement(
    val value: Double,
    val unit: String,
    val sources: List<String>
)

data class SimpleMetric(
    val count: Int,
    val source: String
)

data class ActivityMetadata(
    val devices: List<String>,
    val confidence: DataConfidence
)

enum class DataConfidence {
    HIGH, MEDIUM, LOW
}

// ============================================
// 3. HEART RATE DATA (Hourly Summary)
// ============================================
data class HeartRateHourly(
    @BsonId
    val id: ObjectId = ObjectId(),
    val userId: String,
    val timestamp: Instant,
    val source: DataSource,
    val restingHeartRate: HeartRateReading? = null,
    val averageHeartRate: AverageHeartRate? = null,
    val minHeartRate: HeartRateReading? = null,
    val maxHeartRate: HeartRateReading? = null,
    val heartRateVariability: HRVReading? = null,
    val metadata: HeartRateMetadata,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now()
)

data class HeartRateReading(
    val bpm: Int,
    val recordedAt: Instant
)

data class AverageHeartRate(
    val bpm: Int,
    val sampleCount: Int
)

data class HRVReading(
    val sdnn: Double,
    val recordedAt: Instant,
    val source: String
)

data class HeartRateMetadata(
    val devices: List<String>,
    val measurementContext: MeasurementContext
)

enum class MeasurementContext {
    RESTING, ACTIVE, EXERCISE
}

// ============================================
// 4. SLEEP DATA (Per Sleep Session)
// ============================================
data class SleepSession(
    @BsonId
    val id: ObjectId = ObjectId(),
    val userId: String,
    val startTime: Instant,
    val endTime: Instant,
    val source: DataSource,
    val totalDuration: Int, // minutes
    val stages: SleepStages,
    val sleepScore: SleepScore? = null,
    val interruptions: SleepInterruptions,
    val efficiency: Double, // percentage
    val metadata: SleepMetadata,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now()
)

data class SleepStages(
    val awake: Int, // minutes
    val light: Int,
    val deep: Int,
    val rem: Int
)

data class SleepScore(
    val value: Int, // 0-100
    val source: String
)

data class SleepInterruptions(
    val count: Int,
    val totalDuration: Int // minutes
)

data class SleepMetadata(
    val device: String,
    val dataQuality: String,
    val isNap: Boolean = false
)

// ============================================
// 5. WORKOUTS / EXERCISE SESSIONS
// ============================================
data class Workout(
    @BsonId
    val id: ObjectId = ObjectId(),
    val userId: String,
    val startTime: Instant,
    val endTime: Instant,
    val source: DataSource,
    val workoutType: WorkoutType,
    val duration: Int, // minutes
    val distance: Measurement? = null,
    val calories: CaloriesBurned? = null,
    val heartRate: WorkoutHeartRate? = null,
    val elevation: ElevationData? = null,
    val pace: PaceData? = null,
    val appleSpecific: AppleWorkoutData? = null,
    val googleSpecific: GoogleWorkoutData? = null,
    val metadata: WorkoutMetadata,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now()
)

enum class WorkoutType {
    RUNNING, CYCLING, SWIMMING, STRENGTH_TRAINING, WALKING,
    HIKING, YOGA, PILATES, BOXING, ROWING, ELLIPTICAL, OTHER
}

data class CaloriesBurned(
    val total: Double,
    val unit: String
)

data class WorkoutHeartRate(
    val average: Int,
    val max: Int,
    val min: Int
)

data class ElevationData(
    val gain: Double,
    val loss: Double,
    val unit: String
)

data class PaceData(
    val average: Double, // min/km or min/mile
    val unit: String
)

data class AppleWorkoutData(
    val activeEnergyBurned: Double,
    val basalEnergyBurned: Double
)

data class GoogleWorkoutData(
    val activitySegments: List<ActivitySegment>
)

data class ActivitySegment(
    val activityType: String,
    val duration: Int
)

data class WorkoutMetadata(
    val device: String,
    val indoor: Boolean,
    val route: Map<String, Any>? = null
)

// ============================================
// 6. NUTRITION DATA (Hourly or per meal)
// ============================================
data class NutritionHourly(
    @BsonId
    val id: ObjectId = ObjectId(),
    val userId: String,
    val timestamp: Instant,
    val source: DataSource,
    val calories: CaloriesConsumed? = null,
    val macros: Macronutrients? = null,
    val hydration: Hydration? = null,
    val micronutrients: Micronutrients? = null,
    val mealType: MealType? = null,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now()
)

data class CaloriesConsumed(
    val consumed: Double,
    val unit: String
)

data class Macronutrients(
    val protein: NutrientAmount,
    val carbs: NutrientAmount,
    val fats: NutrientAmount,
    val fiber: NutrientAmount
)

data class NutrientAmount(
    val value: Double,
    val unit: String
)

data class Hydration(
    val water: Double,
    val unit: String
)

data class Micronutrients(
    val sodium: Double? = null,
    val sugar: Double? = null,
    val cholesterol: Double? = null
)

enum class MealType {
    BREAKFAST, LUNCH, DINNER, SNACK
}

// ============================================
// 7. BODY MEASUREMENTS (Periodic)
// ============================================
data class BodyMeasurement(
    @BsonId
    val id: ObjectId = ObjectId(),
    val userId: String,
    val timestamp: Instant,
    val source: DataSource,
    val weight: SourcedMeasurement? = null,
    val bodyFatPercentage: SourcedValue? = null,
    val bmi: BMIValue? = null,
    val leanBodyMass: Measurement? = null,
    val waistCircumference: Measurement? = null,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now()
)

data class SourcedMeasurement(
    val value: Double,
    val unit: String,
    val source: String
)

data class SourcedValue(
    val value: Double,
    val source: String
)

data class BMIValue(
    val value: Double,
    val calculatedFrom: String // 'reported' or 'calculated'
)

// ============================================
// 8. VITALS (Hourly or as recorded)
// ============================================
data class Vitals(
    @BsonId
    val id: ObjectId = ObjectId(),
    val userId: String,
    val timestamp: Instant,
    val source: DataSource,
    val bloodPressure: BloodPressureReading? = null,
    val oxygenSaturation: OxygenSaturation? = null,
    val bodyTemperature: TemperatureReading? = null,
    val respiratoryRate: RespiratoryRate? = null,
    val bloodGlucose: BloodGlucoseReading? = null,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now()
)

data class BloodPressureReading(
    val systolic: Int,
    val diastolic: Int,
    val unit: String, // 'mmHg'
    val recordedAt: Instant
)

data class OxygenSaturation(
    val percentage: Double,
    val recordedAt: Instant
)

data class TemperatureReading(
    val value: Double,
    val unit: String, // 'celsius', 'fahrenheit'
    val recordedAt: Instant
)

data class RespiratoryRate(
    val value: Int,
    val recordedAt: Instant
)

data class BloodGlucoseReading(
    val value: Double,
    val unit: String, // 'mg/dL', 'mmol/L'
    val context: GlucoseContext,
    val recordedAt: Instant
)

enum class GlucoseContext {
    FASTING, POST_MEAL, RANDOM
}

// ============================================
// 9. MINDFULNESS / MENTAL WELLNESS
// ============================================
data class Mindfulness(
    @BsonId
    val id: ObjectId = ObjectId(),
    val userId: String,
    val timestamp: Instant,
    val source: DataSource,
    val meditationMinutes: MeditationSession? = null,
    val moodScore: MoodScore? = null,
    val stressLevel: StressLevel? = null,
    val breathingRate: BreathingRate? = null,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now()
)

data class MeditationSession(
    val duration: Int,
    val sessionType: MeditationType
)

enum class MeditationType {
    GUIDED, UNGUIDED, BREATHING
}

data class MoodScore(
    val value: Int, // 1-10 scale
    val notes: String? = null
)

data class StressLevel(
    val value: Double,
    val unit: String
)

data class BreathingRate(
    val average: Double,
    val min: Double,
    val max: Double
)

// ============================================
// 10. DATA SYNC LOG
// ============================================
data class SyncLog(
    @BsonId
    val id: ObjectId = ObjectId(),
    val userId: String,
    val platform: Platform,
    val syncStartTime: Instant,
    val syncEndTime: Instant,
    val status: SyncStatus,
    val dataTypes: List<DataTypeSync>,
    val lastSuccessfulTimestamp: Instant? = null,
    val createdAt: Instant = Instant.now()
)

enum class Platform {
    GOOGLE, APPLE
}

enum class SyncStatus {
    SUCCESS, PARTIAL, FAILED
}

data class DataTypeSync(
    val type: String,
    val recordsProcessed: Int,
    val recordsInserted: Int,
    val recordsUpdated: Int,
    val errors: List<String>
)

// ============================================
// AGGREGATION HELPERS
// ============================================
data class DailySummary(
    @BsonId
    val id: ObjectId = ObjectId(),
    val userId: String,
    val date: LocalDate,
    val activity: DailyActivity,
    val heartRate: DailyHeartRate,
    val sleep: DailySleep,
    val workouts: DailyWorkouts,
    val completenessScore: Int, // 0-100
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now()
)

data class DailyActivity(
    val totalSteps: Int,
    val totalDistance: Double,
    val totalCalories: Double,
    val activeMinutes: Int,
    val floorsClimbed: Int
)

data class DailyHeartRate(
    val restingAvg: Int,
    val averageHR: Int,
    val minHR: Int,
    val maxHR: Int
)

data class DailySleep(
    val totalMinutes: Int,
    val efficiency: Double,
    val deepSleepMinutes: Int,
    val remSleepMinutes: Int
)

data class DailyWorkouts(
    val count: Int,
    val totalDuration: Int,
    val totalCalories: Double
)

// ============================================
// INDEX DEFINITIONS (to be created in MongoDB)
// ============================================
/*
// Activity
db.activityHourly.createIndex({ userId: 1, timestamp: -1 })
db.activityHourly.createIndex({ userId: 1, timestamp: 1, source: 1 })

// Heart Rate
db.heartRateHourly.createIndex({ userId: 1, timestamp: -1 })

// Sleep
db.sleepSessions.createIndex({ userId: 1, startTime: -1 })

// Workouts
db.workouts.createIndex({ userId: 1, startTime: -1 })
db.workouts.createIndex({ userId: 1, workoutType: 1, startTime: -1 })

// Body Measurements
db.bodyMeasurements.createIndex({ userId: 1, timestamp: -1 })

// Vitals
db.vitals.createIndex({ userId: 1, timestamp: -1 })

// Daily Summaries
db.dailySummaries.createIndex({ userId: 1, date: -1 })
*/

// ============================================
// IMPLEMENTATION NOTES
// ============================================
/*
1. Dependencies needed:
   - MongoDB Kotlin Driver: org.mongodb:mongodb-driver-kotlin-coroutine
   - BSON: org.mongodb:bson

2. Deduplication Strategy:
   - Use 'sources' lists to track which platform provided data
   - Prefer higher quality data (e.g., from dedicated devices)
   - Implement merge logic in application/service layer

3. Time Zones:
   - Store all Instant timestamps in UTC
   - Store user's timezone in profile for display purposes

4. Data Retention:
   - Hourly data: Keep for 90 days, then aggregate to daily
   - Daily summaries: Keep indefinitely
   - Raw data: Archive to cold storage if needed

5. Null Safety:
   - Kotlin nullable types used where data may not be available
   - Non-null fields are required from both platforms

6. Repository Pattern Example:
   interface ActivityRepository {
       suspend fun insert(activity: ActivityHourly): ObjectId
       suspend fun findByUserAndTimeRange(
           userId: String,
           start: Instant,
           end: Instant
       ): List<ActivityHourly>
       suspend fun updateActivity(id: ObjectId, activity: ActivityHourly)
   }
*/