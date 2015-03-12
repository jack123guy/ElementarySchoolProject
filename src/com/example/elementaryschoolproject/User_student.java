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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
import android.content.Intent;
import android.os.Bundle;
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

public class User_student extends Activity {
	private TextView Content;
	private Button ToUp;
	private Button Exit;
	private String logins1[];
	private TextView Deadline;
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
		Deadline = (TextView) findViewById(R.id.deadline);

		initbtn();
		setbtnonclick();
		
		H_download();
		
		Content.setText(logins1[0]);
		Deadline.setText("期限:"+logins1[1]);
	}
	
	private OnClickListener BtnClick = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.ToUpload:
				SimpleDateFormat time_deadline = new SimpleDateFormat("yyyy-MM-dd");
				Calendar cc = Calendar.getInstance();
				Calendar c_now = Calendar.getInstance();
				try{
					cc.setTime(time_deadline.parse(logins1[1]));
				}catch(ParseException e){
					//TODO Auto-generated method stub
					e.printStackTrace();
				}
				int day_dl = cc.get(Calendar.DAY_OF_YEAR);
				int day_now = c_now.get(Calendar.DAY_OF_YEAR);
				int daydiff = day_now -day_dl; 
			
				if(daydiff > 0){
					Toast.makeText(getApplicationContext(), "已超過繳交期限", Toast.LENGTH_SHORT).show();
				}
				else{
					Intent intent = new Intent(User_student.this,Student_Upload.class);
					startActivity(intent);
				}
			break;
			
			case R.id.EXIT:
				finish();

				break;
			}
			
		}


	};
		
	
	
	
	
	private void initbtn(){
		ToUp = (Button) findViewById(R.id.ToUpload);
		Exit = (Button) findViewById(R.id.EXIT);
	}
	
	private void setbtnonclick(){
		ToUp.setOnClickListener(BtnClick);
		Exit.setOnClickListener(BtnClick);
		
	}
		
		
		
	private void H_download(){
		// TODO Auto-generated method stub
		FileOutputStream preference = null;
		BufferedOutputStream buffered = null;
		String result = null;
		try{
			//���J�Ѯv���b����
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
			String atLast = total.toString();
			logins1 = atLast.split("\n");
			
			
			/*
			//��w�n�g�J��txt
			preference = openFileOutput("HW.txt",MODE_PRIVATE);
			buffered = new BufferedOutputStream(preference);
			for(int i=0; i<logins1.length;i+=2){
				buffered.write(logins1[i].getBytes());
				buffered.write("\n".getBytes());
				buffered.write(logins1[i+1].getBytes());
				buffered.write("\n".getBytes());
			}			
			buffered.close();//�S��close�g�J�|��null
			*/
		}	
		catch(Exception e){
			Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_LONG).show();
		}
		
	}
	

	}

