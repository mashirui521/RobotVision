package com.robotvision.javaserver;

import java.io.IOException;

import com.robotvision.javaserver.utils.Commands;
import com.robotvision.javaserver.utils.Receiver;
import com.robotvision.javaserver.utils.Sender;

public class ServerSupporter implements IServerSupporter{

	private static ServerSupporter _supporter = null;
	
	private int port;
	
	private int clientPort;
	private String clientIpAddress;
	
	private byte[] data;
	
	private int[] R;
	private int[] G;
	private int[] B;
	
	public static IServerSupporter getInstance(int port) {
		if (_supporter != null) {
			return _supporter;
		} else {
			return new ServerSupporter(port);
		}
	}
	
	private ServerSupporter(int port) {
		this.port = port;
	}
	
	@Override
	public void findClient() {
		try {
			Receiver receiver = new Receiver(port);
			receiver.init();
			receiver.run();
		} catch (Exception e) {

		}
		
		// dummy receiver, just for server checking on client side;
	}
	
	@Override
	public void send(int data) throws Exception {
		if (clientIpAddress == null) {
			throw new Exception("client ipaddress is empty.");
		}
		
		Sender sender = new Sender(clientIpAddress, clientPort);
		sender.init(data);
		sender.run();
	}

	@Override
	public void receivePicture() throws IOException {
				
		Receiver receiver = new Receiver(port);
		receiver.init();
		receiver.run();
		
		this.data = receiver.getData();
	}
	
	@Override
	public String receiveClientAddress() throws IOException {
		Receiver receiver = new Receiver(port);
		receiver.init();
		receiver.run();
		
		String address = new String(receiver.getData());
		
		if (address.contains(":")) {
			clientPort = Integer.parseInt(address.substring(address.indexOf(":") + 1, 
					address.length()));
			clientIpAddress = address.substring(0, address.indexOf(":"));
		}else {
			clientIpAddress = address;
			clientPort = 0;
		}
		
		return address;
	}
	
	@Override
	public boolean receiveCameraAvailable() throws IOException {
		Receiver receiver = new Receiver(port);
		receiver.init();
		receiver.run(); 
		byte[] data = receiver.getData();
		int command = data[3] & 0xFF |
	            (data[2] & 0xFF) << 8 |
	            (data[1] & 0xFF) << 16 |
	            (data[0] & 0xFF) << 24;
		return command == Commands.CAMERA_AVAILABLE;
	}
	
	@Override
	public int getClientPort() {
		return clientPort;
	}

	@Override
	public String getClientIpAddress() {
		return clientIpAddress;
	}
	
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
