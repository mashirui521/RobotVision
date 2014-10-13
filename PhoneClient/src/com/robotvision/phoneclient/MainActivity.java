package com.robotvision.phoneclient;

import com.camera.cameraclient.R;

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


public class MainActivity extends Activity {

	private Camera mCamera;
	private CameraPreview mPreview;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
                
        try {
        	mCamera = Camera.open();
        } catch (Exception e) {
        	Log.d("MainActivity", "Error opening camera: " + e.getMessage());
        }
        
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout previewLayout = (FrameLayout) findViewById(R.id.camera_preview);
        previewLayout.addView(mPreview);
        
        Button captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(new OnClickListener () {

			@Override
			public void onClick(View arg0) {
				mCamera.takePicture(null, null, mPreview.getPicture());
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
