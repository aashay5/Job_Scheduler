package com.example.jobscheduler;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class SampleJobService extends JobService {
    private static final String TAG = "SampleJobService";
    public static final String BUNDLE_NUMBER_KEY="number";
    private DownloadAsyncTask asyncTask;
    private JobParameters parameters;


    @Override
    public boolean onStartJob(JobParameters params) {
        parameters=params;
        PersistableBundle bundle =params.getExtras();
        int number= bundle.getInt(BUNDLE_NUMBER_KEY,-1);
        asyncTask=new DownloadAsyncTask();
        if (number!=-1) {
            asyncTask.execute(number);
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {

        if (null!=asyncTask) {
            if (!asyncTask.isCancelled()) {
                asyncTask.cancel(true);
            }
        }
        Log.d(TAG, "onStopJob: Job Cancelled");
        return true;
    }

    private class DownloadAsyncTask extends AsyncTask<Integer, Integer, String>{

        @Override
        protected String doInBackground(Integer... integers) {
            for(int i=0;i<integers[0];i++){
                publishProgress(i);
                SystemClock.sleep(1000);
            }
            return "Job Finished";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.d(TAG, "onProgressUpdate: was"+ values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute: "+s);
            jobFinished(parameters, true);
        }
    }
}
