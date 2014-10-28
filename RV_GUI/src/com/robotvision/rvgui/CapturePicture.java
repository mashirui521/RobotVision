package com.robotvision.rvgui;

import java.io.IOException;


import com.robotvision.javaserver.IServerSupporter;
import com.robotvision.javaserver.ServerSupporter;
import com.robotvision.javaserver.utils.Commands;

public class CapturePicture extends Thread implements Runnable{
		
	private int _port;
	private IServerSupporter _supporter;

	private ImageCaptureListener _icListener;

	public CapturePicture (int port) {
		_port = port;
	}

	public void stopCapture() {
		try {
			_supporter.send(Commands.STOP_CAPTURE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run () {
		_supporter = ServerSupporter.getInstance(_port);

		try {
			_supporter.findClient();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			_supporter.receiveClientAddress();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			_supporter.send(Commands.LOGIN);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (_supporter.receiveCameraAvailable()) {

				while (isAlive()) {
					_supporter.send(Commands.CAPTURE_PICTURE);
					_supporter.receivePicture();
					_supporter.adaptByteToRGB();
					
					
					if (_icListener != null) {
						_icListener.OnImageCapture(_supporter.getData());					
					}
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void setOnImageCapture(ImageCaptureListener listener) {
		this._icListener = listener;
	}
}
