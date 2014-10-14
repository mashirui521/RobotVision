package com.robotvision.javaserver;

import java.io.IOException;
import java.net.UnknownHostException;

public class ServerSupporter implements IServerSupporter{

	private int port;
	
	private int clientPort;
	private String clientIpAddress;
	private byte[] data;
	
	private int[] R;
	private int[] G;
	private int[] B;
	
	public ServerSupporter(int port) {
		this.port = port;
	}
	
	@Override
	public void send(int data) throws UnknownHostException, IOException {
		if (clientIpAddress == null) {
			System.out.println("client ip address is empty. return.");
			return;
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
		
		String address = receiver.getData().toString();
		
		port = Integer.parseInt(address.substring(address.indexOf(":") + 1, 
				address.length()));
		clientIpAddress = address.substring(0, address.indexOf(":") - 1);
		
		return address;
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
