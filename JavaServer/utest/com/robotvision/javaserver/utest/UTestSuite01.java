package com.robotvision.javaserver.utest;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import com.robotvision.javaserver.IServerSupporter;
import com.robotvision.javaserver.ServerSupporter;

public class UTestSuite01 {

	@Test
	public void UTest01() {
		IServerSupporter supporter = new ServerSupporter(8888);
		try {
			System.out.println(supporter.receiveClientAddress());
			System.out.println(supporter.getClientIpAddress());
			System.out.println(supporter.getClientPort());
		} catch (IOException e) {
			fail("fail to receive ip: " + e.getLocalizedMessage());
		}
	}
}
