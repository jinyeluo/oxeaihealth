package com.oxeai.healthconnect.models

import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
sealed class BaseHealthData {
    abstract val userId: String

    @Serializable(with = InstantSerializer::class)
    abstract val timestamp: Instant
    abstract val source: DataSource
    abstract val metadata: ActivityMetadata
}

@Serializable
data class StepsData(
    override val userId: String,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val steps: TrackedMetric
) : BaseHealthData()

@Serializable
data class DistanceData(
    override val userId: String,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val distance: TrackedMeasurement
) : BaseHealthData()

@Serializable
data class ActiveCaloriesData(
    override val userId: String,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val caloriesActive: TrackedMeasurement
) : BaseHealthData()

@Serializable
data class BasalCaloriesData(
    override val userId: String,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val caloriesBasal: TrackedMeasurement
) : BaseHealthData()

@Serializable
data class FloorsClimbedData(
    override val userId: String,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val floorsClimbed: TrackedMetric
) : BaseHealthData()

@Serializable
data class MoveMinutesData(
    override val userId: String,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val moveMinutes: TrackedMetric
) : BaseHealthData()

@Serializable
data class StandHoursData(
    override val userId: String,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val standHours: SimpleMetric
) : BaseHealthData()

@Serializable
data class HeartRateData(
    override val userId: String,
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
    override val userId: String,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val totalCalories: Double
) : BaseHealthData()

@Serializable
data class SpeedData(
    override val userId: String,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val averageSpeed: Double
) : BaseHealthData()

@Serializable
data class Vo2MaxData(
    override val userId: String,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val vo2Max: Double
) : BaseHealthData()

@Serializable
data class BasalMetabolicRateData(
    override val userId: String,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val basalMetabolicRate: TrackedMeasurement
) : BaseHealthData()

@Serializable
data class PowerData(
    override val userId: String,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val power: TrackedMeasurement
) : BaseHealthData()

@Serializable
data class BodyFatData(
    override val userId: String,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val bodyFatPercentage: Double
) : BaseHealthData()

@Serializable
data class LeanBodyMassData(
    override val userId: String,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val leanBodyMass: Double
) : BaseHealthData()

@Serializable
data class WeightData(
    override val userId: String,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val weight: TrackedMetric
) : BaseHealthData()

@Serializable
data class HeightData(
    override val userId: String,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val height: TrackedMetric
) : BaseHealthData()

@Serializable
data class BoneMassData(
    override val userId: String,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val boneMass: TrackedMetric
) : BaseHealthData()

@Serializable
data class BloodPressureData(
    override val userId: String,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val systolic: TrackedMetric,
    val diastolic: TrackedMetric
) : BaseHealthData()

@Serializable
data class BloodGlucoseData(
    override val userId: String,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val bloodGlucose: BloodGlucoseReading
) : BaseHealthData()

@Serializable
data class BodyTemperatureData(
    override val userId: String,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val bodyTemperature: TemperatureReading
) : BaseHealthData()

@Serializable
data class OxygenSaturationData(
    override val userId: String,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val oxygenSaturation: OxygenSaturation
) : BaseHealthData()

@Serializable
data class RespiratoryRateData(
    override val userId: String,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val respiratoryRate: RespiratoryRate
) : BaseHealthData()

@Serializable
data class HydrationData(
    override val userId: String,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val hydration: Hydration
) : BaseHealthData()

@Serializable
data class NutritionData(
    override val userId: String,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val nutrition: Nutrition
) : BaseHealthData()

@Serializable
data class SleepSessionData(
    override val userId: String,
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
    override val userId: String,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: ActivityMetadata,
    val flow: String
) : BaseHealthData()

@Serializable
data class IntermenstrualBleedingData(
    override val userId: String,
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
    val count: Int,
    val sources: List<String>
)

@Serializable
data class TrackedMeasurement(
    val value: Double,
    val unit: String,
    val sources: List<String>
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
    val userId: String,
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

enum class MealType {
    BREAKFAST, LUNCH, DINNER, SNACK
}

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
data class BloodGlucoseReading(
    val value: Double,
    val unit: String, // 'mg/dL', 'mmol/L'
    val context: GlucoseContext,
    @Serializable(with = InstantSerializer::class)
    val recordedAt: Instant
)

enum class GlucoseContext {
    FASTING, POST_MEAL, RANDOM
}

