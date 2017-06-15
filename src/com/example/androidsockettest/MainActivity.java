package com.example.androidsockettest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static ServerSocket serverSocket = null;
	private final String SDCARD_PATH= android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
	private final String FILE_PATH =  SDCARD_PATH + "/aReceive/"; 
	private File path = new File(FILE_PATH);// ����Ŀ¼
	private File f2;// �����ļ�
	private int flag=0;
	public static TextView mTextView, textView1;
	int[] rssi1;
	int[] rssi;
	 int Sample_cnt=0; 
	// private WifiManager wifiManager;  
	 private WifiManager mWifiManager;  
	    //����һ��WifiInfo����  
	    private WifiInfo mWifiInfo;  
	    //ɨ��������������б�  
	    private List<ScanResult> mWifiList;  
	   
	    private List<WifiConfiguration> mWifiConfigurations;  
	    WifiLock mWifiLock; 
	// private WiFiAdmin mWiFiAdmin;
    // ɨ�����б�    
    private List<ScanResult> list;    
    private ScanResult mScanResult;    
    private StringBuffer sb=new StringBuffer();  
    private String IP = "";
    String buffer = "";
	public  Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			if (msg.what==0x11) {
				Bundle bundle = msg.getData();
				mTextView.append("����wifi�źŴ���"+bundle.getString("msg")+"\n");
			}
			if(msg.what == 0x12) {
	        	  Bundle bundle = msg.getData();
	        	  Toast.makeText(MainActivity.this, bundle.getString("msg1"),Toast.LENGTH_LONG).show();
	          }
	          if(msg.what == 0x13) {
	        	  Bundle bundle = msg.getData();
	        	  Toast.makeText(MainActivity.this, bundle.getString("msg2"),Toast.LENGTH_LONG).show();
	          }
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		 mWifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE); 
	       
		//if (!mWifiManager.isWifiEnabled()) {mWifiManager.setWifiEnabled(true); } 
		mTextView = (TextView) findViewById(R.id.textsss);
		textView1 = (TextView) findViewById(R.id.textView1);
		FileOutputStream fos;
		IP = getlocalip();
		textView1.setText("IP addresss:"+IP);
		
		
		new Thread() {
			public void run() {
				
				String l="";
				Bundle bundle = new Bundle();
				Bundle bundle1 = new Bundle();
				Bundle bundle2 = new Bundle();
				
				
				FileOutputStream fos;
				bundle.clear();
				bundle1.clear();
				bundle2.clear();
				OutputStream output;
				String str = "ͨ�ųɹ�";
				try {
					serverSocket = new ServerSocket(30000);
					
					while (true) {
						Message msg = new Message();
						Message msg1 = new Message();
			            Message msg2 = new Message();
						msg.what = 0x11;
						msg1.what = 0x12;
						msg2.what = 0x13;
						String s="";
						
						try {
							//Log.i("4", "try1");
							Socket socket = serverSocket.accept();
							//Log.i("5", "try1");
							//textView1.setText("yes");
							flag++;
							String name = flag+"";
							f2 = new File(FILE_PATH+name+"receive.txt");
							PreCollect();
							output = socket.getOutputStream();
							output.write(str.getBytes("UTF-8"));
							
							
							//Log.i("6", "try1");

							output.flush();
							socket.shutdownOutput();
							
							//mHandler.sendEmptyMessage(0);
							BufferedReader bff = new BufferedReader(new InputStreamReader(socket.getInputStream()));
							String line = null;
							buffer = "";
							while ((line = bff.readLine())!=null) {
								buffer = line + buffer;
							}
							Sample_cnt = Integer.parseInt(buffer);
							//Log.i("buffer", buffer);
							bundle.putString("msg", buffer.toString());
							msg.setData(bundle);
							mHandler.sendMessage(msg);
							bff.close();
							output.close();
							socket.close();
							//Log.i("7", "try1");
							 buffer= "���ڽ���wifi�ź�";
				                bundle1.putString("msg1", buffer.toString());  
				                msg1.setData(bundle1);  
				                mHandler.sendMessage(msg1);
							int data_cnt=1;
			                //rssi1=new int[Sample_cnt];
			                rssi= new int[Sample_cnt];
			                //Log.i("8", "try1");
			               for(data_cnt=1;data_cnt<=Sample_cnt;data_cnt++){
			                     // ÿ�ε��ɨ��֮ǰ�����һ�ε�ɨ����   
			                     if(sb!=null){ sb=new StringBuffer(); }  
			                     Thread.sleep(200);
			                    //��ʼɨ������
			                     mWifiManager = (WifiManager) MainActivity.this.getSystemService(Context.WIFI_SERVICE);
			            	   if (!mWifiManager.isWifiEnabled()) {mWifiManager.setWifiEnabled(true); } 
			            	     mWifiInfo=mWifiManager.getConnectionInfo(); 
			            	  // Log.i("8.1", "try1");
			                    // mWifiManager.startScan();  
			                     //Log.i("8.2", "try1");
			                     //�õ�ɨ����  
			                    // mWifiList=mWifiManager.getScanResults();  
			                     //�õ����úõ���������  
			                    // mWifiConfigurations=mWifiManager.getConfiguredNetworks(); 
			                    // list = mWifiList;
			                    // if(list!=null){ 
			                    //     	for(int i=0;i<list.size();i++){  
			                	              //�õ�ɨ����  
			                	              //mScanResult=list.get(i);
			                	             // send.setText("cccg5");
			                	            //  text1.append(mScanResult.BSSID);
			                	              
			                	         //  if(mScanResult.BSSID.equals("8c:21:0a:7b:a9:44")){
			                	        	//   Log.i("9", "try1");
			                	        	  // Log.i("׼��", "ccg1");
			                	        	   //if(data_cnt<=2) text1.append(mScanResult.BSSID);
			               					//rssi1[data_cnt-1]=mScanResult.level;
			               					rssi[data_cnt-1] = mWifiInfo.getRssi();
			               				  //  String data=rssi1[data_cnt-1]+"";
			               				    //Log.isLoggable("ֵ", rssi1[data_cnt-1]);
			               				    s = s + rssi[data_cnt-1]+"\n";//ƴ�ӳ��ַ��������շ��ڱ���s��
			               				   // l = l + rssi1[data_cnt-1];
			               					  // text1.append(data);
			               				   // send.setText("wifi");
			               					//}  
			                	           
			                         	//}
			                         	
			                        // }
			                     
			                     }
							
							
							
							
							
						} catch (IOException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
							// TODO �Զ����ɵ� catch ��
							e.printStackTrace();
						}
						if(s!="")
			            {
					try {
						fos = new FileOutputStream(f2);
						//fos.write("rssi:".getBytes());
						fos.write(s.getBytes());
						//fos.write("\r\n".getBytes());
						//fos.write("level:".getBytes());
						//fos.write(l.getBytes());
						fos.close();
						
						} catch (FileNotFoundException e1) {
						e1.printStackTrace();
						
					} catch (IOException e) {
						
						e.printStackTrace();
					}
					bundle2.putString("msg2", "���ݽ��մ洢���".toString());  
		            msg2.setData(bundle2);  
		            //������Ϣ �޸�UI�߳��е����  
		            mHandler.sendMessage(msg2);
		            s = "";
			            }
						
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				 
				 
			};
		}.start();
	}
	private String getlocalip(){  
	       WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);    
	        WifiInfo wifiInfo = wifiManager.getConnectionInfo();    
	        int ipAddress = wifiInfo.getIpAddress();   
	      //  Log.d(Tag, "int ip "+ipAddress);  
	        if(ipAddress==0)return null;  
	        return ((ipAddress & 0xff)+"."+(ipAddress>>8 & 0xff)+"."  
	               +(ipAddress>>16 & 0xff)+"."+(ipAddress>>24 & 0xff));  
	    } 
private void PreCollect(){
    	
        String SdState=android.os.Environment.getExternalStorageState();
        if(!SdState.equals(android.os.Environment.MEDIA_MOUNTED)){       	
        	//text.setText("�����MicroSD��!");
        	this.finish();}
        else{ 
        	if (!path.exists()) {// Ŀ¼���ڷ���false
  	          path.mkdirs();// ����һ��Ŀ¼
  	         }
  	        if (!f2.exists()) {// �ļ����ڷ���false
  	          try {
  	           f2.createNewFile();//�����ļ� 
  	          } catch (IOException e) {
  	           // TODO Auto-generated catch block
  	           e.printStackTrace();
  	          }
  	         } 	
           	   
             }
       }
public void openwifi(){
	Log.i("8.1", "try1");
    if (!mWifiManager.isWifiEnabled()) {mWifiManager.setWifiEnabled(true); } 
    Log.i("8.2", "try1");
	
	
}
}
