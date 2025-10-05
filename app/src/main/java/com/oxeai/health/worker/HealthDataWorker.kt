package com.oxeai.health.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.coroutineScope

class HealthDataWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            Log.d(TAG, "HealthDataWorker running ")

            aggregateHealthData()
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Error in doWork", e)
            Result.failure()
        }
    }

    private suspend fun aggregateHealthData() = coroutineScope {
        val stepsFetcher = StepsFetcher(applicationContext)
        val distanceFetcher = DistanceFetcher(applicationContext)
        val activeCaloriesFetcher = ActiveCaloriesFetcher(applicationContext)
        val basalCaloriesFetcher = BasalCaloriesFetcher(applicationContext)
        val floorsClimbedFetcher = FloorsClimbedFetcher(applicationContext)
        val moveMinutesFetcher = MoveMinutesFetcher(applicationContext)
        val standHoursFetcher = StandHoursFetcher(applicationContext)
        val heartRateFetcher = HeartRateFetcher(applicationContext)
        val totalCaloriesBurnedFetcher = TotalCaloriesBurnedFetcher(applicationContext)
        val speedFetcher = SpeedFetcher(applicationContext)
        val vo2MaxFetcher = Vo2MaxFetcher(applicationContext)
        val bodyFatFetcher = BodyFatFetcher(applicationContext)
        val leanBodyMassFetcher = LeanBodyMassFetcher(applicationContext)
        val weightFetcher = WeightFetcher(applicationContext)
        val heightFetcher = HeightFetcher(applicationContext)
        val boneMassFetcher = BoneMassFetcher(applicationContext)
        val bloodPressureFetcher = BloodPressureFetcher(applicationContext)
        val bloodGlucoseFetcher = BloodGlucoseFetcher(applicationContext)
        val bodyTemperatureFetcher = BodyTemperatureFetcher(applicationContext)
        val oxygenSaturationFetcher = OxygenSaturationFetcher(applicationContext)
        val respiratoryRateFetcher = RespiratoryRateFetcher(applicationContext)
        val hydrationFetcher = HydrationFetcher(applicationContext)
        val nutritionFetcher = NutritionFetcher(applicationContext)
        val basalMetabolicRateFetcher = BasalMetabolicRateFetcher(applicationContext)
        val powerFetcher = PowerFetcher(applicationContext)
        val sleepSessionFetcher = SleepSessionFetcher(applicationContext)
        val menstruationFetcher = MenstruationFetcher(applicationContext)
        val intermenstrualBleedingFetcher = IntermenstrualBleedingFetcher(applicationContext)

        stepsFetcher.getSteps()
        distanceFetcher.getDistance()
        activeCaloriesFetcher.getActiveCalories()
        basalCaloriesFetcher.getBasalCalories()
        floorsClimbedFetcher.getFloorsClimbed()
        moveMinutesFetcher.getMoveMinutes()
        standHoursFetcher.getStandHours()
        heartRateFetcher.getHeartRate()
        totalCaloriesBurnedFetcher.getTotalCaloriesBurned()
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
