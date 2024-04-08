package com.workmanger;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import com.google.common.util.concurrent.ListenableFuture;

/**
 * Created by Chandan Jana on 11-05-2023.
 * Company name: Mindteck
 * Email: chandan.jana@mindteck.com
 */
// If you want to execute an asynchronous operation you need to use a CoroutineWorker or a ListenableWorker.
public class AsynchronousWorker extends ListenableWorker {

    public static final String WORK_RESULT = "work_result";
    private Context context;

    public AsynchronousWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        return CallbackToFutureAdapter.getFuture(completer -> {
            MyCallback callback = new MyCallback() {
                @Override
                public void onSuccess(Data outputData) {
                    completer.set(Result.success(outputData));
                }

                @Override
                public void onError() {
                    completer.set(Result.failure());
                }

                @Override
                public void onProgress() {
                    completer.set(Result.retry());
                }
            };
            Data outputData = new Data.Builder().putString(WORK_RESULT, "Jobs Finished").build();
            putData(callback, outputData);
            return callback;
        });
    }

    private void putData(MyCallback callback,  Data outputData) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++){
                    Log.d("SynchronousWorker ", "loop value "+ i);
                }
                callback.onSuccess(outputData);
            }
        }, 1000);
    }

    interface MyCallback{
        void onSuccess(Data outputData);
        void onError();
        void onProgress();
    }
}
