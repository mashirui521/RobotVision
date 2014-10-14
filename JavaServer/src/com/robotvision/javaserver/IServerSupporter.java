package com.robotvision.javaserver;

import java.io.IOException;

public interface IServerSupporter {
	
	public void startService() throws IOException;
	
	public void stopService() throws IOException;
	
	public void send();
	
	public void receive() throws IOException;

	public void adaptByteToRGB();

	public int[] getR();

	public int[] getG();

	public int[] getB();
	
	//public String savePicture(String rootPath) throws IOException;
	
}
