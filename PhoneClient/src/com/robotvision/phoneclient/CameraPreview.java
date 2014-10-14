package com.robotvision.phoneclient;

import java.io.IOException;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

	private SurfaceHolder mHolder;
	private Camera mCamera;
	private PictureCallback mPicture = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] arg0, Camera arg1) {
			SocketSender sender = new SocketSender("192.168.0.101", 8888, arg0);
			sender.execute();
			mCamera.startPreview();
		}
		
	};
	
	public CameraPreview(Context context, Camera camera) {
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
			parameters.setPictureSize(640, 480);
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

}
