package com.robotvision.javaserver.utils;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Receiver {
	
	private static Receiver _receiver;
	
	private byte[] _data;
	private ServerSocket _server;
	private Socket _socket;
	
	private Receiver() {
		
	}
	
	public static Receiver getInstance(int port) throws IOException {
		if (_receiver == null) {
			_receiver = new Receiver();
			_receiver.initializeServer(port);
		}
		return _receiver;
	}
	
	public void initializeServer(int port) throws IOException {
		_server = new ServerSocket(port);	
	}
	
	public void run() {
		DataInputStream stream = null;
		try {
			_socket = _server.accept();
			stream = new DataInputStream(_socket.getInputStream());
			_data = new byte[stream.readInt()];
			stream.readFully(_data);
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
				_socket.close();
				_socket = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				_server.close();
				_server = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public byte[] getData() {
		return this._data;
	}
}
