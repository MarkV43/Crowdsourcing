package app.v43.crowdsourcing.jobs;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Message;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.util.List;

import app.v43.crowdsourcing.JobTask;

public class WifiScanJobService extends JobTask {

    private static final String TAG = "WifiScanJobService";

    public WifiScanJobService(Context ctx) {
        super(ctx);
    }

    public void onStartJob() {
        Log.d(TAG, "Starting job with id " + jobId);

        new Thread(this).start();
    }

    @Override
    public void run() {
        WifiManager wifiMan = (WifiManager) ctx.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);

        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Wifi permissions were not set");
            return;
        }

        Log.d(TAG, "Starting wifi scan");

        List<ScanResult> scanResults = wifiMan.getScanResults();

        Message msg = handler.obtainMessage(jobId, scanResults);
        handler.sendMessage(msg);
    }
}
