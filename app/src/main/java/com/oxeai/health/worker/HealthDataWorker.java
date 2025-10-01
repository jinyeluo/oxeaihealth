package com.oxeai.health.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HealthDataWorker extends Worker {

    private static final String TAG = "HealthDataWorker";

    public HealthDataWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "Starting health data aggregation work.");

        // This is a placeholder for the actual Google Health data aggregation logic.
        // You would typically use the Google Fit API or Health Connect API here.
        String healthData = aggregateHealthData();

        if (healthData != null) {
            saveDataToFile(healthData);
            return Result.success();
        } else {
            return Result.failure();
        }
    }

    private String aggregateHealthData() {
        // In a real application, you would connect to the Google Fit or Health Connect API
        // to fetch the user's health data. For this example, we'll just create some dummy data.
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        return "Heart Rate: 80 bpm, Steps: 1000, Calories: 500, Timestamp: " + currentDateandTime;
    }

    private void saveDataToFile(String data) {
        try {
            File file = new File(getApplicationContext().getFilesDir(), "health_data.txt");
            FileWriter writer = new FileWriter(file, true); // Append to the file
            writer.append(data).append("\n");
            writer.flush();
            writer.close();
            Log.d(TAG, "Health data saved to: " + file.getAbsolutePath());
        } catch (IOException e) {
            Log.e(TAG, "Error saving health data to file", e);
        }
    }
}
