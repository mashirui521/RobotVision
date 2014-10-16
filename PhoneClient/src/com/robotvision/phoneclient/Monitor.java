package com.robotvision.phoneclient;

import com.robotvision.phoneclient.R;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;


public class Monitor extends Activity {

	private Camera _mCamera;
	private CameraPreview _mPreview;
	
	private String _ipAddress;
	private int _port;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	_ipAddress = extras.getString("ipAddress");
        	_port = extras.getInt("port");
        }
                
        try {
        	_mCamera = Camera.open();
        } catch (Exception e) {
        	Log.d("Monitor", "Error opening camera: " + e.getMessage());
        }
        
        _mPreview = new CameraPreview(this, _mCamera, _ipAddress, _port);
        FrameLayout previewLayout = (FrameLayout) findViewById(R.id.camera_preview);
        previewLayout.addView(_mPreview);
        
        Button captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(new OnClickListener () {

			@Override
			public void onClick(View arg0) {
				_mCamera.takePicture(null, null, _mPreview.getPicture());
			}
        	
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
