package com.example.elementaryschoolproject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class LoginPage extends  Activity{
	private EditText UserName;
	private EditText Password;
	private Button Login;
	private Button Regedit;
	private Button TEST;
	private final static String TAG = "mylog";
	private String data;
	private String[] logins1 = null;
	private String[] logins2 = null;
	private TextView Test;
	private RadioGroup rgroup;
	private RadioButton Teacher;
	private RadioButton Student;
	private boolean TeacherChk = false;
	private boolean StudentChk = false;
	HttpPost httppost;
	List<NameValuePair>params = new ArrayList<NameValuePair>();
	InputStream IS;
	Thread Upload;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		StrictMode.setThreadPolicy( new StrictMode.ThreadPolicy.Builder()
		.detectDiskReads()
		.detectDiskWrites()
		.detectNetwork()
		.penaltyLog()
		.build());

		UserName = (EditText) findViewById(R.id.UserName);
		Password = (EditText) findViewById(R.id.Password);
		Login = (Button) findViewById(R.id.Login);
		Regedit = (Button) findViewById(R.id.Regedit);
		Teacher = (RadioButton) findViewById(R.id.Teacher);
		Student = (RadioButton) findViewById(R.id.Student);
		rgroup = (RadioGroup) findViewById(R.id.Class);
		
		//download Teacher&Student Account
		T_downloader();
		S_downloader();
		//read the login.txt
		T_Reader();
		S_Reader();
		Login.setOnClickListener(listener);
		Regedit.setOnClickListener(listener);
		
		
		rgroup.setOnCheckedChangeListener(RadioListener);
	}
	
	
//read txt in the app
	private void T_Reader() {
		// TODO Auto-generated method stub
		FileInputStream preference = null;
		BufferedInputStream buffered = null;
		
		try{
			//讀取login.txt
			preference = openFileInput("T_User.txt");
			buffered = new BufferedInputStream(preference);
			byte[] buffbyte = new byte[1];
			data = "";
		
			do{
				int flag = buffered.read(buffbyte);//buffered.read(bufftype)還有資料就繼續讀取
				
				if(flag==-1){
					break;
				}
				else{
					data += new String(buffbyte);
				}
			}
			while (true);
			buffered.close();
		}
		catch(Exception e){
			Toast.makeText(getApplicationContext(), "X", Toast.LENGTH_SHORT);
		}
		if(data != null)
		logins1 =data.split("\n");
	}
	private void S_Reader() {
		// TODO Auto-generated method stub
		FileInputStream preference = null;
		BufferedInputStream buffered = null;
		
		try{
			//讀取login.txt
			preference = openFileInput("S_User.txt");
			buffered = new BufferedInputStream(preference);
			byte[] buffbyte = new byte[1];
			data = "";
		
			do{
				int flag = buffered.read(buffbyte);//buffered.read(bufftype)還有資料就繼續讀取
				
				if(flag==-1){
					break;
				}
				else{
					data += new String(buffbyte);
				}
			}
			while (true);
			buffered.close();
		}
		catch(Exception e){
			Toast.makeText(getApplicationContext(), "X", Toast.LENGTH_SHORT);
		}
		if(data != null)
		logins2 =data.split("\n");
	}
