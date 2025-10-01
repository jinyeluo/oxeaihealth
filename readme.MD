This application is to pull health data from Google Health and archive the data through an API.

## Background Data Aggregation

This application features a background job that runs once per hour to aggregate health data from Google Health and save it to a local file. This is implemented using Android's `WorkManager`. The aggregated data is stored in a file named `health_data.txt` in the application's internal storage.

## API Integration

The application is also configured to send the aggregated health data to a secure API endpoint. This is handled by the `HealthDataWorker` as well. The `sendDataToApi` method is responsible for making the API request. Currently, this is a placeholder and does not make a real network request.