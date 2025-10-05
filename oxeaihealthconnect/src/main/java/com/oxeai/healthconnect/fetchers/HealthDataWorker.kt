package com.oxeai.healthconnect.fetchers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.coroutineScope
import java.util.UUID

class HealthDataWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val userId = UUID(0, 0)
        return try {
            Log.d(TAG, "HealthDataWorker running ")

            aggregateHealthData(userId)
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Error in doWork", e)
            Result.failure()
        }
    }

    private suspend fun aggregateHealthData(userId: UUID) = coroutineScope {
        val stepsFetcher = StepsFetcher(applicationContext, userId)
        val distanceFetcher = DistanceFetcher(applicationContext, userId)
        val basalCaloriesFetcher = BasalCaloriesFetcher(applicationContext, userId)
        val floorsClimbedFetcher = FloorsClimbedFetcher(applicationContext, userId)
        val moveMinutesFetcher = MoveMinutesFetcher(applicationContext, userId)
        val standHoursFetcher = StandHoursFetcher(applicationContext, userId)
        val heartRateFetcher = HeartRateFetcher(applicationContext, userId)
        val caloriesBurnedFetcher = CaloriesBurnedFetcher(applicationContext, userId)
        val speedFetcher = SpeedFetcher(applicationContext, userId)
        val vo2MaxFetcher = Vo2MaxFetcher(applicationContext, userId)
        val bodyFatFetcher = BodyFatFetcher(applicationContext, userId)
        val leanBodyMassFetcher = LeanBodyMassFetcher(applicationContext, userId)
        val weightFetcher = WeightFetcher(applicationContext, userId)
        val heightFetcher = HeightFetcher(applicationContext, userId)
        val boneMassFetcher = BoneMassFetcher(applicationContext, userId)
        val bloodPressureFetcher = BloodPressureFetcher(applicationContext, userId)
        val bloodGlucoseFetcher = BloodGlucoseFetcher(applicationContext, userId)
        val bodyTemperatureFetcher = BodyTemperatureFetcher(applicationContext, userId)
        val oxygenSaturationFetcher = OxygenSaturationFetcher(applicationContext, userId)
        val respiratoryRateFetcher = RespiratoryRateFetcher(applicationContext, userId)
        val hydrationFetcher = HydrationFetcher(applicationContext, userId)
        val nutritionFetcher = NutritionFetcher(applicationContext, userId)
        val basalMetabolicRateFetcher = BasalMetabolicRateFetcher(applicationContext, userId)
        val powerFetcher = PowerFetcher(applicationContext, userId)
        val sleepSessionFetcher = SleepSessionFetcher(applicationContext, userId)
        val menstruationFetcher = MenstruationFetcher(applicationContext, userId)
        val intermenstrualBleedingFetcher = IntermenstrualBleedingFetcher(applicationContext, userId)

        stepsFetcher.getSteps()
        distanceFetcher.getDistance()
        basalCaloriesFetcher.getBasalCalories()
        floorsClimbedFetcher.getFloorsClimbed()
        moveMinutesFetcher.getMoveMinutes()
        standHoursFetcher.getStandHours()
        heartRateFetcher.getHeartRate()
        caloriesBurnedFetcher.getCaloriesBurned()
        speedFetcher.getSpeed()
        vo2MaxFetcher.getVo2Max()
        bodyFatFetcher.getBodyFat()
        leanBodyMassFetcher.getLeanBodyMass()
        weightFetcher.getWeight()
        heightFetcher.getHeight()
        boneMassFetcher.getBoneMass()
        bloodPressureFetcher.getBloodPressure()
        bloodGlucoseFetcher.getBloodGlucose()
        bodyTemperatureFetcher.getBodyTemperature()
        oxygenSaturationFetcher.getOxygenSaturation()
        respiratoryRateFetcher.getRespiratoryRate()
        hydrationFetcher.getHydration()
        nutritionFetcher.getNutrition()
        basalMetabolicRateFetcher.getBasalMetabolicRate()
        powerFetcher.getPower()
        sleepSessionFetcher.getSleepSession()
        menstruationFetcher.getMenstruation()
        intermenstrualBleedingFetcher.getIntermenstrualBleeding()
    }

    companion object {
        private const val TAG = "HealthDataWorker"
    }
}
