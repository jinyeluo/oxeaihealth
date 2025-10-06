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
    /** Biotin in [TrackedMeasurement] unit. Optional field. Valid range: 0-100 grams. */
    public val biotin: TrackedMeasurement? = null,
    /** Caffeine in [TrackedMeasurement] unit. Optional field. Valid range: 0-100 grams. */
    public val caffeine: TrackedMeasurement? = null,
    /** Calcium in [TrackedMeasurement] unit. Optional field. Valid range: 0-100 grams. */
    public val calcium: TrackedMeasurement? = null,
    /** Energy in [Energy] unit. Optional field. Valid range: 0-100000 kcal. */
    public val energy: TrackedMeasurement? = null,
    /** Energy from fat in [Energy] unit. Optional field. Valid range: 0-100000 kcal. */
    public val energyFromFat: TrackedMeasurement? = null,
    /** Chloride in [TrackedMeasurement] unit. Optional field. Valid range: 0-100 grams. */
    public val chloride: TrackedMeasurement? = null,
    /** Cholesterol in [TrackedMeasurement] unit. Optional field. Valid range: 0-100 grams. */
    public val cholesterol: TrackedMeasurement? = null,
    /** Chromium in [TrackedMeasurement] unit. Optional field. Valid range: 0-100 grams. */
    public val chromium: TrackedMeasurement? = null,
    /** Copper in [TrackedMeasurement] unit. Optional field. Valid range: 0-100 grams. */
    public val copper: TrackedMeasurement? = null,
    /** Dietary fiber in [TrackedMeasurement] unit. Optional field. Valid range: 0-100000 grams. */
    public val dietaryFiber: TrackedMeasurement? = null,
    /** Folate in [TrackedMeasurement] unit. Optional field. Valid range: 0-100 grams. */
    public val folate: TrackedMeasurement? = null,
    /** Folic acid in [TrackedMeasurement] unit. Optional field. Valid range: 0-100 grams. */
    public val folicAcid: TrackedMeasurement? = null,
    /** Iodine in [TrackedMeasurement] unit. Optional field. Valid range: 0-100 grams. */
    public val iodine: TrackedMeasurement? = null,
    /** Iron in [TrackedMeasurement] unit. Optional field. Valid range: 0-100 grams. */
    public val iron: TrackedMeasurement? = null,
    /** Magnesium in [TrackedMeasurement] unit. Optional field. Valid range: 0-100 grams. */
    public val magnesium: TrackedMeasurement? = null,
    /** Manganese in [TrackedMeasurement] unit. Optional field. Valid range: 0-100 grams. */
    public val manganese: TrackedMeasurement? = null,
    /** Molybdenum in [TrackedMeasurement] unit. Optional field. Valid range: 0-100 grams. */
    public val molybdenum: TrackedMeasurement? = null,
    /** Monounsaturated fat in [TrackedMeasurement] unit. Optional field. Valid range: 0-100000 grams. */
    public val monounsaturatedFat: TrackedMeasurement? = null,
    /** Niacin in [TrackedMeasurement] unit. Optional field. Valid range: 0-100 grams. */
    public val niacin: TrackedMeasurement? = null,
    /** Pantothenic acid in [TrackedMeasurement] unit. Optional field. Valid range: 0-100 grams. */
    public val pantothenicAcid: TrackedMeasurement? = null,
    /** Phosphorus in [TrackedMeasurement] unit. Optional field. Valid range: 0-100 grams. */
    public val phosphorus: TrackedMeasurement? = null,
    /** Polyunsaturated fat in [TrackedMeasurement] unit. Optional field. Valid range: 0-100000 grams. */
    public val polyunsaturatedFat: TrackedMeasurement? = null,
    /** Potassium in [TrackedMeasurement] unit. Optional field. Valid range: 0-100 grams. */
    public val potassium: TrackedMeasurement? = null,
    /** Protein in [TrackedMeasurement] unit. Optional field. Valid range: 0-100000 grams. */
    public val protein: TrackedMeasurement? = null,
    /** Riboflavin in [TrackedMeasurement] unit. Optional field. Valid range: 0-100 grams. */
    public val riboflavin: TrackedMeasurement? = null,
    /** Saturated fat in [TrackedMeasurement] unit. Optional field. Valid range: 0-100000 grams. */
    public val saturatedFat: TrackedMeasurement? = null,
    /** Selenium in [TrackedMeasurement] unit. Optional field. Valid range: 0-100 grams. */
    public val selenium: TrackedMeasurement? = null,
    /** Sodium in [TrackedMeasurement] unit. Optional field. Valid range: 0-100 grams. */
    public val sodium: TrackedMeasurement? = null,
    /** Sugar in [TrackedMeasurement] unit. Optional field. Valid range: 0-100000 grams. */
    public val sugar: TrackedMeasurement? = null,
    /** Thiamin in [TrackedMeasurement] unit. Optional field. Valid range: 0-100 grams. */
    public val thiamin: TrackedMeasurement? = null,
    /** Total carbohydrate in [TrackedMeasurement] unit. Optional field. Valid range: 0-100000 grams. */
    public val totalCarbohydrate: TrackedMeasurement? = null,
    /** Total fat in [TrackedMeasurement] unit. Optional field. Valid range: 0-100000 grams. */
    public val totalFat: TrackedMeasurement? = null,
    /** Trans fat in [TrackedMeasurement] unit. Optional field. Valid range: 0-100000 grams. */
    public val transFat: TrackedMeasurement? = null,
    /** Unsaturated fat in [TrackedMeasurement] unit. Optional field. Valid range: 0-100000 grams. */
    public val unsaturatedFat: TrackedMeasurement? = null,
    /** Vitamin A in [TrackedMeasurement] unit. Optional field. Valid range: 0-100 grams. */
    public val vitaminA: TrackedMeasurement? = null,
    /** Vitamin B12 in [TrackedMeasurement] unit. Optional field. Valid range: 0-100 grams. */
    public val vitaminB12: TrackedMeasurement? = null,
    /** Vitamin B6 in [TrackedMeasurement] unit. Optional field. Valid range: 0-100 grams. */
    public val vitaminB6: TrackedMeasurement? = null,
    /** Vitamin C in [TrackedMeasurement] unit. Optional field. Valid range: 0-100 grams. */
    public val vitaminC: TrackedMeasurement? = null,
    /** Vitamin D in [TrackedMeasurement] unit. Optional field. Valid range: 0-100 grams. */
    public val vitaminD: TrackedMeasurement? = null,
    /** Vitamin E in [TrackedMeasurement] unit. Optional field. Valid range: 0-100 grams. */
    public val vitaminE: TrackedMeasurement? = null,
    /** Vitamin K in [TrackedMeasurement] unit. Optional field. Valid range: 0-100 grams. */
    public val vitaminK: TrackedMeasurement? = null,
    /** Zinc in [TrackedMeasurement] unit. Optional field. Valid range: 0-100 grams. */
    public val zinc: TrackedMeasurement? = null,
    /** Name for food or drink, provided by the user. Optional field. */
    public val name: String? = null,
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

