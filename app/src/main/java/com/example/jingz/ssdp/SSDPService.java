package com.example.jingz.ssdp;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.renderscript.ScriptGroup;
import android.widget.EditText;
import  com.example.jingz.ssdp.OnFindListener;

import java.util.Timer;
import java.util.TimerTask;



public class SSDPService extends Service {
    private OnFindListener onFindListener;

    public void setOnFindListener(OnFindListener onFindListener) {
        this.onFindListener= onFindListener;
    }
    public IBinder onBind(Intent intent) {
        return new SSDPbinder();
    }
    public  class SSDPbinder extends Binder {
        public SSDPService getService(){
            return SSDPService.this;
        }
        }
    public SSDPService SSDPService() {
        return SSDPService.this;
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public int  onStartCommand(Intent intent, int flags, int startId) {
//        EditText logtext=(EditText) findViewById(R.id.logText);
        System.out.println("启动SSDP服务");
        Timer t=new Timer();

        t.schedule(new TimerTask() {
            int i=0;
            @Override
            public void run(){
                System.out.println("定时运行");
                String str="发送消息";
                i=i+1;
                if (onFindListener != null){
                    System.out.println("执行回调");
                    onFindListener.onFind("ssdp 广播..."+String.valueOf(i));
                }
            }
        }, 2000,1000);

        return super.onStartCommand(intent, flags, startId);
    }
    public void run(){

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
