package com.robotvision.javaserver;

import java.io.IOException;

public interface IServerSupporter {
	
	public void send(int data) throws Exception;
	
	public String receiveClientAddress() throws IOException;
		
	public void receivePicture() throws IOException;

	public void adaptByteToRGB();

	public int[] getR();

	public int[] getG();

	public int[] getB();

	public int getClientPort();
	
	public String getClientIpAddress();
	
}
