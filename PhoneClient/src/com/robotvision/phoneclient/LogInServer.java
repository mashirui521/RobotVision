package com.robotvision.phoneclient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class LogInServer extends Activity {
	
	private String ipAddress;
	private int port;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginserver);
        
        Button buttonLogin = (Button) findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				TextView textView_ipAddress = 
						(TextView) findViewById(R.id.editText_ipAddress);
				TextView textView_portNumber = 
						(TextView) findViewById(R.id.editText_portNumber);
				
				try {
					ipAddress = textView_ipAddress.getText().toString();
					String sPort = textView_portNumber.getText().toString();
					
					if (ipAddress.isEmpty()) {
						alert("IP adress cannot be empty.");
						return;
					} else if (sPort.isEmpty()) {
						alert("Port cannot be empty.");
						return;
					}
					
					port = Integer.parseInt(sPort);
					startMonitor();
					
				} catch (Exception e) {
					Log.d("LogInServer", e.getMessage());
				}
			}
        	
        });
    }
	
	private void alert(String text) {
		AlertDialog ad = new AlertDialog.Builder(this).create();  
		ad.setCancelable(true);  
		ad.setMessage(text);
		ad.show();
	}
	
	private void startMonitor() {
		if (ipAddress.isEmpty()) {
			return;
		}
		
		Intent intent = new Intent(this, Monitor.class);
		intent.putExtra("ipAddress", ipAddress);
		intent.putExtra("port", port);
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
