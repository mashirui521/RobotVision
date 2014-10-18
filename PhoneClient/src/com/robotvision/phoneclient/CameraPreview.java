package com.robotvision.phoneclient;

import java.io.IOException;


import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

	private SurfaceHolder _mHolder;
	private Camera _mCamera;
	private final int PIC_WIDTH = 640;
	private final int PIC_HEIGHT = 480;
		
	
	public CameraPreview(Context context, Camera camera) {
		super(context);
		_mCamera = camera;
		
		_mHolder = getHolder();
		_mHolder.addCallback(this);
	}

	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		if (_mHolder.getSurface() == null) {
			return;
		}
		
		try {
			_mCamera.stopPreview();
		} catch (Exception e) {
			
		}
		
		try {
			Camera.Parameters parameters = _mCamera.getParameters();
			parameters.setPictureSize(PIC_WIDTH, PIC_HEIGHT);
			parameters.setPictureFormat(ImageFormat.JPEG);
			_mCamera.setPreviewDisplay(_mHolder);
			_mCamera.startPreview();
		} catch (Exception e) {
			Log.d("CameraPreview", "Error starting camera preview: " + e.getMessage());
		}
	}

	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			_mCamera.setPreviewDisplay(holder);
			_mCamera.startPreview();
		} catch (IOException e) {
			Log.d("CameraPreview", "Error setting camera preview: " + e.getMessage());
		}
	}

	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		_mCamera.release();
	}

}
