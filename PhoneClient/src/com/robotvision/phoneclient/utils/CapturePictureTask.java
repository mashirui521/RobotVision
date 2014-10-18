package com.robotvision.phoneclient.utils;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.AsyncTask;
import android.util.Log;

public class CapturePictureTask extends AsyncTask<Void, Void, Void>
		implements PictureCallback {
	
	private Camera _camera;
	
	private final int PIC_WIDTH = 640;
	private final int PIC_HEIGHT = 480;
	
	private String _ipAddress;
	private int _port;
	
	private int CLIENT_PORT;
	
	public CapturePictureTask (Camera camera, 
			String ipAddress, int port, int clientPort) {
		this._camera = camera;
		this._ipAddress = ipAddress;
		this._port = port;
		this.CLIENT_PORT = clientPort;
	}

	@Override
	public void onPictureTaken(byte[] arg0, Camera arg1) {
		_camera.stopPreview();
		try {
			SocketSender sender = new SocketSender(_ipAddress, _port, 
					adaptDataToRGB(arg0));
			sender.execute(false);
		} catch (Exception e) {
			Log.d("CameraPreview", "fail to send picture data: " + e.getMessage());
		} finally {
			_camera.startPreview();
		}
	}
	
	private boolean listenCapture() {
    	boolean capture = false;
    	
    	SocketReceiver receiver = null;
		try {
			receiver = SocketReceiver.getInstance(CLIENT_PORT);
		} catch (IOException e1) {
			
		}
		
		if (receiver != null) {
			try {
				int command = receiver.execute().get();
				capture = command == Commands.CAPTURE_PICTURE;
			} catch (InterruptedException e) {

			} catch (ExecutionException e) {

			}
		}
    	
    	return capture;
    	
    }
	
	private byte[] adaptDataToRGB(byte[] data) {
		Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
		int[] rgbIntData = new int[PIC_WIDTH * PIC_HEIGHT];
		bitmap.getPixels(rgbIntData, 0, PIC_WIDTH, 0, 0, PIC_WIDTH, PIC_HEIGHT);
		byte[] rgbByteData = new byte[rgbIntData.length * 4];
		
		for (int i = 0; i < rgbIntData.length; i++) {
			rgbByteData[i*4] = (byte)(rgbIntData[i] & 0xFF);
			rgbByteData[i*4 + 1] = (byte)((rgbIntData[i] & 0xFF00) >>> 8);
			rgbByteData[i*4 + 2] = (byte)((rgbIntData[i] & 0xFF0000) >>> 16);
			rgbByteData[i*4 + 3] = (byte)((rgbIntData[i] & 0xFF000000) >>> 24);
		}
		
		return rgbByteData;
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		
		if (listenCapture()) {
			_camera.takePicture(null, null, this);
		}
		
		return null;
	}

}
