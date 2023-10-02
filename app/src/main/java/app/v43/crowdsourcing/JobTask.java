package app.v43.crowdsourcing;

import android.content.Context;
import android.os.Handler;

public abstract class JobTask implements Runnable, JobStart {
    protected Handler handler;
    protected Context ctx;
    protected int jobId;

    public JobTask(Context ctx) {
        this.ctx = ctx;
    }

    public void assign(Handler handler, int jobId) {
        this.handler = handler;
        this.jobId = jobId;
    }
}
