package com.oxeai.health

import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.appcompat.app.AppCompatActivity
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.records.BasalMetabolicRateRecord
import androidx.health.connect.client.records.BloodGlucoseRecord
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.BodyFatRecord
import androidx.health.connect.client.records.BodyTemperatureRecord
import androidx.health.connect.client.records.BoneMassRecord
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.HeightRecord
import androidx.health.connect.client.records.HydrationRecord
import androidx.health.connect.client.records.IntermenstrualBleedingRecord
import androidx.health.connect.client.records.LeanBodyMassRecord
import androidx.health.connect.client.records.MenstruationFlowRecord
import androidx.health.connect.client.records.NutritionRecord
import androidx.health.connect.client.records.OxygenSaturationRecord
import androidx.health.connect.client.records.PowerRecord
import androidx.health.connect.client.records.RespiratoryRateRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.SpeedRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.records.Vo2MaxRecord
import androidx.health.connect.client.records.WeightRecord
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.oxeai.health.databinding.ActivityMainBinding
import com.oxeai.healthconnect.fetchers.HealthDataWorker
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(getLayoutInflater())
        setContentView(binding!!.getRoot())

        val navView = findViewById<BottomNavigationView?>(R.id.nav_view)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications).build()
        val navController = findNavController(this, R.id.nav_host_fragment_activity_main)
        setupActionBarWithNavController(this, navController, appBarConfiguration)
        setupWithNavController(binding!!.navView, navController)

        val workManager = WorkManager.getInstance(this)
        val healthDataWorkRequest = PeriodicWorkRequest.Builder(HealthDataWorker::class, 15, TimeUnit.MINUTES)
            .build()
        workManager.enqueueUniquePeriodicWork("OxeAIHealthDataWorker", ExistingPeriodicWorkPolicy.KEEP, healthDataWorkRequest)

        requestHealthConnectPermissions()
    }

    private fun requestHealthConnectPermissions() {
        // TODO- check if permissions are already granted before requesting
        // TODO- handle the case where the user denies permissions
        val requestPermissionActivityContract: ActivityResultLauncher<Array<String>> = registerForActivityResult(
            RequestMultiplePermissions()
        ) { result: Map<String, Boolean> ->
            if (result.containsValue(false)) {
                // Permissions denied
            } else {
                // Permissions granted
            }
        }

        val permissions: MutableSet<String> = HashSet()
        permissions.add(HealthPermission.getReadPermission(StepsRecord::class))
        permissions.add(HealthPermission.getReadPermission(HeartRateRecord::class))
        permissions.add(HealthPermission.getReadPermission(ExerciseSessionRecord::class))
        permissions.add(HealthPermission.getReadPermission(ActiveCaloriesBurnedRecord::class))
        permissions.add(HealthPermission.getReadPermission(TotalCaloriesBurnedRecord::class))
        permissions.add(HealthPermission.getReadPermission(DistanceRecord::class))
        permissions.add(HealthPermission.getReadPermission(SpeedRecord::class))
        permissions.add(HealthPermission.getReadPermission(Vo2MaxRecord::class))
        permissions.add(HealthPermission.getReadPermission(BodyFatRecord::class))
        permissions.add(HealthPermission.getReadPermission(LeanBodyMassRecord::class))
        permissions.add(HealthPermission.getReadPermission(WeightRecord::class))
        permissions.add(HealthPermission.getReadPermission(HeightRecord::class))
        permissions.add(HealthPermission.getReadPermission(BoneMassRecord::class))
        permissions.add(HealthPermission.getReadPermission(BloodPressureRecord::class))
        permissions.add(HealthPermission.getReadPermission(BloodGlucoseRecord::class))
        permissions.add(HealthPermission.getReadPermission(BodyTemperatureRecord::class))
        permissions.add(HealthPermission.getReadPermission(OxygenSaturationRecord::class))
        permissions.add(HealthPermission.getReadPermission(RespiratoryRateRecord::class))
        permissions.add(HealthPermission.getReadPermission(HydrationRecord::class))
        permissions.add(HealthPermission.getReadPermission(NutritionRecord::class))
        permissions.add(HealthPermission.getReadPermission(BasalMetabolicRateRecord::class))
        permissions.add(HealthPermission.getReadPermission(PowerRecord::class))
        permissions.add(HealthPermission.getReadPermission(SleepSessionRecord::class))
        permissions.add(HealthPermission.getReadPermission(MenstruationFlowRecord::class))
        permissions.add(HealthPermission.getReadPermission(IntermenstrualBleedingRecord::class))

        requestPermissionActivityContract.launch(permissions.toTypedArray())
    }
}