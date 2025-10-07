package com.oxeai.healthconnect.models

import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.UUID

enum class DataSource {
    GOOGLE
}

@Serializable
data class TrackedMetric(
    val count: Int,
    @Serializable(with = InstantSerializer::class)
    val startTime: Instant,
    @Serializable(with = InstantSerializer::class)
    val endTime: Instant
)

@Serializable
data class SimpleMeasurement(
    val value: Double,
    val unit: String,
)

@Serializable
data class TrackedMeasurement(
    val value: Double,
    val unit: String,

    @Serializable(with = InstantSerializer::class)
    val recordedAt: Instant
)

@Serializable
data class Metadata(
    val devices: List<String>,
    val confidence: DataConfidence
)

enum class DataConfidence {
    HIGH, MEDIUM, LOW
}

@Serializable
data class IntervalMeasurement(
    @Serializable(with = InstantSerializer::class)
    val startTime: Instant,
    @Serializable(with = InstantSerializer::class)
    val endTime: Instant,
    val value: Double,
    val unit: String,
)

@Serializable
sealed class BaseHealthData {
    abstract val userId: UUID

    @Serializable(with = InstantSerializer::class)
    abstract val timestamp: Instant
    abstract val source: DataSource
    abstract val metadata: Metadata

    abstract fun isValid(): Boolean
}

@Serializable
abstract class MeasurementData<T>(
    @Serializable(with = UUIDSerializer::class)
    override val userId: UUID,
    @Serializable(with = InstantSerializer::class)
    override val timestamp: Instant,
    override val source: DataSource,
    override val metadata: Metadata,
    open val measurements: MutableList<T> = ArrayList()
) : BaseHealthData() {
    override fun isValid(): Boolean {
        return measurements.isNotEmpty()
    }
}


class StepsData(
    userId: UUID,
    timestamp: Instant,
    source: DataSource,
    metadata: Metadata,
    measurements: MutableList<TrackedMetric> = ArrayList()
) : MeasurementData<TrackedMetric>(userId, timestamp, source, metadata, measurements)

class DistanceData(
    userId: UUID,
    timestamp: Instant,
    source: DataSource,
    metadata: Metadata,
    measurements: MutableList<IntervalMeasurement> = ArrayList()
) : MeasurementData<IntervalMeasurement>(userId, timestamp, source, metadata, measurements)

class ActiveCaloriesData(
    userId: UUID,
    timestamp: Instant,
    source: DataSource,
    metadata: Metadata,
    measurements: MutableList<IntervalMeasurement> = ArrayList()
) : MeasurementData<IntervalMeasurement>(userId, timestamp, source, metadata, measurements)

class TotalCaloriesData(
    userId: UUID,
    timestamp: Instant,
    source: DataSource,
    metadata: Metadata,
    measurements: MutableList<IntervalMeasurement> = ArrayList()
) : MeasurementData<IntervalMeasurement>(userId, timestamp, source, metadata, measurements)

class FloorsClimbedData(
    userId: UUID,
    timestamp: Instant,
    source: DataSource,
    metadata: Metadata,
    measurements: MutableList<IntervalMeasurement> = ArrayList()
) : MeasurementData<IntervalMeasurement>(userId, timestamp, source, metadata, measurements)

@Serializable
data class HeartRateSample(
    @Serializable(with = InstantSerializer::class)
    val time: Instant,
    val beatsPerMinute: Int
)

class HeartRateData(
    userId: UUID,
    timestamp: Instant,
    source: DataSource,
    metadata: Metadata,
    measurements: MutableList<HeartRateSample> = ArrayList()
) : MeasurementData<HeartRateSample>(userId, timestamp, source, metadata, measurements)

class SpeedData(
    userId: UUID,
    timestamp: Instant,
    source: DataSource,
    metadata: Metadata,
    measurements: MutableList<TrackedMeasurement> = ArrayList()
) : MeasurementData<TrackedMeasurement>(userId, timestamp, source, metadata, measurements)

class Vo2MaxData(
    userId: UUID,
    timestamp: Instant,
    source: DataSource,
    metadata: Metadata,
    measurements: MutableList<TrackedMeasurement> = ArrayList()
) : MeasurementData<TrackedMeasurement>(userId, timestamp, source, metadata, measurements)

class BasalMetabolicRateData(
    userId: UUID,
    timestamp: Instant,
    source: DataSource,
    metadata: Metadata,
    measurements: MutableList<TrackedMeasurement> = ArrayList()
) : MeasurementData<TrackedMeasurement>(userId, timestamp, source, metadata, measurements)

