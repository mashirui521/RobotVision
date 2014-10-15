package com.robotvision.phoneclient;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import android.os.AsyncTask;
import android.util.Log;

public class SocketReceiver extends AsyncTask<Void, Void, Void> {

	private ServerSocket server;

	private int command;
	private int port;

	SocketReceiver (int port) {
		this.port = port;
	}

	private void receiveData() {

		Socket socket = null;
		DataInputStream stream = null;

		try {
			if (server == null) {
				server = new ServerSocket(port);
			}

			socket = server.accept();
			stream = new DataInputStream(socket.getInputStream());
			command = stream.readInt();
		} catch (IOException e) {
			Log.d("SocketReceiver", "fail to receive data: " + e.getMessage());
		} finally {
			if (server != null) {
				try {
					server.close();
				} catch (IOException e) {
					Log.d("SocketReceiver", "fail to close server: " + e.getMessage());
				} finally {
					server = null;
				}
			}

			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					Log.d("SocketReceiver", "fail to close stream: " + e.getMessage());
				} finally {
					stream = null;
				}
			}


			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					Log.d("SocketReceiver", "fail to close socket: " + e.getMessage());
				} finally {
					socket = null;
				}
			}

		}
	}

	private int getCommand() {
		return command;
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		receiveData();
		return null;
	}

}
