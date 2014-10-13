package com.camera.cameradummyserver;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	ServerSocket serverSocket;
	
	Server (int port) {
		try {
			serverSocket = new ServerSocket(port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public byte[] receive() {
		byte[] data = null;
		
		Socket socket = null;
		try {
			socket = serverSocket.accept();
			DataInputStream dataStream = 
					new DataInputStream(socket.getInputStream());
			int length = dataStream.readInt();
			data = new byte[length];
			if (length > 1) {
				dataStream.readFully(data);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
			
		return data;
	}
}
