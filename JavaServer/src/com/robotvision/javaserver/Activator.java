package com.robotvision.javaserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Activator {

	public static void main(String[] args) {
		Server sever = new Server(8888);
		
		while(true) {
			byte[] data = sever.receive();
			if (data != null) {
				System.out.println("the length of recieved data: " + 
						data.length);
				
				FileOutputStream stream = null;
				try {
					File file = new File("picture.jpg");
					if (!file.exists()) {
						file.createNewFile();
					} else {
						file.delete();
						file.createNewFile();
					}
					stream = new FileOutputStream(file);
					stream.write(data);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (stream != null) {
						try {
							stream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

}
