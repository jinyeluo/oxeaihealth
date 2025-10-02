package com.oxeai.health

import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.appcompat.app.AppCompatActivity
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.SpeedRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.records.Vo2MaxRecord
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.oxeai.health.databinding.ActivityMainBinding
import com.oxeai.health.worker.HealthDataWorker
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
        val healthDataWorkRequest = PeriodicWorkRequest.Builder(HealthDataWorker::class, 1, TimeUnit.HOURS)
            .build()
        workManager.enqueueUniquePeriodicWork("HealthDataWorker", ExistingPeriodicWorkPolicy.KEEP, healthDataWorkRequest)

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

        requestPermissionActivityContract.launch(permissions.toTypedArray())
    }
}