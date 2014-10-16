package com.robotvision.phoneclient;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class LogInServer extends Activity {

	private String _ipAddress;
	private int _port;

	private final int CLIENT_PORT = 8888;
	
	private Button _buttonLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loginserver);

		_buttonLogin = (Button) findViewById(R.id.button_login);
		_buttonLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					couple();
				} catch (Exception e) {
					Log.d("LogInServer", e.getMessage());
				}
			}

		});
	}
	
	private void couple() throws UnknownHostException, SocketException, 
								InterruptedException, ExecutionException {
		
		if (!checkWiFi()) {
			alert("Please connect to wifi.");
			return;
		}
		
		
		TextView textView_ipAddress = 
				(TextView) findViewById(R.id.editText_ipAddress);
		TextView textView_portNumber = 
				(TextView) findViewById(R.id.editText_portNumber);
		
		String sIpAddress = textView_ipAddress.getText().toString();
		String sPort = textView_portNumber.getText().toString();

		if (sIpAddress.isEmpty()) {
			alert("Empty IP address is not allowed.");
			return;
		} else if (sPort.isEmpty()) {
			alert("Empty port number is not allowed.");
			return;
		}
		
		int nPort = Integer.parseInt(sPort);
		
		if (!checkServer(sIpAddress, nPort)) {
			alert("server not available.");
			return;
		}
		
		_ipAddress = sIpAddress;
		_port = Integer.parseInt(sPort);

		sendIPAddress();
		login();
	}
	
	private boolean checkWiFi() {
		ConnectivityManager connMgr = 
				(ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		final NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		
		return wifi.isAvailable();
	}
	
	private boolean checkServer(String sIpAddress, int nPort) 
			throws InterruptedException, ExecutionException {
		SocketSender sender = new SocketSender(sIpAddress, nPort, 
				ByteBuffer.allocate(4).putInt(Commands.TEST_CONNECTION).array());
		return sender.execute(true).get();
	}

	private void sendIPAddress() throws UnknownHostException, SocketException {

		String address = getIPAddress();
		if (address == null) {
			new Exception("address is null.");
		}

		SocketSender sender = new SocketSender(_ipAddress, _port, 
				address.getBytes());
		sender.execute(false);
	}

	private void login() throws InterruptedException, ExecutionException {
				
		SocketReceiver receiver = new SocketReceiver(CLIENT_PORT);
		
		if (receiver.execute().get() == Commands.LOGIN) {	
			startMonitor();
		}
	}

	@SuppressWarnings("deprecation")
	private String getIPAddress() throws UnknownHostException, SocketException {

		String clientIpAddress = null;
		
		WifiManager wifiMgr = (WifiManager) this.getSystemService(WIFI_SERVICE);
		WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
		
		clientIpAddress = Formatter.formatIpAddress(wifiInfo.getIpAddress());
		
		if (clientIpAddress != null) {
			return clientIpAddress + ":" + CLIENT_PORT;
		} else {
			return null;
		}
	}

	private void alert(String text) {
		AlertDialog ad = new AlertDialog.Builder(this).create();  
		ad.setCancelable(true);  
		ad.setMessage(text);
		ad.show();
	}

	private void startMonitor() {
		if (_ipAddress.isEmpty()) {
			return;
		}

		Intent intent = new Intent(this, Monitor.class);
		intent.putExtra("ipAddress", _ipAddress);
		intent.putExtra("port", _port);
		startActivity(intent);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
