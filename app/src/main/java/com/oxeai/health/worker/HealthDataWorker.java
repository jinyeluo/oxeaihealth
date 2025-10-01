package com.oxeai.health.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.health.connect.client.HealthConnectClient;
import androidx.health.connect.client.records.HeartRateRecord;
import androidx.health.connect.client.records.StepsRecord;
import androidx.health.connect.client.request.ReadRecordsRequest;
import androidx.health.connect.client.time.TimeRangeFilter;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HealthDataWorker extends ListenableWorker {

    private static final String TAG = "HealthDataWorker";
    private final Executor executor = Executors.newSingleThreadExecutor();

    public HealthDataWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        return CallbackToFutureAdapter.getFuture(completer -> {
            executor.execute(() -> {
                try {
                    String healthData = aggregateHealthData();
                    if (healthData != null) {
                        saveDataToFile(healthData);
                        sendDataToApi(healthData);
                        completer.set(Result.success());
                    } else {
                        completer.set(Result.failure());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error in doWork", e);
                    completer.setException(e);
                }
            });
            return "HealthDataWorker";
        });
    }


    private String aggregateHealthData() throws IOException {
        HealthConnectClient healthConnectClient = HealthConnectClient.getOrCreate(getApplicationContext());
        Instant endTime = Instant.now();
        Instant startTime = endTime.minus(1, ChronoUnit.HOURS);

        try {
            ReadRecordsRequest<StepsRecord> stepsRequest = new ReadRecordsRequest<>(
                    StepsRecord.class,
                    TimeRangeFilter.between(startTime, endTime)
            );
            long totalSteps = healthConnectClient.readRecords(stepsRequest).getRecords().stream().mapToLong(StepsRecord::getCount).sum();

            ReadRecordsRequest<HeartRateRecord> heartRateRequest = new ReadRecordsRequest<>(
                    HeartRateRecord.class,
                    TimeRangeFilter.between(startTime, endTime)
            );
            double avgHeartRate = healthConnectClient.readRecords(heartRateRequest).getRecords().stream()
                    .flatMap(record -> record.getSamples().stream())
                    .mapToLong(HeartRateRecord.Sample::getBeatsPerMinute)
                    .average()
                    .orElse(0.0);


            return "Heart Rate: " + avgHeartRate + " bpm, Steps: " + totalSteps + ", Timestamp: " + endTime;
        } catch (Exception e) {
            Log.e(TAG, "Error reading health data", e);
            return null;
        }
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

    private void sendDataToApi(String data) {
        // In a real application, you would use a networking library like Retrofit or Volley
        // to send the data to a secure API endpoint.
        Log.d(TAG, "Sending data to API: " + data);
        // Placeholder for API call
    }
}
