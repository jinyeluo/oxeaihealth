package com.oxeai.healthconnect.models

import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.UUID

@Serializable
sealed class BaseHealthData {
    abstract val userId: UUID

    @Serializable(with = InstantSerializer::class)
    abstract val timestamp: Instant
    abstract val source: DataSource
    abstract val metadata: ActivityMetadata
}

@Serializable
abstract class IntervalHealthData : BaseHealthData() {
    abstract val startTime: Instant
    abstract val endTime: Instant
}

@Serializable
data class StepsData(
    @Serializable(with = UUIDSerializer::class)
    override val userId: UUID,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    @Serializable(with = InstantSerializer::class)
    override val startTime: Instant,
    @Serializable(with = InstantSerializer::class)
    override val endTime: Instant,
    val steps: TrackedMetric
) : IntervalHealthData()

@Serializable
data class DistanceData(
    @Serializable(with = UUIDSerializer::class)
    override val userId: UUID,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val distance: TrackedMeasurement
) : BaseHealthData()

@Serializable
data class CaloriesData(
    @Serializable(with = UUIDSerializer::class)
    override val userId: UUID,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    @Serializable(with = InstantSerializer::class)
    override val startTime: Instant,
    @Serializable(with = InstantSerializer::class)
    override val endTime: Instant,
    val caloriesActive: TrackedMeasurement,
    val caloriesTotal: TrackedMeasurement
) : IntervalHealthData()

@Serializable
data class BasalCaloriesData(
    @Serializable(with = UUIDSerializer::class)
    override val userId: UUID,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val caloriesBasal: TrackedMeasurement
) : BaseHealthData()

@Serializable
data class FloorsClimbedData(
    @Serializable(with = UUIDSerializer::class)
    override val userId: UUID,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val floorsClimbed: TrackedMetric
) : BaseHealthData()

@Serializable
data class MoveMinutesData(
    @Serializable(with = UUIDSerializer::class)
    override val userId: UUID,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val moveMinutes: TrackedMetric
) : BaseHealthData()

@Serializable
data class StandHoursData(
    @Serializable(with = UUIDSerializer::class)
    override val userId: UUID,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val standHours: SimpleMetric
) : BaseHealthData()

@Serializable
data class HeartRateData(
    @Serializable(with = UUIDSerializer::class)
    override val userId: UUID,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val averageBpm: Double,
    val minBpm: Long,
    val maxBpm: Long
) : BaseHealthData()

@Serializable
data class TotalCaloriesBurnedData(
    @Serializable(with = UUIDSerializer::class)
    override val userId: UUID,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val totalCalories: Double
) : BaseHealthData()

@Serializable
data class SpeedData(
    @Serializable(with = UUIDSerializer::class)
    override val userId: UUID,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val averageSpeed: Double
) : BaseHealthData()

@Serializable
data class Vo2MaxData(
    @Serializable(with = UUIDSerializer::class)
    override val userId: UUID,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val vo2Max: Double
) : BaseHealthData()

@Serializable
data class BasalMetabolicRateData(
    @Serializable(with = UUIDSerializer::class)
    override val userId: UUID,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val measurements: MutableList<TrackedMeasurement> = ArrayList()
) : BaseHealthData()

@Serializable
data class PowerData(
    @Serializable(with = UUIDSerializer::class)
    override val userId: UUID,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val measurements: MutableList<TrackedMeasurement>
) : BaseHealthData()


@Serializable
data class BodyFat(
    val percentage: Double,
    @Serializable(with = InstantSerializer::class)
    val recordedAt: Instant
)

@Serializable
data class BodyFatData(
    @Serializable(with = UUIDSerializer::class)
    override val userId: UUID,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val bodyFatPercentages: MutableList<BodyFat>
) : BaseHealthData()

@Serializable
data class LeanBodyMassData(
    @Serializable(with = UUIDSerializer::class)
    override val userId: UUID,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val leanBodyMass: Double
) : BaseHealthData()

@Serializable
data class WeightData(
    @Serializable(with = UUIDSerializer::class)
    override val userId: UUID,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val weight: TrackedMetric
) : BaseHealthData()

@Serializable
data class HeightData(
    @Serializable(with = UUIDSerializer::class)
    override val userId: UUID,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val height: TrackedMetric
) : BaseHealthData()

@Serializable
data class BoneMassData(
    @Serializable(with = UUIDSerializer::class)
    override val userId: UUID,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val boneMass: TrackedMetric
) : BaseHealthData()

@Serializable
data class BloodPressure(
    val systolic: Int,
    val diastolic: Int,
    val unit: String,
    @Serializable(with = InstantSerializer::class)
    val recordedAt: Instant
)

