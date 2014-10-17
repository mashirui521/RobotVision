package com.robotvision.javaserver.utest;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import com.robotvision.javaserver.IServerSupporter;
import com.robotvision.javaserver.ServerSupporter;
import com.robotvision.javaserver.utils.Commands;

public class UTestSuite01 {

	@Test
	public void UTest01() {
		IServerSupporter supporter = ServerSupporter.getInstance(8888);
		supporter.findClient();
		
		try {
			String address = supporter.receiveClientAddress();
			System.out.println(address);
			System.out.println("client ip: " + supporter.getClientIpAddress());
			System.out.println("client port: " + supporter.getClientPort());
		} catch (IOException e) {
			fail("fail to receive ip: " + e.getLocalizedMessage());
		}
		
		try {
			supporter.send(Commands.LOGIN);
		} catch (Exception e) {
			fail("fail to send login command: " + e.getLocalizedMessage());
		}
		
		try {
			if (supporter.receiveCameraAvailable()) {
				supporter.send(Commands.CAPTURE_PICTURE);
				supporter.receivePicture();
				System.out.println("captured");
				supporter.send(Commands.STOP_CAPTURE);
			}
		} catch (Exception e) {
			fail("fail to capture: " + e.getMessage());
		}
		
		supporter.adaptByteToRGB();
		supporter.getR();
		supporter.getG();
		supporter.getB();
	}
}
