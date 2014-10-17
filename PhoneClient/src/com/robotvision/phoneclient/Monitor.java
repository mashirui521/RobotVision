package com.robotvision.phoneclient;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;

import com.robotvision.phoneclient.utils.CaptureListener;
import com.robotvision.phoneclient.utils.CommandHandler;
import com.robotvision.phoneclient.utils.Commands;
import com.robotvision.phoneclient.utils.SocketReceiver;
import com.robotvision.phoneclient.utils.SocketSender;


public class Monitor extends Activity {

	private Camera _mCamera;
	private CameraPreview _mPreview;
	
	private String _ipAddress;
	private int _port;
	private CommandHandler _commandHandler;
	
	private int CLIENT_PORT;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	_ipAddress = extras.getString("ipAddress");
        	_port = extras.getInt("port");
        	CLIENT_PORT = extras.getInt("CLIENT_PORT");
        }
                
        startCameraPreview();
        setCommandHandler();
        
        
        Button captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(new OnClickListener () {

			@Override
			public void onClick(View arg0) {
				sendCameraAvailable();
				listenCapture();
			}
        	
        });
    }
    
    private void listenCapture() {
    	
    	SocketReceiver receiver = new SocketReceiver(CLIENT_PORT);
    	try {
			int command = receiver.execute().get();
			if (command == Commands.REQUEER_PICTURE) {
				_commandHandler.capture();
			}
		} catch (InterruptedException e) {
			
		} catch (ExecutionException e) {
			
		}
    	
    }
    
    private void sendCameraAvailable() {
    	SocketSender sender = new SocketSender(_ipAddress, _port, 
    			Commands.CAMERA_AVAILABLE);
    	sender.execute(false);
    }
    
    
    private void setCommandHandler() {
    	_commandHandler = new CommandHandler();
    	_commandHandler.setCaptureListener(new CaptureListener() {

        	@Override
        	public void onCapture() {
        		_mCamera.takePicture(null, null, _mPreview.getPicture());
        	}
		
        });
    }
    
    private void startCameraPreview() {
    	try {
        	_mCamera = Camera.open();
        	_mPreview = new CameraPreview(this, _mCamera, _ipAddress, _port);
            FrameLayout previewLayout = (FrameLayout) findViewById(R.id.camera_preview);
            previewLayout.addView(_mPreview);
        } catch (Exception e) {
        	Log.d("Monitor", "Error opening camera: " + e.getMessage());
        }
        
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
