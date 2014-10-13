package com.robotvision.javaserver;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerSupporter implements IServerSupporter{

	private ServerSocket server = null;
	private int port;
	private byte[] data;
	
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

		this.data = data;
		return data;
	}

	@Override
	public String savePicture(String rootPath) throws IOException {
		String path = null;
		
		Date now = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String fileName = formatter.format(now) + ".jpg";
		
		File root = new File(rootPath);
		if (!root.exists()) {
			root.mkdirs();
		}
		
		File file = new File(root, fileName);
		FileOutputStream outputStream = new FileOutputStream(file);
		try {
			outputStream.write(this.data);
			path = file.getAbsolutePath();
			System.out.println("data saved in " + path);
		} catch (IOException e) {
			throw e;
		} finally {
			outputStream.close();
		}
		
		return path;
	}	
}
