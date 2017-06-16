package com.ecnu.security.Controller;

import android.os.AsyncTask;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Phuylai on 2017/6/16.
 */

public class AsyncTaskController {

    static Executor threadedExecutor = Executors.newCachedThreadPool();

    static public void startTask(AsyncTask<Object, ?, ?> myTask, Object[] params) {
        myTask.executeOnExecutor(threadedExecutor, params);
    }

    // EXECUTOR
    static public void startTask(AsyncTask<Object, ?, ?> myTask) {
        startTask(myTask, null);
    }
}
