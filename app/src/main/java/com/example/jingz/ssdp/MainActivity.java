package com.example.jingz.ssdp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
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

public class MainActivity extends AppCompatActivity {
    Pushhandler pushhandler;
    private SSDPService ssdpService;
    private EditText logtext;
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
 //                   System.out.println("activity:"+data);
  //                  logtext.setText(data);
                }
            });

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Intent ssdpIntent = new Intent(this, SSDPService.class);
//        //启动SSDB服务
//        startService(ssdpIntent);
        Intent ssdp = new Intent(this, SSDPService.class);
        //开始服务
        startService(ssdp);
        //绑定服务
        this.bindService(ssdp, conn, Context.BIND_AUTO_CREATE);


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        logtext = (EditText) findViewById(R.id.logText);
        logtext.setEnabled(false);
        setSupportActionBar(toolbar);
        pushhandler=new Pushhandler();


//        logtext.setText("start...");
//        logtext.setEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

//    @Override
//    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//        System.out.println("连接服务完成");
//        myBinder = (SSDPService.SSDPbinder) iBinder;
//        myBinder.getService().setCallback(new SSDPService.Callback(){
//            @Override
//            public void onDataChange(String data) {
//                logtext.setText(data);
//            }
//        });
//    }
//
//    @Override
//    public void onServiceDisconnected(ComponentName name) {
//        System.out.println("中断服务连接");
//
//    }
}
