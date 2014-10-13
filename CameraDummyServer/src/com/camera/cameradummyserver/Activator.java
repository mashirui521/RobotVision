package com.camera.cameradummyserver;

public class Activator {

	public static void main(String[] args) {
		Server sever = new Server(8888);
		
		while(true) {
			byte[] data = sever.recieve();
			if (data != null) {
				System.out.println("the length of recieved data: " + 
						data.length);
			}
		}
	}

}
