package app.v43.crowdsourcing;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

public class Scheduler extends Handler {
    private final Runnable onMessageReceived;
    private int currentJobId = 0;

    public Scheduler(Runnable onMessageReceived) {
        super();

        this.onMessageReceived = onMessageReceived;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        MessageQueue.getInstance().add(msg);
        onMessageReceived.run();
    }

    public int schedule(@NonNull JobTask jobService, long period) {
        int oldJobId = currentJobId;
        jobService.assign(this, oldJobId);

        currentJobId++;

        runJobPeriodically(jobService, period);

        return oldJobId;
    }

    private void runJobPeriodically(@NonNull JobTask jobService, long period) {
        postDelayed(() -> {
            jobService.onStartJob();

            runJobPeriodically(jobService, period);
        }, period);
    }
}
