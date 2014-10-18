package com.robotvision.phoneclient.utils;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.AsyncTask;
import android.util.Log;


public class CapturePictureTask extends AsyncTask<Camera, Void, Void>
		implements PictureCallback {
	
	private final int PIC_WIDTH = 640;
	private final int PIC_HEIGHT = 480;
	
	private String _ipAddress;
	private int _port;
	
	
	public CapturePictureTask (String ipAddress, int port) {
	
		this._ipAddress = ipAddress;
		this._port = port;
	}

	@Override
	public void onPictureTaken(byte[] picture, Camera camera) {
		camera.stopPreview();
		try {
			SocketSender sender = new SocketSender(_ipAddress, _port, 
					adaptDataToRGB(picture));
			sender.execute(false);
		} catch (Exception e) {
			Log.d("CameraPreview", "fail to send picture data: " + e.getMessage());
		} finally {
			camera.startPreview();
		}
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
	protected Void doInBackground(Camera... arg0) {
		
		arg0[0].takePicture(null, null, this);
		
		return null;
	}

}
