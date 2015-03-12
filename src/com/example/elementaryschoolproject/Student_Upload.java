package com.example.elementaryschoolproject;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView; 

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class Student_Upload extends Activity {
	
	private Button sendBtn,capture,choose;
	private TextView txtMessage1,txtMessage2,txtMessage3;
	private String uriAPI = "http://140.116.39.44/photoPostTest.php";
	private String upLoadServerUri = "http://140.116.39.44/photosendTest.php";
	//摰����
	private ImageView mImg;
	private DisplayMetrics mPhone;
	private final static int CAMERA = 66 ;
	private final static int PHOTO = 99 ;
	
	private String uploadfilepath;
	
	protected static final int REFRESH_DATA = 0x00000001;
	
	Handler mHandler = new Handler()
	   {
	      @Override
	      public void handleMessage(Message msg1)
	      {
	          switch (msg1.what)
	          {
	          // 憿舐內蝬脰楝銝������
	          case REFRESH_DATA:
	             String result = null;
	             if (msg1.obj instanceof String)
	                result = (String) msg1.obj;
	             
	             if (result != null)
	                // ��蝬脰楝�������
	                Toast.makeText(Student_Upload.this, result, Toast.LENGTH_LONG).show();
	             break;
	          }
	      }
	   };
	   
	int serverResponseCode = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.student_upload);

		//霈�����圾��漲
	    mPhone = new DisplayMetrics();
	    getWindowManager().getDefaultDisplay().getMetrics(mPhone);
		
		SetObjFunction();
		
		capture.setOnClickListener(new OnClickListener()
	    {
	       @Override
	       public void onClick(View v) 
	       {
	       //���璈�嚗蒂撠�敺����SD�����嚗�startActivityForResult銝葆� requestCode�脰��嚗����摰敺�������onActivityResult
	       ContentValues value = new ContentValues();
	       value.put(Media.MIME_TYPE, "image/jpeg");                                      
	       Uri uri= getContentResolver().insert(Media.EXTERNAL_CONTENT_URI,
	                                            value); 
	       Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
	       intent.putExtra(MediaStore.EXTRA_OUTPUT, uri.getPath());  
	       startActivityForResult(intent, CAMERA);      
	       }
	    });
		
		choose.setOnClickListener(new OnClickListener()
	    {
	         @Override
	         public void onClick(View v) 
	         {
	         //���蝪輻�����startActivityForResult銝葆�requestCode�脰��嚗��暺���������onActivityResult
	         Intent intent = new Intent();
	         intent.setType("image/*");
	         intent.setAction(Intent.ACTION_GET_CONTENT);
	         startActivityForResult(intent, PHOTO);
	         }
	    });
		
		sendBtn.setOnClickListener(new OnClickListener()
	    {
	         @Override
	         public void onClick(View v) 
	         {
	        	 	SendfileData();
	        		       		
	        		new Thread(new Runnable() {
	        			public void run() {
	        				runOnUiThread(new Runnable() {
	        					public void run() {
	        					}
	        				});
	        				uploadFile(uploadfilepath);
	        			}
	        		}).start();
	         }
	      });
		}
	
	
	private void SetObjFunction() {
		
		// TODO Auto-generated method stub
		//摰儔瘥�隞嗉�
		sendBtn = (Button) findViewById(R.id.button2);
		capture = (Button) findViewById(R.id.button1);
		choose = (Button) findViewById(R.id.button3);
		txtMessage1 = (EditText) findViewById(R.id.editText1);
	    txtMessage2 = (EditText) findViewById(R.id.editText2);
	    txtMessage3 = (EditText) findViewById(R.id.editText3);
	    
	    mImg = (ImageView) findViewById(R.id.img);
		
	}
	
	//��摰��������甇文撘�
	@Override 
	protected void onActivityResult(int requestCode, int resultCode,Intent data)
	   {
	      //��requestCode��������璈���蝪輯������ata銝null
	      if ((requestCode == CAMERA || requestCode == PHOTO ) && data != null)
	      {
	         //�����楝敺ri
	         Uri uri = data.getData();
	         ContentResolver cr = this.getContentResolver();
	         uploadfilepath = getPath(uri);             
	         try
	         {
	         //霈��������Bitmap
	         Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));

	         //�����璈怠�������蒂�脣ScalePic������閬�脰�葬�
	         if(bitmap.getWidth()>bitmap.getHeight())ScalePic(bitmap,mPhone.heightPixels);
	         else ScalePic(bitmap,mPhone.widthPixels);
	         } 
	         catch (FileNotFoundException e)
	         {
	         }
	      }
	      super.onActivityResult(requestCode, resultCode, data);
	   }
	
	public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
	        
	private void ScalePic(Bitmap bitmap,int phone)
	   {
	      //蝮格瘥��身�1
	      float mScale = 1 ;
	                
	      //憒���祝摨血之����祝摨血��脰�葬�嚗���撠���ImageView�
	      if(bitmap.getWidth() > phone )
	      {
	         //��蝮格瘥��
	         mScale = (float)phone/(float)bitmap.getWidth();
	                      
	         Matrix mMat = new Matrix() ;
	         mMat.setScale(mScale, mScale);
	                          
	         Bitmap mScaleBitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),mMat,false);
	         mImg.setImageBitmap(mScaleBitmap);
	      }
	   else mImg.setImageBitmap(bitmap);
	   }
	   
	   
	public void SendfileData()
	   {
		   String msg01 = txtMessage1.getEditableText().toString();
           String msg02 = txtMessage2.getEditableText().toString();
           String msg03 = txtMessage3.getEditableText().toString();
           
           Thread t = new Thread(new sendPostRunnable(msg01,msg02,msg03));
           t.start();
	   }
	
	private String sendPostDataToInternet(String strTxt01,String strTxt02,String strTxt03)
	   {
		  /* 撱箇�TTP Post��蝺� */
	      HttpPost httpRequest = new HttpPost(uriAPI);
	      /*Post�����敹�NameValuePair[]���摮�*/
	      List<NameValuePair> params = new ArrayList<NameValuePair>();
	      params.add(new BasicNameValuePair("ID", strTxt01));
	      params.add(new BasicNameValuePair("title", strTxt02));
	      params.add(new BasicNameValuePair("detail", strTxt03));
	      
	      try
	      {
	          /* ��HTTP request */
	          httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
	          /* ���TTP response */
	          HttpResponse httpResponse = new DefaultHttpClient()
	                .execute(httpRequest);
	          /* �����Ⅳ�200 ok */
	          if (httpResponse.getStatusLine().getStatusCode() == 200)
	          {
	             /* ������葡 */
	             String strResult = EntityUtils.toString(httpResponse.getEntity());
	             // ������葡
	             return strResult;
	          }
	      } catch (Exception e)
	      {
	          e.printStackTrace();
	      }
	      return null;
		
	   }
	class sendPostRunnable implements Runnable
	   {
		  String strTxt03 = null;
	      String strTxt01 = null;
	      String strTxt02 = null;
	      
	      public sendPostRunnable(String strTxt01,String strTxt02,String strTxt03)
	      {
	    	  	 this.strTxt03 = strTxt03;
	    	  	 this.strTxt01 = strTxt01;
		         this.strTxt02 = strTxt02;
	      }
	      @Override
	      public void run()
	      {
	    	  String result = sendPostDataToInternet(strTxt01,strTxt02,strTxt03);
	         
	          mHandler.obtainMessage(REFRESH_DATA, result).sendToTarget();
	      }
	   }
		
	public int uploadFile(String sourceFileUri) {
		
		String fileName = sourceFileUri;
		  
        HttpURLConnection conn = null;
        DataOutputStream dos = null;  
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024; 
        File sourceFile = new File(sourceFileUri);
        
        if (!sourceFile.isFile()) {
        	runOnUiThread(new Runnable() {
        		public void run() {
        			Toast.makeText(Student_Upload.this, "Source File not exist",Toast.LENGTH_LONG).show();	
        		}
        	});
        	return 0;
        }
        else{
        	
        	try {
        		
        		// open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);
                
                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection(); 
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);
                
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data;name=\"uploaded_file\";filename=\""+fileName+"\"" + lineEnd);
                dos.writeBytes(lineEnd);
                
                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available(); 
       
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];
       
                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);  
                   
                while (bytesRead > 0) {
                     
                  dos.write(buffer, 0, bufferSize);
                  bytesAvailable = fileInputStream.available();
                  bufferSize = Math.min(bytesAvailable, maxBufferSize);
                  bytesRead = fileInputStream.read(buffer, 0, bufferSize);   
                   
                 }
       
                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
       
                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();
                
                Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);
                
                if(serverResponseCode == 200){
                	
                	runOnUiThread(new Runnable() {
                		
                		public void run() {
                			
                			Toast.makeText(Student_Upload.this, "File Upload Complete.",Toast.LENGTH_LONG).show();
                		}
                	});
                }
                
              //close the streams 
                fileInputStream.close();
                dos.flush();
                dos.close();
                
        	}catch (MalformedURLException ex) {
        		
                ex.printStackTrace();
                 
                runOnUiThread(new Runnable() {
                	
                	public void run() {
                		Toast.makeText(Student_Upload.this, "MalformedURLException",Toast.LENGTH_LONG).show();
                	}
                });
                
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
        	}catch (Exception e) {
        		
                e.printStackTrace();
                 
                runOnUiThread(new Runnable() {
                	
                	public void run() {
                		
                		Toast.makeText(Student_Upload.this,"Got Exception : see logcat",Toast.LENGTH_LONG).show();
                	}
                });
                Log.e("Upload file to server Exception", "Exception : "+ e.getMessage(), e);
        	}
  
            return serverResponseCode;
        } // End else block
	}	
	   
			
}