//download data from php and write into txt
	private void T_downloader(){
		// TODO Auto-generated method stub
		FileOutputStream preference = null;
		BufferedOutputStream buffered = null;
		String result = null;
		try{
			//載入老師的帳戶資料
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpGet httppost =new HttpGet("http://140.116.39.44/CatchAccount.php");
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
			result = total.toString();
			logins1 = result.split("\n");

			Toast.makeText(getApplicationContext(), "Account data Loading Complete", Toast.LENGTH_SHORT).show();
			
			//選定要寫入的txt
			preference = openFileOutput("T_User.txt",MODE_PRIVATE);
			buffered = new BufferedOutputStream(preference);
			for(int i=0; i<logins1.length;i+=2){
				buffered.write(logins1[i].getBytes());
				buffered.write("\n".getBytes());
				buffered.write(logins1[i+1].getBytes());
				buffered.write("\n".getBytes());
			}			
			buffered.close();//沒有close寫入會變null
		
		}
		
		catch(Exception e){
			Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_LONG).show();
		}
		
	}
	private void S_downloader(){
		// TODO Auto-generated method stub
		FileOutputStream preference = null;
		BufferedOutputStream buffered = null;
		String result = null;
		try{
			//載入老師的帳戶資料
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpGet httppost =new HttpGet("http://140.116.39.44/CatchAccount2.php");
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
			result = total.toString();
			logins2 = result.split("\n");

			Toast.makeText(getApplicationContext(), "Account2 data Loading Complete", Toast.LENGTH_SHORT).show();
			
			//選定要寫入的txt
			preference = openFileOutput("S_User.txt",MODE_PRIVATE);
			buffered = new BufferedOutputStream(preference);
			for(int i=0; i<logins2.length;i+=2){
				buffered.write(logins2[i].getBytes());
				buffered.write("\n".getBytes());
				buffered.write(logins2[i+1].getBytes());
				buffered.write("\n".getBytes());
			}			
			buffered.close();//沒有close寫入會變null
		
		}
		
		catch(Exception e){
			Toast.makeText(getApplicationContext(), "Connection1 Failed", Toast.LENGTH_LONG).show();
		}
		
	}
	
	
	
	private OnClickListener listener = new OnClickListener(){
		@Override
		public void onClick(View v){
			//TODO Auto-generated method stub
			FileOutputStream preference = null;
			BufferedOutputStream buffered = null;
			
			switch(v.getId()){
			case R.id.Login:
				try{
					//以flag標示帳號密碼是否正確
					Boolean flag = false;
					if(UserName.getText().toString().equals("")|| Password.getText().toString().equals("")){
						Toast.makeText(getApplicationContext(), "帳號密碼不能為空白", Toast.LENGTH_SHORT).show();
					}
					else if(TeacherChk == false && StudentChk == false){
						Toast.makeText(getApplicationContext(), "請選擇老師或學生", Toast.LENGTH_SHORT).show();
					}
					else{
						if(TeacherChk == true){
						for(int i=0;i<logins1.length;i=i+2){
							Log.d(TAG, "Login" + i + "=" + logins1[i]);
							Log.d(TAG, "Login" + (i + 1) + "=" + logins1[i + 1]);
							Log.d(TAG, "Login Acount ="+ UserName.getText().toString());
							Log.d(TAG, "Login Password ="+ Password.getText().toString());
							
							//判斷帳號密碼
							if(UserName.getText().toString().equals(logins1[i])){
								if(Password.getText().toString().equals(logins1[i+1])){
								flag = true;
								Intent Intent = new Intent();
								Intent.setClass(LoginPage.this, User_teacher.class);
								startActivity(Intent);
								LoginPage.this.finish();
								Toast.makeText(getApplicationContext(), "老師登入成功", Toast.LENGTH_SHORT).show();
								}else{}
								
							}
							
							
						}
					}
						else if(StudentChk == true){
							for(int i=0;i<logins2.length;i=i+2){
								Log.d(TAG, "Login" + i + "=" + logins2[i]);
								Log.d(TAG, "Login" + (i + 1) + "=" + logins2[i + 1]);
								Log.d(TAG, "Login Acount ="+ UserName.getText().toString());
								Log.d(TAG, "Login Password ="+ Password.getText().toString());
								
								//判斷帳號密碼
								if(UserName.getText().toString().equals(logins2[i])){
									if(Password.getText().toString().equals(logins2[i+1])){
									flag = true;
									Intent Intent = new Intent();
									Intent.setClass(LoginPage.this, User_student.class);
									startActivity(Intent);
									LoginPage.this.finish();
									Toast.makeText(getApplicationContext(), "學生登入成功", Toast.LENGTH_SHORT).show();
									}else{}
									
								}
								
								
							}
						}
						//未通過認證
						if(flag==false){
							Toast.makeText(getApplicationContext(),"登入失敗",Toast.LENGTH_SHORT).show();
						}
					}
				}					
				catch (Exception e){
					Toast.makeText(getApplicationContext(),"X",Toast.LENGTH_SHORT).show();
				}
				break;
				
				
			case R.id.Regedit:
			try{
				if(TeacherChk == true){
				String name = UserName.getText().toString();
				String pass = Password.getText().toString();
				
				params.add(new BasicNameValuePair("User_Name",name));
				params.add(new BasicNameValuePair("Password",pass));
				
				preference = openFileOutput("T_User.txt",MODE_PRIVATE);
				buffered = new BufferedOutputStream(preference);
				
				//匯入舊有帳號密碼
				for(int i1 =0;i1<logins1.length -1;i1+=2){
					buffered.write(logins1[i1].getBytes());
					buffered.write("\n".getBytes());
					buffered.write(logins1[i1+1].getBytes());
					buffered.write("\n".getBytes());
				}
				//加入新的帳號密碼
				buffered.write(name.getBytes());
				buffered.write("\n".getBytes());
				buffered.write(pass.getBytes());
				buffered.write("\n".getBytes());
				buffered.close();
				
				Upload = new Thread(SendData);
				Upload.start();

				T_Reader();
								
				Toast.makeText(getApplicationContext(),"Regedit Done",Toast.LENGTH_SHORT).show();
				}
				else if(StudentChk == true){
					String name = UserName.getText().toString();
					String pass = Password.getText().toString();
					
					params.add(new BasicNameValuePair("User_Name",name));
					params.add(new BasicNameValuePair("Password",pass));
					
					preference = openFileOutput("S_User.txt",MODE_PRIVATE);
					buffered = new BufferedOutputStream(preference);
					
					//匯入舊有帳號密碼
					for(int i1 =0;i1<logins2.length -1;i1+=2){
						buffered.write(logins2[i1].getBytes());
						buffered.write("\n".getBytes());
						buffered.write(logins2[i1+1].getBytes());
						buffered.write("\n".getBytes());
					}
					//加入新的帳號密碼
					buffered.write(name.getBytes());
					buffered.write("\n".getBytes());
					buffered.write(pass.getBytes());
					buffered.write("\n".getBytes());
					buffered.close();
					
					Upload = new Thread(SendData);
					Upload.start();

					S_Reader();
									
					Toast.makeText(getApplicationContext(),"Regedit Done",Toast.LENGTH_SHORT).show();
				}
			}
			catch (Exception e){
			Toast.makeText(getApplicationContext(),"X",Toast.LENGTH_SHORT).show();
				}
			break;
			

		}
		}


		
		
};

	private Runnable SendData = new Runnable()
{
    public void run()
    {
    	if(TeacherChk ==true){
    	try
		{
    		String IP = "http://140.116.39.44/UploadAccount.php";
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
			Log.e("log_tag", "Error in http connection"+e.toString());
		}
    }
    	else if(StudentChk ==true){
        	try
    		{
        		String IP = "http://140.116.39.44/UploadAccount2.php";
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
    			Log.e("log_tag", "Error in http connection"+e.toString());
    		}
    		
    	}
    }
};

	private RadioGroup.OnCheckedChangeListener RadioListener = new RadioGroup.OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(RadioGroup rgroup, int checkedId) {
			// TODO Auto-generated method stub
			switch(checkedId){
			case R.id.Teacher:
				TeacherChk = true;
				StudentChk = false;
				break;
			case R.id.Student:
				StudentChk = true;
				TeacherChk = false;
				break;
			}
			
		}
	};

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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.activity_main, container,
					false);
			return rootView;
		}
	}


}
