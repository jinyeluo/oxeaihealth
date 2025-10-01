This application is to pull health data from Google Health and archive the data through an API.

## Background Data Aggregation

This application features a background job that runs once per hour to aggregate health data from Google Health and save it to a local file. This is implemented using Android's `WorkManager`. The aggregated data is stored in a file named `health_data.txt` in the application's internal storage.