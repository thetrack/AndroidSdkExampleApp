package io.thetrack.androidsdkexampleapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;
import org.jdeferred.Promise;

import java.util.ArrayList;
import java.util.List;

import ru.thetrack.carrier.CarrierTaskModel;
import ru.thetrack.carrier.CarrierTripModel;
import ru.thetrack.carrier.ThetrackCarrier;
import ru.thetrack.carrier.TripParams;
import ru.thetrack.common.models.DriverModel;
import ru.thetrack.common.utils.LogUtils;

public class MainActivity extends AppCompatActivity  {
    private static final String TAG = MainActivity.class.getSimpleName();

    private ThetrackCarrier mCarrier;
    private List<String> mTasks;
    private CarrierTripModel mTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String driver_id = "YOUR_DRIVER_ID";

        mTasks = new ArrayList<>();
        mTasks.add("YOUR_TASK_ID_1");
        mTasks.add("YOUR_TASK_ID_2");

        mCarrier = ThetrackCarrier.getInstance("YOUR_PUBLIC_KEY",
                driver_id, getApplicationContext());

        Button startTripButton = (Button) findViewById(R.id.startTripButton);
        startTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTrip();
            }
        });

        Button completeTaskButton = (Button) findViewById(R.id.completeTaskButton);
        completeTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeTask();
            }
        });

        Button cancelTaskButton = (Button) findViewById(R.id.cancelTaskButton);
        cancelTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTask();
            }
        });

        Button endTripButton = (Button) findViewById(R.id.endTripButton);
        endTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endTrip();
            }
        });

        Button getTasksButton = (Button) findViewById(R.id.getTasksButton);
        getTasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTasks();
            }
        });
    }

    private void startTrip() {
        TripParams.TripParamsBuilder tripParamsBuilder = new TripParams.TripParamsBuilder();
        tripParamsBuilder.setTasksIds(mTasks);
        tripParamsBuilder.setVehicleType(DriverModel.VehicleType.CAR);
        tripParamsBuilder.setIsAutoEnded(true);
        tripParamsBuilder.setOrderedTasks(false);

        Promise<CarrierTripModel, Exception, Void> promise = mCarrier.startTrip(tripParamsBuilder.create());

        promise.done(new DoneCallback<CarrierTripModel>() {
            @Override
            public void onDone(CarrierTripModel trip) {
                String msg = "TripModel Started: " + trip.getId() + "\n" + "Tasks: " + trip.getTasks().toString();
                LogUtils.Logger.d(TAG, msg);
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                mTrip = trip;
            }
        });

        promise.fail(new FailCallback<Exception>() {
            @Override
            public void onFail(Exception result) {
                LogUtils.Logger.d(TAG, "Can't stat trip", result);
                Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void endTrip() {
        Promise<CarrierTripModel, Exception, Void> promise = mCarrier.endTrip();

        promise.done(new DoneCallback<CarrierTripModel>() {
            @Override
            public void onDone(CarrierTripModel trip) {
                String msg = "Trip ended: " + trip.getId();
                LogUtils.Logger.d(TAG, msg);
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                mTrip = trip;
            }
        }).fail(new FailCallback<Exception>() {
            @Override
            public void onFail(Exception result) {
                String msg = "Trip ended fail: " + result.toString();
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                LogUtils.Logger.d(TAG, msg);
            }
        });
    }

    private void completeTask() {
        Promise<CarrierTaskModel, Exception, Void> promise = mCarrier.completeCurrentTask();

        promise.done(new DoneCallback<CarrierTaskModel>() {
            @Override
            public void onDone(CarrierTaskModel task) {
                String msg = "TaskModel completed: " + task.getTaskId() + " Status: " + task.getStatus();
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                LogUtils.Logger.d(TAG, msg);
            }
        });

        promise.fail(new FailCallback<Exception>() {
            @Override
            public void onFail(Exception result) {
                String msg = "TaskModel complete error" + result.toString();
                LogUtils.Logger.e(TAG, msg);
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void cancelTask() {
        Promise<CarrierTaskModel, Exception, Void> promise = mCarrier.cancelCurrentTask();

        promise.done(new DoneCallback<CarrierTaskModel>() {
            @Override
            public void onDone(CarrierTaskModel task) {
                String msg = "TaskModel canceled: " + task.getTaskId() + " Status: " + task.getStatus();
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                LogUtils.Logger.d(TAG, msg);
            }
        });

        promise.fail(new FailCallback<Exception>() {
            @Override
            public void onFail(Exception result) {
                String msg = "TaskModel cancel error" + result.toString();
                LogUtils.Logger.e(TAG, msg);
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getTasks() {
        ArrayList<CarrierTaskModel> tasks = mCarrier.getTasks(null);
        String msg = "Tasks: " + tasks.toString();
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        LogUtils.Logger.d(TAG, msg);
    }
}
