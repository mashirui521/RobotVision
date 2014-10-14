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
	
	private int[] R;
	private int[] G;
	private int[] B;
	
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
	public void receive() throws IOException {
		
		byte[] data = null;
		
		if (server == null) {
			System.out.println("server is null. return.");
			return;
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
	}

//	@Override
//	public String savePicture(String rootPath) throws IOException {
//		String path = null;
//		
//		Date now = new Date();
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//		String fileName = formatter.format(now) + ".jpg";
//		
//		File root = new File(rootPath);
//		if (!root.exists()) {
//			root.mkdirs();
//		}
//		
//		File file = new File(root, fileName);
//		FileOutputStream outputStream = new FileOutputStream(file);
//		try {
//			outputStream.write(this.data);
//			path = file.getAbsolutePath();
//			System.out.println("data saved in " + path);
//		} catch (IOException e) {
//			throw e;
//		} finally {
//			outputStream.close();
//		}
//		
//		return path;
//	}
	
	@Override
	public void adaptByteToRGB() {
		
		if (data == null || data.length == 0) {
			System.out.println("data not received. return.");
			return;
		}
		
		int[] intData = new int[data.length / 4];
		R = new int[intData.length];
		G = new int[intData.length];
		B = new int[intData.length];
		for (int i = 0; i < intData.length; i++) {
			int value = (data[i*4] & 0xFF) << 24 |
					(data[i*4 + 1] & 0xFF) << 16 |
					(data[i*4 + 2] & 0xFF) << 8 |
					data[i*4 + 3] & 0xFF;
			intData[i] = value;
			R[i] = (value >> 16) & 0xff;
			G[i] = (value >> 8) & 0xff;
			B[i] = value & 0xff;
		}
	}

	@Override
	public int[] getR() {
		return R;
	}

	@Override
	public int[] getG() {
		return G;
	}

	@Override
	public int[] getB() {
		return B;
	}
	
}
