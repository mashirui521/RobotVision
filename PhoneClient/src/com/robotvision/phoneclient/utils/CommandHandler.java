package com.robotvision.phoneclient.utils;

public class CommandHandler {
	
	CaptureListener _captureListener;
	
	public void setCaptureListener(CaptureListener listener) {
		_captureListener = listener;
	}
	
	public void capture() {
		if (_captureListener != null) {
			_captureListener.onCapture();
		}
	}
	
}