@Serializable
data class BloodPressureData(
    @Serializable(with = UUIDSerializer::class)
    override val userId: UUID,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val bloodPressure: MutableList<BloodPressure>
) : BaseHealthData()

@Serializable
data class BloodGlucoseData(
    @Serializable(with = UUIDSerializer::class)
    override val userId: UUID,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val bloodGlucose: BloodGlucoseReading
) : BaseHealthData()

@Serializable
data class BodyTemperatureData(
    @Serializable(with = UUIDSerializer::class)
    override val userId: UUID,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val bodyTemperature: MutableList<TemperatureReading>
) : BaseHealthData()

@Serializable
data class OxygenSaturationData(
    @Serializable(with = UUIDSerializer::class)
    override val userId: UUID,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val oxygenSaturation: OxygenSaturation
) : BaseHealthData()

@Serializable
data class RespiratoryRateData(
    @Serializable(with = UUIDSerializer::class)
    override val userId: UUID,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val respiratoryRate: RespiratoryRate
) : BaseHealthData()

@Serializable
data class HydrationData(
    @Serializable(with = UUIDSerializer::class)
    override val userId: UUID,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val hydration: Hydration
) : BaseHealthData()

@Serializable
data class NutritionData(
    @Serializable(with = UUIDSerializer::class)
    override val userId: UUID,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val nutrition: Nutrition
) : BaseHealthData()

@Serializable
data class SleepSessionData(
    @Serializable(with = UUIDSerializer::class)
    override val userId: UUID,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    @Serializable(with = InstantSerializer::class)
    val startTime: Instant,
    @Serializable(with = InstantSerializer::class)
    val endTime: Instant,
    val duration: Long,
    val stages: String
) : BaseHealthData()

@Serializable
data class MenstruationData(
    @Serializable(with = UUIDSerializer::class)
    override val userId: UUID,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val flow: String
) : BaseHealthData()

@Serializable
data class IntermenstrualBleedingData(
    @Serializable(with = UUIDSerializer::class)
    override val userId: UUID,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata
) : BaseHealthData()

enum class DataSource {
    GOOGLE
}

@Serializable
data class TrackedMetric(
    val count: Int
)

@Serializable
data class TrackedMeasurement(
    val value: Double,
    val unit: String,

    @Serializable(with = InstantSerializer::class)
    val recordedAt: Instant
)

@Serializable
data class SimpleMetric(
    val count: Int,
    val source: String
)

@Serializable
data class ActivityMetadata(
    val devices: List<String>,
    val confidence: DataConfidence
)

enum class DataConfidence {
    HIGH, MEDIUM, LOW
}

@Serializable
data class Nutrition(
    @Serializable(with = InstantSerializer::class)
    val timestamp: Instant,
    val source: DataSource,
    val calories: CaloriesConsumed? = null,
    val macros: Macronutrients? = null,
    val hydration: Hydration? = null,
    val micronutrients: Micronutrients? = null,
    val mealType: MealType? = null,
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant = Instant.now(),
    @Serializable(with = InstantSerializer::class)
    val updatedAt: Instant = Instant.now()
)

@Serializable
data class CaloriesConsumed(
    val consumed: Double,
    val unit: String
)

@Serializable
data class Macronutrients(
    val protein: NutrientAmount,
    val carbs: NutrientAmount,
    val fats: NutrientAmount,
    val fiber: NutrientAmount
)

@Serializable
data class NutrientAmount(
    val value: Double,
    val unit: String
)

@Serializable
data class Hydration(
    val water: Double,
    val unit: String
)

@Serializable
data class Micronutrients(
    val sodium: Double? = null,
    val sugar: Double? = null,
    val cholesterol: Double? = null
)


@Serializable
data class OxygenSaturation(
    val percentage: Double,
    @Serializable(with = InstantSerializer::class)
    val recordedAt: Instant
)

@Serializable
data class TemperatureReading(
    val value: Double,
    val unit: String, // 'celsius', 'fahrenheit'
    @Serializable(with = InstantSerializer::class)
    val recordedAt: Instant
)

@Serializable
data class RespiratoryRate(
    val value: Int,
    @Serializable(with = InstantSerializer::class)
    val recordedAt: Instant
)

@Serializable
data class GlucoseContext(
    val mealType: MealType,
    var relationToMeal: RelationToMeal
)


@Serializable
data class BloodGlucoseReading(
    val value: Double,
    val unit: String, // 'mg/dL',
    val context: GlucoseContext,
    @Serializable(with = InstantSerializer::class)
    val recordedAt: Instant
)

enum class MealType {
    BREAKFAST, LUNCH, DINNER, SNACK, UNKNOWN
}

enum class RelationToMeal {
    GENERAL, FASTING, BEFORE_MEAL, AFTER_MEAL, UNKNOWN
}

