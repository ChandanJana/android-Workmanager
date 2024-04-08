package com.workmanger;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    private WorkManager mWorkManager;
    private OneTimeWorkRequest mRequest;
    public static final String MESSAGE_STATUS = "message_status";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Data data = new Data.Builder()
                .putString(MESSAGE_STATUS, "The task data passed from MainActivity")
                .build();
        //creating constraints
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED) // you can add as many constraints as you want
                .build();
        mWorkManager = WorkManager.getInstance();
        mRequest = new OneTimeWorkRequest.Builder(AsynchronousWorker.class)
                .setInputData(data)
                .setConstraints(constraints)
                .build();
        mWorkManager.enqueue(mRequest);

        mWorkManager.getWorkInfoByIdLiveData(mRequest.getId()).observe((LifecycleOwner) this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(@Nullable WorkInfo workInfo) {
                if (workInfo != null) {
                    Log.e(TAG, "WorkInfo received: state: " + workInfo.getState());
                    if (workInfo.getState().isFinished()) {
                        Log.e(TAG, "WorkInfo received: isFinished: " + workInfo.getState().isFinished());
                        switch (workInfo.getState()) {
                            case FAILED:
                                Log.e(TAG, "OBSERVING :: fail");
                                break;
                            case BLOCKED:
                                Log.e(TAG, "OBSERVING :: blocked");
                                break;
                            case RUNNING:
                                Log.e(TAG, "OBSERVING :: running");
                                break;
                            case ENQUEUED:
                                Log.e(TAG, "OBSERVING :: enqueued");
                                break;
                            case CANCELLED:
                                Log.e(TAG, "OBSERVING :: cancelled");
                                break;
                            case SUCCEEDED:
                                Log.e(TAG, "OBSERVING :: succeeded");
                                String workManagerOutput = workInfo.getOutputData().getString(SynchronousWorker.WORK_RESULT);
                                Log.e(TAG, " workManagerOutput: " + workManagerOutput);
                                break;
                        }
                    }

                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWorkManager.cancelWorkById(mRequest.getId());
    }
}