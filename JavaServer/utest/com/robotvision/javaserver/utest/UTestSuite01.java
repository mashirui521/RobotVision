package com.robotvision.javaserver.utest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.robotvision.javaserver.IServerSupporter;
import com.robotvision.javaserver.ServerSupporter;

public class UTestSuite01 {

	@Test
	public void UTest01() {
		IServerSupporter supporter = new ServerSupporter(8888);
		try {
			supporter.startService();
		} catch (IOException e) {
			fail("start service failed. " + e.getLocalizedMessage());
		}
		
		byte[] data = null;
		try {
			data = supporter.receive();
			assertFalse("received data is null.", data == null);
			
			File picture = new File(supporter.savePicture("utruntemp"));
			assertFalse("picture not exist.", !picture.exists());
		} catch (IOException e) {
			fail("receive data failed. " + e.getLocalizedMessage());
		}
		
		try {
			supporter.stopService();
		} catch (IOException e) {
			fail("stop service failed. " + e.getLocalizedMessage());
		}
	}
}
