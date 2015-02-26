package com.example.elementaryschoolproject;

import android.os.Bundle;
import android.os.StrictMode;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;




import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.R.string;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class User_student extends Activity {
	private TextView Content;
	private String logins1;
	HttpPost httppost;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstancestate) {
		super.onCreate(savedInstancestate);
		setContentView(R.layout.user_student);
		
		StrictMode.setThreadPolicy( new StrictMode.ThreadPolicy.Builder()
		.detectDiskReads()
		.detectDiskWrites()
		.detectNetwork()
		.penaltyLog()
		.build());
		
		Content = (TextView) findViewById(R.id.Content);
		
		H_download();
		
		Content.setText(logins1);
	}
	
	private void H_download(){
		// TODO Auto-generated method stub
		FileOutputStream preference = null;
		BufferedOutputStream buffered = null;
		String result = null;
		try{
			//載入老師的帳戶資料
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpGet httppost =new HttpGet("http://140.116.39.44/CatchHW.php");
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity ht = response.getEntity();

			BufferedHttpEntity buf = new BufferedHttpEntity(ht);
			InputStream is = buf.getContent();
			BufferedReader r = new BufferedReader(new InputStreamReader(is));
			StringBuilder total = new StringBuilder();
			String line;
			while((line = r.readLine()) != null){
				total.append(line + "\n");
			}
			logins1 = total.toString();
			
			
			/*
			//選定要寫入的txt
			preference = openFileOutput("HW.txt",MODE_PRIVATE);
			buffered = new BufferedOutputStream(preference);
			for(int i=0; i<logins1.length;i+=2){
				buffered.write(logins1[i].getBytes());
				buffered.write("\n".getBytes());
				buffered.write(logins1[i+1].getBytes());
				buffered.write("\n".getBytes());
			}			
			buffered.close();//沒有close寫入會變null
			*/
		}	
		catch(Exception e){
			Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_LONG).show();
		}
		
	}
}
