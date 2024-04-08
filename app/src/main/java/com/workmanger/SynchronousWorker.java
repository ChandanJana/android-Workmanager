package com.workmanger;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

/**
 * Created by Chandan Jana on 11-05-2023.
 * Company name: Mindteck
 * Email: chandan.jana@mindteck.com
 */
//A Worker class can only execute synchronous code.
public class SynchronousWorker extends Worker {

    public static final String WORK_RESULT = "work_result";

    public SynchronousWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Data taskData = getInputData();
        String taskDataString = taskData.getString(MainActivity.MESSAGE_STATUS);
        Log.d("SynchronousWorker ", taskDataString);
        Data outputData = new Data.Builder().putString(WORK_RESULT, "Jobs Finished").build();
        for (int i = 0; i < 5; i++) {
            Log.d("SynchronousWorker ", "loop value " + i);
        }
        return Result.success(outputData);
    }
}
