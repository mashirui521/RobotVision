package com.robotvision.javaserver;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSupporter implements IServerSupporter{

	private ServerSocket server = null;
	private int port;
	
	public ServerSupporter(int port) {
		this.port = port;
	}
	
	@Override
	public void startService() throws IOException {
		if (server == null) {
			server = new ServerSocket(this.port);
			System.out.println("socket server started.");
		}
	}

	@Override
	public void stopService() throws IOException {
		if (server != null) {
			server.close();
			server = null;
		}
		System.out.println("socket server stopped.");
	}

	@Override
	public void send() {
		
	}

	@Override
	public byte[] receive() throws IOException {
		
		byte[] data = null;
		
		if (server == null) {
			System.out.println("server is null. return.");
			return null;
		}
		
		Socket socket = null;
		DataInputStream stream = null;
		try {
			socket = server.accept();
			stream = new DataInputStream(socket.getInputStream());
			data = new byte[stream.readInt()];
			stream.readFully(data);
		} catch (IOException e) {
			throw e;
		} finally {
			if (stream != null) {
				stream.close();
				System.out.println("input stream closed.");
			}
			if (socket != null) {
				socket.close();
				System.out.println("socket closed.");
			}
		}

		return data;
	}	
}
