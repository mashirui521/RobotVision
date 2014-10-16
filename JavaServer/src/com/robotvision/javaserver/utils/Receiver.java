package com.robotvision.javaserver.utils;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Receiver {
	
	private byte[] data;
	private ServerSocket server;
	private Socket socket;
	
	private int port;
	
	public Receiver(int port) {
		this.port = port;
	}
	
	public void init() throws IOException {
		if (server == null) {
			server = new ServerSocket(port);
		}
	}
	
	public void run() {
		DataInputStream stream = null;
		try {
			socket = server.accept();
			stream = new DataInputStream(socket.getInputStream());
			data = new byte[stream.readInt()];
			stream.readFully(data);
		} catch (IOException e) {
			//e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
					stream = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			try {
				socket.close();
				socket = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				server.close();
				server = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public byte[] getData() {
		return this.data;
	}
}