class PowerData(
    userId: UUID,
    timestamp: Instant,
    source: DataSource,
    metadata: Metadata,
    measurements: MutableList<TrackedMeasurement> = ArrayList()
) : MeasurementData<TrackedMeasurement>(userId, timestamp, source, metadata, measurements)


@Serializable
data class PercentageMeasurement(
    val percentage: Double,
    @Serializable(with = InstantSerializer::class)
    val recordedAt: Instant
)

class BodyFatData(
    userId: UUID,
    timestamp: Instant,
    source: DataSource,
    metadata: Metadata,
    measurements: MutableList<PercentageMeasurement> = ArrayList()
) : MeasurementData<PercentageMeasurement>(userId, timestamp, source, metadata, measurements)

class LeanBodyMassData(
    userId: UUID,
    timestamp: Instant,
    source: DataSource,
    metadata: Metadata,
    measurements: MutableList<TrackedMeasurement> = ArrayList()
) : MeasurementData<TrackedMeasurement>(userId, timestamp, source, metadata, measurements)

class WeightData(
    userId: UUID,
    timestamp: Instant,
    source: DataSource,
    metadata: Metadata,
    measurements: MutableList<TrackedMeasurement> = ArrayList()
) : MeasurementData<TrackedMeasurement>(userId, timestamp, source, metadata, measurements)

class HeightData(
    userId: UUID,
    timestamp: Instant,
    source: DataSource,
    metadata: Metadata,
    measurements: MutableList<TrackedMeasurement> = ArrayList()
) : MeasurementData<TrackedMeasurement>(userId, timestamp, source, metadata, measurements)

class BoneMassData(
    userId: UUID,
    timestamp: Instant,
    source: DataSource,
    metadata: Metadata,
    measurements: MutableList<TrackedMeasurement> = ArrayList()
) : MeasurementData<TrackedMeasurement>(userId, timestamp, source, metadata, measurements)
@Serializable
data class BloodPressure(
    val systolic: Double,
    val diastolic: Double,
    val unit: String,
    @Serializable(with = InstantSerializer::class)
    val recordedAt: Instant
)

class BloodPressureData(
    userId: UUID,
    timestamp: Instant,
    source: DataSource,
    metadata: Metadata,
    measurements: MutableList<BloodPressure> = ArrayList()
) : MeasurementData<BloodPressure>(userId, timestamp, source, metadata, measurements)

class BloodGlucoseData(
    userId: UUID,
    timestamp: Instant,
    source: DataSource,
    metadata: Metadata,
    measurements: MutableList<BloodGlucoseReading> = ArrayList()
) : MeasurementData<BloodGlucoseReading>(userId, timestamp, source, metadata, measurements)

class BodyTemperatureData(
    userId: UUID,
    timestamp: Instant,
    source: DataSource,
    metadata: Metadata,
    measurements: MutableList<TemperatureReading> = ArrayList()
) : MeasurementData<TemperatureReading>(userId, timestamp, source, metadata, measurements)

class OxygenSaturationData(
    userId: UUID,
    timestamp: Instant,
    source: DataSource,
    metadata: Metadata,
    measurements: MutableList<PercentageMeasurement> = ArrayList()
) : MeasurementData<PercentageMeasurement>(userId, timestamp, source, metadata, measurements)

class RespiratoryRateData(
    userId: UUID,
    timestamp: Instant,
    source: DataSource,
    metadata: Metadata,
    measurements: MutableList<TrackedMeasurement> = ArrayList()
) : MeasurementData<TrackedMeasurement>(userId, timestamp, source, metadata, measurements)

class HydrationData(
    userId: UUID,
    timestamp: Instant,
    source: DataSource,
    metadata: Metadata,
    measurements: MutableList<IntervalMeasurement> = ArrayList()
) : MeasurementData<IntervalMeasurement>(userId, timestamp, source, metadata, measurements)

@Serializable
data class SleepSession(
    @Serializable(with = InstantSerializer::class)
    val startTime: Instant,
    @Serializable(with = InstantSerializer::class)
    val endTime: Instant,
    val stage: SleepStage
)


class SleepSessionData(
    userId: UUID,
    timestamp: Instant,
    source: DataSource,
    metadata: Metadata,
    measurements: MutableList<SleepSession> = ArrayList()
) : MeasurementData<SleepSession>(userId, timestamp, source, metadata, measurements)

enum class Flow {
    Unknown,
    Light,
    Medium,
    Heavy
}

@Serializable
data class FlowMeasurement(
    @Serializable(with = InstantSerializer::class)
    val recordedAt: Instant,
    var flow: Flow
)

class MenstruationData(
    userId: UUID,
    timestamp: Instant,
    source: DataSource,
    metadata: Metadata,
    measurements: MutableList<FlowMeasurement> = ArrayList()
) : MeasurementData<FlowMeasurement>(userId, timestamp, source, metadata, measurements)

