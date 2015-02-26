package com.example.elementaryschoolproject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.R.string;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
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

public class User_teacher extends Activity {
	private Button Upload;
	private EditText HW;
	HttpPost httppost;
	List<NameValuePair>params = new ArrayList<NameValuePair>();
	InputStream IS;
	Thread up;
//	String hwoutput;
	ProgressDialog dialog = null;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstancestate) {
		super.onCreate(savedInstancestate);
		setContentView(R.layout.user_teacher);
		
		StrictMode.setThreadPolicy( new StrictMode.ThreadPolicy.Builder()
		.detectDiskReads()
		.detectDiskWrites()
		.detectNetwork()
		.penaltyLog()
		.build());
		
		HW = (EditText) findViewById(R.id.HomeWork);
		Upload = (Button) findViewById(R.id.Upload);
		
		Upload.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String hwoutput = HW.getText().toString();
				params.add(new BasicNameValuePair("HW",hwoutput));
				up = new Thread(SendData);
				up.start();	
				Toast.makeText(getApplicationContext(), "µo¥¬¦¨¥\", Toast.LENGTH_SHORT).show();
			}
		});
		
	}
	
	private Runnable SendData = new Runnable()
{
    public void run()
    {
    	Looper.prepare();
    	try
		{
    		String IP = "http://140.116.39.44/HWUpload.php";
    		httppost = new HttpPost(IP);
			HttpClient httpclient = new DefaultHttpClient();
			httppost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			if(entity != null){
				Toast.makeText(getApplicationContext(), "php response", Toast.LENGTH_SHORT).show();
			}
			else{
				Toast.makeText(getApplicationContext(), "php not response", Toast.LENGTH_SHORT).show();
			}
			IS = entity.getContent();
			
		}
		catch(Exception e)
		{
			Log.e("log_tag", "Error in http connection "+e.toString());
		}
    

}
    };
    
}
