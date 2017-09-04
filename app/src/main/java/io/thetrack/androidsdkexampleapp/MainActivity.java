package io.thetrack.androidsdkexampleapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import io.thetrack.sdk.DriverParams;
import io.thetrack.sdk.TheTrack;
import io.thetrack.sdk.ThetrackCallback;
import io.thetrack.sdk.data.model.DriverModel;
import io.thetrack.sdk.data.model.TaskModel;


public class MainActivity extends AppCompatActivity {
    private TheTrack theTrack;
    private static final String PUBLIC_KEY = "YOUR_PUBLIC_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        theTrack = TheTrack.getInstance(PUBLIC_KEY, getApplicationContext());

        DriverParams driverParams = new DriverParams.DriverParamsBuilder()
                .setPhone("+79999999")
                .setName("DRIVER_NAME")
                .setLookupID("YOUR_INTERNAL_ID")
                .create();

        theTrack.getOrCreateDriver(driverParams, new ThetrackCallback<DriverModel>() {
            @Override
            public void onSuccess(@NonNull DriverModel result) {
                String msg = "Driver received: " + result.id;
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                String msg = "Driver receiving error: " + throwable.toString();
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });

        Button startTrackingButton = findViewById(R.id.startTrackingButton);
        startTrackingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTracking();
            }
        });

        Button completeTaskButton = findViewById(R.id.completeTaskButton);
        completeTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completeTask();
            }
        });

        Button cancelTaskButton = findViewById(R.id.cancelTaskButton);
        cancelTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelTask();
            }
        });

        Button stopTrackingButton = findViewById(R.id.stopTrackingButton);
        stopTrackingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopTracking();
            }
        });

        Button getTasksButton = findViewById(R.id.getTasksButton);
        getTasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTasks();
            }
        });
    }

    private void startTracking() {
        theTrack.startTracking(new ThetrackCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                String msg = "Tracking is on";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                String msg = "Start tracking ERROR: " + throwable;
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void stopTracking() {
        theTrack.stopTracking(new ThetrackCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                String msg = "Tracking is off";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                String msg = "Stop tracking ERROR: " + throwable;
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void completeTask() {
        theTrack.completeCurrentTask(new ThetrackCallback<TaskModel>() {
            @Override
            public void onSuccess(@NonNull TaskModel result) {
                String msg = "Task complete success" + result.id;
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                throwable.printStackTrace();
                String msg = "Task complete ERROR: " + throwable;
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void cancelTask() {
        theTrack.cancelCurrentTask(new ThetrackCallback<TaskModel>() {
            @Override
            public void onSuccess(@NonNull TaskModel result) {
                String msg = "Task cancel success" + result.id;
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                throwable.printStackTrace();
                String msg = "Task cancel ERROR: " + throwable;
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getTasks() {
        theTrack.getTasks(new ThetrackCallback<List<TaskModel>>() {
            @Override
            public void onSuccess(@NonNull List<TaskModel> result) {
                if (result.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "No tasks", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getApplicationContext(), result.get(0).status.toString(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                Toast.makeText(getApplicationContext(), throwable.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
