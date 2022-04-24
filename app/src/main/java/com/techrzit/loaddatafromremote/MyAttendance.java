package com.techrzit.loaddatafromremote;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.webkit.WebView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyAttendance extends Activity {

    private WebView webView;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());
    String data;
    private String URL = "https://www.muthosoft.com/univ/attendance/report.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_attendance);

        webView = findViewById(R.id.webView);

        String[] keys = {"CSE489-Lab","year","semester","course","section","sid"};
        String[] values = {"true","2022","1","CSE489","2","2018360088"};
        httpRequest(keys,values);
    }


    @SuppressLint("StaticFieldLeak")
    private void httpRequest(final String keys[],final String values[]){
        executorService.execute( ()->{
            try{
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                for (int i =0;i<keys.length;i++){
                    params.add( new BasicNameValuePair(keys[i],values[i]));
                }
                data = JSONParser.getInstance().makeHttpRequest(URL,"POST",params);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
            handler.post(()->{
                if(data != null){
                    try{
                        System.out.println("data"+data);
                        webView.loadDataWithBaseURL(null,data,"text/html","UTF-8",null);
                        //web.loadData(data,"text/html","UTF-8");
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });
        });
    }
}