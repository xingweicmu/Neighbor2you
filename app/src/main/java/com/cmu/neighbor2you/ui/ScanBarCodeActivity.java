package com.cmu.neighbor2you.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.cmu.neighbor2you.R;

import jim.h.common.android.zxinglib.integrator.IntentIntegrator;
import jim.h.common.android.zxinglib.integrator.IntentResult;

/**
 * Created by mangobin on 15-4-8.
 */
public class ScanBarCodeActivity extends BaseActivity {
    private Handler  handler = new Handler();
    private TextView txtScanResult;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_request);

        txtScanResult = (TextView) findViewById(R.id.itemName);
        View btnScan = findViewById(R.id.confirm);

        // Scan button
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set the last parameter to true to open front light if available
                IntentIntegrator.initiateScan(ScanBarCodeActivity.this, R.layout.capture,
                        R.id.viewfinder_view, R.id.preview_view, true);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IntentIntegrator.REQUEST_CODE:
                IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode,
                        resultCode, data);
                if (scanResult == null) {
                    return;
                }
                final String result = scanResult.getContents();
                if (result != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            txtScanResult.setText(result);
                        }
                    });
                }
                break;
            default:
        }
    }
}
