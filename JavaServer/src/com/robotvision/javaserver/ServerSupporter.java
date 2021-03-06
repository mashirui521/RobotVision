package com.robotvision.javaserver;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;

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
	
	/**
	 * @param port The port number used in localhost server
	 * @return
	 */
	public static IServerSupporter getInstance(int port) {
		if (_supporter != null) {
			return _supporter;
		} else {
			return new ServerSupporter(port);
		}
	}
	
	private ServerSupporter(int port) {
		this.port = port;
		try {
			System.out.print("Start server. IP: "+ 
			           Inet4Address.getLocalHost().getHostAddress() + 
			           ", Port: " + port);
		} catch (UnknownHostException e) {
			System.out.println("..FAIL." + e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.robotvision.javaserver.IServerSupporter#findClient()
	 */
	@Override
	public void findClient() throws IOException {
		
		Receiver receiver = Receiver.getInstance(port);
		receiver.run();
		
	}
	
	/* (non-Javadoc)
	 * @see com.robotvision.javaserver.IServerSupporter#send(int)
	 */
	@Override
	public void send(int data) throws Exception {
		if (clientIpAddress == null) {
			throw new Exception("client ipaddress is empty.");
		}
		
		Sender sender = new Sender(clientIpAddress, clientPort);
		sender.init(data);
		sender.run();
	}

	/* (non-Javadoc)
	 * @see com.robotvision.javaserver.IServerSupporter#receivePicture()
	 */
	@Override
	public void receivePicture() throws IOException {
				
		Receiver receiver = Receiver.getInstance(port);
		receiver.run();
		
		this.data = receiver.getData();
	}
	
	/* (non-Javadoc)
	 * @see com.robotvision.javaserver.IServerSupporter#receiveClientAddress()
	 */
	@Override
	public String receiveClientAddress() throws IOException {
		Receiver receiver = Receiver.getInstance(port);
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
	
	/* (non-Javadoc)
	 * @see com.robotvision.javaserver.IServerSupporter#receiveCameraAvailable()
	 */
	@Override
	public boolean receiveCameraAvailable() throws IOException {
		Receiver receiver = Receiver.getInstance(port);
		receiver.run(); 
		byte[] data = receiver.getData();
		int command = data[3] & 0xFF |
	            (data[2] & 0xFF) << 8 |
	            (data[1] & 0xFF) << 16 |
	            (data[0] & 0xFF) << 24;
		return command == Commands.CAMERA_AVAILABLE;
	}
	
	/* (non-Javadoc)
	 * @see com.robotvision.javaserver.IServerSupporter#getClientPort()
	 */
	@Override
	public int getClientPort() {
		return clientPort;
	}

	/* (non-Javadoc)
	 * @see com.robotvision.javaserver.IServerSupporter#getClientIpAddress()
	 */
	@Override
	public String getClientIpAddress() {
		return clientIpAddress;
	}
	
	/* (non-Javadoc)
	 * @see com.robotvision.javaserver.IServerSupporter#adaptByteToRGB()
	 */
	@Override
	public void adaptByteToRGB() {
		
		if (data == null || data.length == 0) {
			System.out.println("data not received. return.");
			return;
		}
		
		int[] intData = new int[data.length / 3];
		R = new int[intData.length];
		G = new int[intData.length];
		B = new int[intData.length];
		
		for (int i = 0; i < intData.length; i++) {
			R[i] = data[i*3] & 0xFF;
			G[i] = data[i*3 + 1] & 0xFF;
			B[i] = data[i*3 + 2] & 0xFF;
		}

	}

	/* (non-Javadoc)
	 * @see com.robotvision.javaserver.IServerSupporter#getR()
	 */
	@Override
	public int[] getR() {
		return R;
	}

	/* (non-Javadoc)
	 * @see com.robotvision.javaserver.IServerSupporter#getG()
	 */
	@Override
	public int[] getG() {
		return G;
	}

	/* (non-Javadoc)
	 * @see com.robotvision.javaserver.IServerSupporter#getB()
	 */
	@Override
	public int[] getB() {
		return B;
	}
	
	/* (non-Javadoc)
	 * @see com.robotvision.javaserver.IServerSupporter#getData()
	 */
	@Override
	public byte[] getData() {
		return data;
	}
	
}
