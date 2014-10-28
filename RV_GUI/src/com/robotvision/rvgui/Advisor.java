package com.robotvision.rvgui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Advisor {
	
	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		
		new RVGUI(display, shell).run();
	}

}
