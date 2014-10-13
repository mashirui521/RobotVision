package com.robotvision.javaserver;

import java.io.IOException;

public interface IServerSupporter {
	
	public void startService() throws IOException;
	
	public void stopService() throws IOException;
	
	public void send();
	
	public byte[] receive() throws IOException;
	
	public String savePicture(String rootPath) throws IOException;
	
}
