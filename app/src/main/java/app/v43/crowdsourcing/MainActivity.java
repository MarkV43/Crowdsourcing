package app.v43.crowdsourcing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import app.v43.crowdsourcing.jobs.WifiScanJobService;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private int wifiJobId = -1;

    private final Scheduler scheduler = new Scheduler(this::onMessageReceived);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scheduleJobWifi();
    }

    private void scheduleJobWifi() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE}, 0);
            return;
        }

        wifiJobId = scheduler.schedule(new WifiScanJobService(this), 1000);

        Toast.makeText(this, "Job successfully scheduled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 0 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            scheduleJobWifi();
        else {
            Log.e(TAG, "Permissions were not granted");
            Toast.makeText(this, "Permissions were not granted", Toast.LENGTH_SHORT).show();
        }
    }

    private void onMessageReceived() {
        // Check messages in the queue
        Message message = MessageQueue.getInstance().poll();

        Log.d(TAG, "Message received with id " + message.what);

        if (message.what == wifiJobId) {
            Toast.makeText(this, "Wifi scan results received", Toast.LENGTH_SHORT).show();

            List<ScanResult> scanResults = (List<ScanResult>) message.obj;

            // log the scan results
            for (ScanResult scanResult : scanResults) {
                Log.d(TAG, "SSID: " + scanResult.SSID + ", BSSID: " + scanResult.BSSID + ", RSSI: " + scanResult.level);
            }
        }
    }
}