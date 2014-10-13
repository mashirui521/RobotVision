package com.robotvision.phoneclient;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.util.Log;

public class SocketSender extends AsyncTask<Void, Void, Void> {
	private byte[] data;
	private String host;
	private int port;
	
	SocketSender (String host, int port, byte[] data) {
		this.data = data;
		this.host = host;
		this.port = port;
	}
	
	private void sendData() {
		if (data == null) {
			return;
		}
		
		Socket socket = null;
		DataOutputStream outputStream = null;
		try {
			socket = new Socket(host, port);
			outputStream = new DataOutputStream (
					socket.getOutputStream());
			outputStream.writeInt(data.length);
			outputStream.write(data);
		} catch (UnknownHostException e) {
			Log.d("SocketSender", "UnknownHostException: " + e.getMessage());
		} catch (IOException e) {
			Log.d("SocketSender", "IOException: " + e.getMessage());
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					Log.d("SocketSender", 
							"IOException in closing stream: " + e.getMessage());
				}
			}
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					Log.d("SocketSender", 
							"IOException in closing socket: " + e.getMessage());
				}
			}

		}
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		sendData();
		return null;
	}

}
