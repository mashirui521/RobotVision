package com.robotvision.javaserver;

import java.io.IOException;
import java.net.UnknownHostException;

public interface IServerSupporter {
	
	public void send(int data) throws UnknownHostException, IOException;
	
	public String receiveClientAddress() throws IOException;
		
	public void receivePicture() throws IOException;

	public void adaptByteToRGB();

	public int[] getR();

	public int[] getG();

	public int[] getB();

	public int getClientPort();
	
	public String getClientIpAddress();
	
}
