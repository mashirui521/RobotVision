package com.robotvision.phoneclient;

import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

	private SurfaceHolder mHolder;
	private Camera mCamera;
	private final int PIC_WIDTH = 640;
	private final int PIC_HEIGHT = 480;
	
	private String ipAddress;
	private int port;
	
	private PictureCallback mPicture = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] arg0, Camera arg1) {
			try {
				SocketSender sender = new SocketSender(ipAddress, port, 
						adaptDataToRGB(arg0));
				sender.execute();
			} catch (Exception e) {
				Log.d("CameraPreview", "fail to send picture data: " + e.getMessage());
			} finally {
				mCamera.startPreview();
			}
		}
	};
	
	public CameraPreview(Context context, Camera camera, 
			String ipAddress, int port) {
		super(context);
		mCamera = camera;
		
		mHolder = getHolder();
		mHolder.addCallback(this);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		if (mHolder.getSurface() == null) {
			return;
		}
		
		try {
			mCamera.stopPreview();
		} catch (Exception e) {
			
		}
		
		try {
			Camera.Parameters parameters = mCamera.getParameters();
			parameters.setPictureSize(PIC_WIDTH, PIC_HEIGHT);
			parameters.setPictureFormat(ImageFormat.JPEG);
			mCamera.setPreviewDisplay(mHolder);
			mCamera.startPreview();
		} catch (Exception e) {
			Log.d("CameraPreview", "Error starting camera preview: " + e.getMessage());
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();
		} catch (IOException e) {
			Log.d("CameraPreview", "Error setting camera preview: " + e.getMessage());
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mCamera.release();
	}
	
	public PictureCallback getPicture() {
		return this.mPicture;
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

}