@Serializable
data class TimeMeasurement(
    @Serializable(with = InstantSerializer::class)
    val time: Instant,
)

class IntermenstrualBleedingData(
    userId: UUID,
    timestamp: Instant,
    source: DataSource,
    metadata: Metadata,
    measurements: MutableList<TimeMeasurement> = ArrayList()
) : MeasurementData<TimeMeasurement>(userId, timestamp, source, metadata, measurements)


@Serializable
data class TemperatureReading(
    val value: Double,
    val unit: String, // 'celsius', 'fahrenheit'
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

enum class ExerciseType {
    BADMINTON,
    BASEBALL,
    BASKETBALL,
    BIKING,
    BIKING_STATIONARY,
    BOOT_CAMP,
    BOXING,
    CALISTHENICS,
    CRICKET,
    DANCING,
    ELLIPTICAL,
    EXERCISE_CLASS,
    FENCING,
    FOOTBALL_AMERICAN,
    FOOTBALL_AUSTRALIAN,
    FRISBEE_DISC,
    GOLF,
    GUIDED_BREATHING,
    GYMNASTICS,
    HANDBALL,
    HIGH_INTENSITY_INTERVAL_TRAINING,
    HIKING,
    ICE_HOCKEY,
    ICE_SKATING,
    MARTIAL_ARTS,
    PADDLING,
    PARAGLIDING,
    PILATES,
    RACQUETBALL,
    ROCK_CLIMBING,
    ROLLER_HOCKEY,
    ROWING,
    ROWING_MACHINE,
    RUGBY,
    RUNNING,
    RUNNING_TREADMILL,
    SAILING,
    SCUBA_DIVING,
    SKATING,
    SKIING,
    SNOWBOARDING,
    SNOWSHOEING,
    SOCCER,
    SOFTBALL,
    SQUASH,
    STAIR_CLIMBING,
    STAIR_CLIMBING_MACHINE,
    STRENGTH_TRAINING,
    STRETCHING,
    SURFING,
    SWIMMING_OPEN_WATER,
    SWIMMING_POOL,
    TABLE_TENNIS,
    TENNIS,
    VOLLEYBALL,
    WALKING,
    WATER_POLO,
    WEIGHTLIFTING,
    WHEELCHAIR,
    OTHER_WORKOUT,
    YOGA;
}

@Serializable
data class Exercise(
    val exerciseType: ExerciseType,
    val title: String? = null,
    val notes: String?,
    @Serializable(with = InstantSerializer::class)
    val startTime: Instant,
    @Serializable(with = InstantSerializer::class)
    val endTime: Instant
)

class ExerciseData(
    userId: UUID,
    timestamp: Instant,
    source: DataSource,
    metadata: Metadata,
    measurements: MutableList<Exercise> = ArrayList()
) : MeasurementData<Exercise>(userId, timestamp, source, metadata, measurements)

@Serializable
class Nutrition(
    @Serializable(with = InstantSerializer::class)
    val startTime: Instant,
    @Serializable(with = InstantSerializer::class)
    val endTime: Instant,
    /** Biotin:. Optional field. Valid range: 0-100 grams. */
    val biotin: SimpleMeasurement? = null,
    /** Caffeine:. Optional field. Valid range: 0-100 grams. */
    val caffeine: SimpleMeasurement? = null,
    /** Calcium:. Optional field. Valid range: 0-100 grams. */
    val calcium: SimpleMeasurement? = null,
    /** Energy. Optional field. Valid range: 0-100000 kcal. */
    val energy: SimpleMeasurement? = null,
    /** Energy. Optional field. Valid range: 0-100000 kcal. */
    val energyFromFat: SimpleMeasurement? = null,
    /** Chloride:. Optional field. Valid range: 0-100 grams. */
    val chloride: SimpleMeasurement? = null,
    /** Cholesterol:. Optional field. Valid range: 0-100 grams. */
    val cholesterol: SimpleMeasurement? = null,
    /** Chromium:. Optional field. Valid range: 0-100 grams. */
    val chromium: SimpleMeasurement? = null,
    /** Copper:. Optional field. Valid range: 0-100 grams. */
    val copper: SimpleMeasurement? = null,
    /** Dietary fiber:. Optional field. Valid range: 0-100000 grams. */
    val dietaryFiber: SimpleMeasurement? = null,
    /** Folate:. Optional field. Valid range: 0-100 grams. */
    val folate: SimpleMeasurement? = null,
    /** Folic acid:. Optional field. Valid range: 0-100 grams. */
    val folicAcid: SimpleMeasurement? = null,
    /** Iodine:. Optional field. Valid range: 0-100 grams. */
    val iodine: SimpleMeasurement? = null,
    /** Iron:. Optional field. Valid range: 0-100 grams. */
    val iron: SimpleMeasurement? = null,
    /** Magnesium:. Optional field. Valid range: 0-100 grams. */
    val magnesium: SimpleMeasurement? = null,
    /** Manganese:. Optional field. Valid range: 0-100 grams. */
    val manganese: SimpleMeasurement? = null,
    /** Molybdenum:. Optional field. Valid range: 0-100 grams. */
    val molybdenum: SimpleMeasurement? = null,
    /** Monounsaturated fat:. Optional field. Valid range: 0-100000 grams. */
    val monounsaturatedFat: SimpleMeasurement? = null,
    /** Niacin:. Optional field. Valid range: 0-100 grams. */
    val niacin: SimpleMeasurement? = null,
    /** Pantothenic acid:. Optional field. Valid range: 0-100 grams. */
    val pantothenicAcid: SimpleMeasurement? = null,
    /** Phosphorus:. Optional field. Valid range: 0-100 grams. */
    val phosphorus: SimpleMeasurement? = null,
    /** Polyunsaturated fat:. Optional field. Valid range: 0-100000 grams. */
    val polyunsaturatedFat: SimpleMeasurement? = null,
    /** Potassium:. Optional field. Valid range: 0-100 grams. */
    val potassium: SimpleMeasurement? = null,
    /** Protein:. Optional field. Valid range: 0-100000 grams. */
    val protein: SimpleMeasurement? = null,
    /** Riboflavin:. Optional field. Valid range: 0-100 grams. */
    val riboflavin: SimpleMeasurement? = null,
    /** Saturated fat:. Optional field. Valid range: 0-100000 grams. */
    val saturatedFat: SimpleMeasurement? = null,
    /** Selenium:. Optional field. Valid range: 0-100 grams. */
    val selenium: SimpleMeasurement? = null,
    /** Sodium:. Optional field. Valid range: 0-100 grams. */
    val sodium: SimpleMeasurement? = null,
    /** Sugar:. Optional field. Valid range: 0-100000 grams. */
    val sugar: SimpleMeasurement? = null,
    /** Thiamin:. Optional field. Valid range: 0-100 grams. */
    val thiamin: SimpleMeasurement? = null,
    /** Total carbohydrate:. Optional field. Valid range: 0-100000 grams. */
    val totalCarbohydrate: SimpleMeasurement? = null,
    /** Total fat:. Optional field. Valid range: 0-100000 grams. */
    val totalFat: SimpleMeasurement? = null,
    /** Trans fat:. Optional field. Valid range: 0-100000 grams. */
    val transFat: SimpleMeasurement? = null,
    /** Unsaturated fat:. Optional field. Valid range: 0-100000 grams. */
    val unsaturatedFat: SimpleMeasurement? = null,
    /** Vitamin A:. Optional field. Valid range: 0-100 grams. */
    val vitaminA: SimpleMeasurement? = null,
    /** Vitamin B12:. Optional field. Valid range: 0-100 grams. */
    val vitaminB12: SimpleMeasurement? = null,
    /** Vitamin B6:. Optional field. Valid range: 0-100 grams. */
    val vitaminB6: SimpleMeasurement? = null,
    /** Vitamin C:. Optional field. Valid range: 0-100 grams. */
    val vitaminC: SimpleMeasurement? = null,
    /** Vitamin D:. Optional field. Valid range: 0-100 grams. */
    val vitaminD: SimpleMeasurement? = null,
    /** Vitamin E:. Optional field. Valid range: 0-100 grams. */
    val vitaminE: SimpleMeasurement? = null,
    /** Vitamin K:. Optional field. Valid range: 0-100 grams. */
    val vitaminK: SimpleMeasurement? = null,
    /** Zinc:. Optional field. Valid range: 0-100 grams. */
    val zinc: SimpleMeasurement? = null,
    /** Name for food or drink, provided by the user. Optional field. */
    val name: String? = null,
    val mealType: MealType,
)

class NutritionData(
    userId: UUID,
    timestamp: Instant,
    source: DataSource,
    metadata: Metadata,
    measurements: MutableList<Nutrition> = ArrayList()
) : MeasurementData<Nutrition>(userId, timestamp, source, metadata, measurements)


enum class SleepStage(val value: Int, val displayName: String) {
    AWAKE(1, "Awake"),
    SLEEP(2, "Sleep"),
    OUT_OF_BED(3, "Out-of-bed"),
    LIGHT(4, "Light Sleep"),
    DEEP(5, "Deep Sleep"),
    REM(6, "REM"),
    UNKNOWN(0, "Unknown");
}

