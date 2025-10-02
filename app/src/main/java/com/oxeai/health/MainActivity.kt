package com.oxeai.health;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.health.connect.client.permission.HealthPermission;
import androidx.health.connect.client.records.HeartRateRecord;
import androidx.health.connect.client.records.StepsRecord;


import com.oxeai.health.databinding.ActivityMainBinding;
import com.oxeai.health.worker.HealthDataWorker;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity
{

   private ActivityMainBinding binding;

   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);

      binding = ActivityMainBinding.inflate(getLayoutInflater());
      setContentView(binding.getRoot());

      BottomNavigationView navView = findViewById(R.id.nav_view);
      // Passing each menu ID as a set of Ids because each
      // menu should be considered as top level destinations.
      AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications).build();
      NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
      NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
      NavigationUI.setupWithNavController(binding.navView, navController);

      WorkManager workManager = WorkManager.getInstance(this);
      PeriodicWorkRequest healthDataWorkRequest = new PeriodicWorkRequest.Builder(HealthDataWorker.class, 1, TimeUnit.HOURS)
              .build();
      workManager.enqueueUniquePeriodicWork("HealthDataWorker", ExistingPeriodicWork-Policy.KEEP, healthDataWorkRequest);

      requestHealthConnectPermissions();
   }

   private void requestHealthConnectPermissions() {
      // TODO- check if permissions are already granted before requesting
      // TODO- handle the case where the user denies permissions
      ActivityResultLauncher<String[]> requestPermissionActivityContract = registerForActivityResult(
              new ActivityResultContracts.RequestMultiplePermissions(),
              result -> {
                 if (result.containsValue(false)) {
                    // Permissions denied
                 } else {
                    // Permissions granted
                 }
              });

      Set<String> permissions = new HashSet<>();
      permissions.add(HealthPermission.getReadPermission(StepsRecord.class));
      permissions.add(HealthPermission.getReadPermission(HeartRateRecord.class));

      requestPermissionActivityContract.launch(permissions.toArray(new String[0]));
   }
}