package com.robotvision.phoneclient;

import java.io.IOException;

import com.robotvision.phoneclient.utils.SocketSender;

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

	private SurfaceHolder _mHolder;
	private Camera _mCamera;
	private final int PIC_WIDTH = 640;
	private final int PIC_HEIGHT = 480;
	
	private String _ipAddress;
	private int _port;
	
	private PictureCallback mPicture = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] arg0, Camera arg1) {
			_mCamera.stopPreview();
			try {
				SocketSender sender = new SocketSender(_ipAddress, _port, 
						adaptDataToRGB(arg0));
				sender.execute(false);
			} catch (Exception e) {
				Log.d("CameraPreview", "fail to send picture data: " + e.getMessage());
			} finally {
				_mCamera.startPreview();
			}
		}
	};
	
	public CameraPreview(Context context, Camera camera, 
			String ipAddress, int port) {
		super(context);
		_mCamera = camera;
		
		_mHolder = getHolder();
		_mHolder.addCallback(this);
		
		_ipAddress = ipAddress;
		_port = port;
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
