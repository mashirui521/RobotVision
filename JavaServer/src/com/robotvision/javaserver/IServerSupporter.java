package com.robotvision.javaserver;

import java.io.IOException;

public interface IServerSupporter {
	
	/**
	 * dummy receiver, just for server checking on client side;
	 * @throws IOException 
	 */
	public void findClient() throws IOException;
	
	/**
	 * send command to client
	 * @param data Command in type com.robotvision.javaserver.utils.Commands
	 * @throws Exception
	 */
	public void send(int data) throws Exception;
	
	/**
	 * get client address
	 * @return address string in format <ip address>:<port number>
	 * @throws IOException
	 */
	public String receiveClientAddress() throws IOException;
		
	/**
	 * receive picture from client
	 * @throws IOException
	 */
	public void receivePicture() throws IOException;
	
	/**
	 * check camera availability
	 * @return camera availability
	 * @throws IOException
	 */
	public boolean receiveCameraAvailable() throws IOException;

	/**
	 * adapt byte picture data to RGB
	 */
	public void adaptByteToRGB();

	/**
	 * @return
	 */
	public int[] getR();

	/**
	 * @return
	 */
	public int[] getG();

	/**
	 * @return
	 */
	public int[] getB();

	/**
	 * @return
	 */
	public int getClientPort();
	
	/**
	 * @return
	 */
	public String getClientIpAddress();
	
	/**
	 * @return
	 */
	public byte[] getData();
	
}
