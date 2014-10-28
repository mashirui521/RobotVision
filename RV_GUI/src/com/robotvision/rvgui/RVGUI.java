package com.robotvision.rvgui;

import java.net.Inet4Address;
import java.net.UnknownHostException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class RVGUI {
	private Display _display;
	private Shell _shell;
	
	private GridLayout _mainGridLayout;
	private GridLayout _buttonGridLayout;
	
	private Button _buttonForward;
	private Button _buttonBack;
	private Button _buttonLeft;
	private Button _buttonRight;
	private Button _buttonBrake;
	
	private Button _buttonStart;
	private Button _buttonStop;
	
	private final SelectionListener buttonSelectionListener = new SelectionListener() {

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			
		}

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			Button sourceButton = (Button)arg0.getSource();
			if (sourceButton.equals(_buttonForward)) {
				System.out.println("forward");
				
			} else if (sourceButton.equals(_buttonBack)) {
				System.out.println("back");

			} else if (sourceButton.equals(_buttonLeft)) {
				System.out.println("left");

			} else if (sourceButton.equals(_buttonRight)) {
				System.out.println("right");

			} else if (sourceButton.equals(_buttonBrake)) {
				System.out.println("brake");

			} else if (sourceButton.equals(_buttonStart)) {
				
				if (!checkPort()) {
					return;
				}
				
				_buttonForward.setEnabled(true);
				_buttonBack.setEnabled(true);
				_buttonLeft.setEnabled(true);
				_buttonRight.setEnabled(true);
				_buttonBrake.setEnabled(true);
				_buttonStop.setEnabled(true);
				_buttonStart.setEnabled(false);
				
				_textPort.setEnabled(false);
				
				startCapture();
				
			} else if (sourceButton.equals(_buttonStop)) {
				_buttonForward.setEnabled(false);
				_buttonBack.setEnabled(false);
				_buttonLeft.setEnabled(false);
				_buttonRight.setEnabled(false);
				_buttonBrake.setEnabled(false);
				_buttonStart.setEnabled(true);
				_buttonStop.setEnabled(false);
				
				_textPort.setEnabled(true);
				
				stopCapture();
			}
		}
		
	};
	
	private ImageCaptureListener _icListener = new ImageCaptureListener() {

		@Override
		public void OnImageCapture(final byte[] imageDataBytes) {
			_display.asyncExec(new Runnable() {

				@Override
				public void run() {
					if (!_imageLabel.isDisposed()) {		
						PaletteData paletteData = new PaletteData(255, 255, 255);
						ImageData imageData = new ImageData(640, 480, 24, paletteData, 1, imageDataBytes);
						Image image = new Image(_display, imageData);
						_imageLabel.setImage(image);
					}
				}
				
			});
		}
		
	};
	
	private Text _textPort;
	private Label _imageLabel;
	
	private CapturePicture _capturePicture;
	
	
	public RVGUI(Display display, Shell shell) {
		this._display = display;
		this._shell = shell;
	}
	
	public void run() {
		
		init();
		
		_shell.open();
		
		while(!_shell.isDisposed()) {
			if(_display.readAndDispatch()) {
				_display.sleep();
			}
		}
		
		_display.dispose();
	}
	
	private void init() {
		_shell.setText("Robot Vision Controller");
		
		_mainGridLayout = new GridLayout(2, false);
		_shell.setLayout(_mainGridLayout);
		
		Composite imageComposite = new Composite(_shell, SWT.FILL);
		imageComposite.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 1, 1));

		imageComposite.setLayout(new GridLayout(1, false));
		_imageLabel = new Label(imageComposite, SWT.FILL);
		GridData imageLabelGridData = new GridData();
		imageLabelGridData.widthHint = 640;
		imageLabelGridData.heightHint = 480;
		_imageLabel.setLayoutData(imageLabelGridData);
		
		Composite controlComposite = new Composite(_shell, SWT.BORDER);
		controlComposite.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, true, false, 1, 1));
		
		controlComposite.setLayout(new GridLayout(1, false));
		
		GridData gridDataFill = new GridData();
		gridDataFill.grabExcessHorizontalSpace = true;
		gridDataFill.grabExcessVerticalSpace = true;
		Composite buttonComposite = new Composite(controlComposite, SWT.BORDER);
		buttonComposite.setLayoutData(gridDataFill);
		_buttonGridLayout = new GridLayout(3, false);
		buttonComposite.setLayout(_buttonGridLayout);
		
		new Label(buttonComposite, SWT.FILL);
		_buttonForward = new Button(buttonComposite, SWT.PUSH);
		_buttonForward.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		new Label(buttonComposite, SWT.FILL);

		_buttonLeft = new Button(buttonComposite, SWT.PUSH);
		_buttonLeft.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		_buttonBrake = new Button(buttonComposite, SWT.PUSH);
		_buttonBrake.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		_buttonRight = new Button(buttonComposite, SWT.PUSH);
		_buttonRight.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));

		new Label(buttonComposite, SWT.FILL);
		_buttonBack = new Button(buttonComposite, SWT.PUSH);
		_buttonBack.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		new Label(buttonComposite, SWT.FILL);
		
		_buttonForward.setText("Forward");
		_buttonBack.setText("Back");
		_buttonLeft.setText("Left");
		_buttonRight.setText("Right");
		_buttonBrake.setText("Brake");
		
		_buttonForward.addSelectionListener(buttonSelectionListener);
		_buttonBack.addSelectionListener(buttonSelectionListener);
		_buttonLeft.addSelectionListener(buttonSelectionListener);
		_buttonRight.addSelectionListener(buttonSelectionListener);
		_buttonBrake.addSelectionListener(buttonSelectionListener);
		
		_buttonForward.setEnabled(false);
		_buttonBack.setEnabled(false);
		_buttonLeft.setEnabled(false);
		_buttonRight.setEnabled(false);
		_buttonBrake.setEnabled(false);
		
		Composite addressPortComposite = new Composite(controlComposite, SWT.BORDER);
		addressPortComposite.setLayoutData(gridDataFill);
		addressPortComposite.setLayout(new GridLayout(2, false));
		Label labelIpAddressHeader = new Label(addressPortComposite, SWT.FILL);
		labelIpAddressHeader.setText("IP Address:");
		Label labelIpAddress = new Label(addressPortComposite, SWT.FILL);
		try {
			labelIpAddress.setText(Inet4Address.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		labelIpAddress.setLayoutData(gridDataFill);
		
		Label labelPortHeader = new Label(addressPortComposite, SWT.FILL);
		labelPortHeader.setText("Port:");
		
		_textPort = new Text(addressPortComposite, SWT.SINGLE|SWT.BORDER);
		_textPort.setLayoutData(gridDataFill);
		
		Composite captureComposite = new Composite(controlComposite, SWT.BORDER);
		captureComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		captureComposite.setLayout(new GridLayout(2, true));
		_buttonStart = new Button(captureComposite, SWT.PUSH);
		_buttonStop = new Button(captureComposite, SWT.PUSH);
		_buttonStop.setEnabled(false);
		
		_buttonStart.addSelectionListener(buttonSelectionListener);
		_buttonStop.addSelectionListener(buttonSelectionListener);
		
		_buttonStart.setText("Start");
		_buttonStop.setText("Stop");
		
		_shell.setSize(910, 520);
		_shell.setMaximized(false);
		_shell.setMinimumSize(910, 480);
	}
	
	private boolean checkPort () {
		if (_textPort.getText().isEmpty()) {
			MessageBox messageBox = new MessageBox(_shell, SWT.ICON_ERROR);
			messageBox.setMessage("port number cannot be empty");
			messageBox.open();
			return false;
		}
		
		return true;
	}
	
	private void startCapture() {
		_capturePicture = 
				new CapturePicture(Integer.parseInt(_textPort.getText()));
		_capturePicture.setOnImageCapture(_icListener);
		_capturePicture.start();
	}
	
	@SuppressWarnings("deprecation")
	private void stopCapture() {
		_capturePicture.stop();
		_capturePicture.stopCapture();
	}
	
}
