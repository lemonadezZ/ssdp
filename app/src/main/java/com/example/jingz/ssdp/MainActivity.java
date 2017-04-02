package com.example.jingz.ssdp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Observable;

public class MainActivity extends AppCompatActivity {
    Pushhandler pushhandler;
    private SSDPService ssdpService;
    private TextView logtext;
    private SSDPService.SSDPbinder myBinder=null;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
   //         logtext.setText("连接服务完成...");
            ssdpService = ((SSDPService.SSDPbinder) service).getService();
            ssdpService.setOnFindListener(new OnFindListener() {
                @Override
                public void onFind(String data) {
                    Message msg = new Message();
                    Bundle b = new Bundle();
                    b.putString("text",data);
                    msg.setData(b);
                    MainActivity.this.pushhandler.sendMessage(msg);
                }
            });

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Intent ssdp = new Intent(this, SSDPService.class);
//        startService(ssdp);
//        this.bindService(ssdp, conn, Context.BIND_AUTO_CREATE);

        setContentView(R.layout.activity_main);
        logtext = (TextView) findViewById(R.id.logText);
        pushhandler=new Pushhandler();

//        WifiManager manager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
//        WifiManager.MulticastLock lock= manager.createMulticastLock("test wifi");
        //新线程


        class MutliThread extends Thread{
            MutliThread(){
                super();//调用父类带参数的构造方法
            }
            public void run(){
                String message="NOTIFY * HTTP/1.1\n" +
                        "Host: 239.255.255.250:1900\n" +
                        "NT: someunique:idscheme3\n" +
                        "NTS: ssdp:byebye\n" +
                        "USN: someunique:idscheme3";
                DatagramSocket s = null;
                InetAddress local = null;
                try {
                    s = new DatagramSocket();

                    try{
                        System.out.println("获取名称");
                        local = InetAddress.getByName("255.255.255.255");
                    }catch (UnknownHostException e){
                        System.out.println("获取名称报错");
                    }

                    try{
                        System.out.println("发送数据");
                        int i=0;
                        while (i<100){
                            i++;
                            String msg=(message+String.valueOf(i));
                            msg=(message);
                            byte[] messageByte=msg.getBytes();
                            int msg_length=msg.length();

                            Message msgdata = new Message();
                            Bundle b = new Bundle();
                            b.putString("text",msg);
                            msgdata.setData(b);
                            MainActivity.this.pushhandler.sendMessage(msgdata);


                            DatagramPacket p = new DatagramPacket(messageByte, msg_length, local, 1990);
                            s.send(p);
                            try{
                                Thread.sleep(10000);
                            }catch (InterruptedException e){
                                System.out.println("sleep时发生错误");
                            };
                        }

                        s.close();
                    }catch (IOException e){
                        System.out.println("发送数据报错");
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                    System.out.println("发送了错误");

                }
                System.out.println("执行完了");
            }
        }
        MutliThread m1=new MutliThread();
        m1.start();

//        logtext.setEnabled(false);
//        setSupportActionBar(toolbar);


//        Intent setting=new Intent(this,Main2Activity.class);
//        startActivity(setting);
//        logtext.setText("start");
    }



    class Pushhandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

                super.handleMessage(msg);
                Bundle b = msg.getData();
                String text = b.getString("text");
                logtext.append(text+"\n");

        }
    }
}
