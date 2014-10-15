package com.robotvision.javaserver.utest;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.Test;

import com.robotvision.javaserver.Commands;
import com.robotvision.javaserver.IServerSupporter;
import com.robotvision.javaserver.ServerSupporter;

public class UTestSuite01 {

	@Test
	public void UTest01() {
		IServerSupporter supporter = ServerSupporter.getInstance(8888);
		try {
			String address = supporter.receiveClientAddress();
			System.out.println(address);
		} catch (IOException e) {
			fail("fail to receive ip: " + e.getLocalizedMessage());
		}
		
		try {
			supporter.send(Commands.LOGIN);
		} catch (UnknownHostException e) {
			fail("host unknown: " + e.getLocalizedMessage());
		} catch (IOException e) {
			fail("IOException in sending login command: " 
					+ e.getLocalizedMessage());
		}
	}
}
