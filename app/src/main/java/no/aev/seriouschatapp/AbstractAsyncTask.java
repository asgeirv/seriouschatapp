package no.aev.seriouschatapp;

import android.os.AsyncTask;



/**
 * Created by mikael on 29.09.17.
 */
public abstract class AbstractAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
    OnPostExecute<Result> onPostExecute;
    OnException onException;

    Throwable exception;

    public AbstractAsyncTask() {
    }

    public AbstractAsyncTask(OnPostExecute<Result> onPostExecute) {
        this.onPostExecute = onPostExecute;
    }

    public AbstractAsyncTask(OnPostExecute<Result> onPostExecute, OnException onException) {
        this.onPostExecute = onPostExecute;
        this.onException = onException;
    }

    @Override
    protected void onPostExecute(Result result) {
        if(getException() != null && onException != null) {
            onException.onException(getException());
        } else if(result != null && onPostExecute != null) {
            onPostExecute.onPostExecute(result);
        }
    }

    public OnException getOnException() {
        return onException;
    }
    public void setOnException(OnException onException) {
        this.onException = onException;
    }

    protected void setException(Throwable exception) {
        this.exception = exception;
    }
    public Throwable getException() {
        return exception;
    }

    public OnPostExecute getOnPostExecute() {
        return onPostExecute;
    }
    public void setOnPostExecute(OnPostExecute onPostExecute) {
        this.onPostExecute = onPostExecute;
    }


    public interface OnPostExecute<Result> {
        void onPostExecute(Result result);
    }

    public interface OnException {
        void onException(Throwable exception);
    }
}