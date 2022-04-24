package com.techrzit.loaddatafromremote;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.webkit.WebView;
import android.widget.GridView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends Activity {

    private GridView gridView;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());
    customCourseAdapter customCourseAdapter;
    ArrayList<courseList> arrayList;
    String data;

    private String URL = "https://muthosoft.com/univ/attendance/report.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.gridView);

        arrayList = new ArrayList<>();

        String[] keys = {"my_courses", "sid"};
        String[] values = {"true", "2018360088"};
        httpRequest(keys, values);
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
                courseList courseArrayList = new courseList(data);
                arrayList.add(courseArrayList);

            }
            catch(Exception ex){
                ex.printStackTrace();
            }
            handler.post(()->{
                if(data != null){
                    try{
                        System.out.println("data"+data);
                        loadData(arrayList);
                        //web.loadData(data,"text/html","UTF-8");
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });
        });
    }
    void loadData(ArrayList arrayList){
        //System.out.println(arrayList.size());
        customCourseAdapter = new customCourseAdapter(this,arrayList);
        gridView.setAdapter(customCourseAdapter);
        customCourseAdapter.notifyDataSetChanged();

    }
}