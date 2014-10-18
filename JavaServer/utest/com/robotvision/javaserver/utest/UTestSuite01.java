package com.robotvision.javaserver.utest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import com.robotvision.javaserver.IServerSupporter;
import com.robotvision.javaserver.ServerSupporter;
import com.robotvision.javaserver.utils.Commands;

public class UTestSuite01 {

	/**
	 * Test standard work flow
	 */
	@Test
	public void UTest01() {
		IServerSupporter supporter = ServerSupporter.getInstance(8888);
		// find client
		supporter.findClient();
		
		// get client ip and port number
		try {
			supporter.receiveClientAddress();
			System.out.print("client ip: " + supporter.getClientIpAddress());
			System.out.println(", port: " + supporter.getClientPort());
		} catch (IOException e) {
			fail("fail to receive ip: " + e.getLocalizedMessage());
		}
		
		// send login
		try {
			supporter.send(Commands.LOGIN);
		} catch (Exception e) {
			fail("fail to send login command: " + e.getLocalizedMessage());
		}
		
		// receiving picture
		try {
			// check camera availability
			if (supporter.receiveCameraAvailable()) {
				System.out.println("client camera is available.");
				
				// get picture twice
				for (int i = 0; i < 2; i++) {
					// send capture command
					System.out.print("sending capture picture command...");
					supporter.send(Commands.CAPTURE_PICTURE);
					System.out.println("...OK");

					// waiting for picture
					System.out.print("getting picture...");
					supporter.receivePicture();
					System.out.println("...captured");

					// adapt picture to RGB
					supporter.adaptByteToRGB();
					int[] R = supporter.getR();
					int[] G = supporter.getG();
					int[] B = supporter.getB();

					// check the received picture data, the picture size should be 640*480
					assertEquals("invalid R.", 307200, R.length);
					assertEquals("invalid G.", 307200, G.length);
					assertEquals("invalid B.", 307200, B.length);
				}
			}
		} catch (Exception e) {
			System.out.println("...FAIL");
			fail("fail to capture: " + e.getMessage());
		}
		
		// stop capture
		try {
			System.out.print("sending stop picture command...");
			supporter.send(Commands.STOP_CAPTURE);
			System.out.println("...OK");
		} catch (Exception e) {
			System.out.println("...FAIL");
			fail("fail to stop caputre: " + e.getMessage());
		}
		
	}
}
