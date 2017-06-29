package com.lattechiffon.swmanager.Project;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.lattechiffon.swmanager.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProjectDetailActivity extends AppCompatActivity {

    private String Id ;
    private TextView ProjectName;
    private TextView Members;
    private TextView ProContent;
    private String[] resultdata;

    BackgroundTask task;
    String result;
    FloatingActionButton fab;

    SharedPreferences pref;
    SharedPreferences.Editor editor;


    private String UserID;
    private String OtherID;
    private int flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        pref = getSharedPreferences("UserData", Activity.MODE_PRIVATE);
        editor = pref.edit();


        ProjectName = (TextView)findViewById(R.id.ProjectName);
        ProContent = (TextView)findViewById(R.id.ProjectDetailContent);
        Members = (TextView)findViewById(R.id.ProjectMembers);

        Intent i = getIntent();
        Id = i.getStringExtra("ProjID");
        flag = i.getIntExtra("flag",0);

        UserID = i.getStringExtra("id");
        OtherID = i.getStringExtra("otherid");

        Log.i("test","Project Detail get id = "+Id);

        //data 호출
        new HttpUtil().execute("key="+Id);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.SendMessageFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //Fcm Send Message
                if(flag==0)
                {
                    task = new BackgroundTask();
                    task.execute();
                 //fcm message
                }else
                {
                    String body = "id="+OtherID+"&memid="+UserID;
                    new HttpUtil2().execute(body);
                    //joinProject.php
                }
            }
        });
    }

    public void SetData(String Name,String sMembers,String Content)
    {
        ProjectName.setText(Name);
        ProContent.setText(Content);
        Members.setText(sMembers);
    }

    public class HttpUtil extends AsyncTask<String, Void, Void> {
        @Override
        public Void doInBackground(String... params) {
            try {
                Log.i("test","start Connection");
                String url = "http://mattmatt96.dothome.co.kr/detailProject.php";
                URL obj = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
                Log.i("test","start Connection2");
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                //conn.setRequestProperty("Content-Type","application/json");
                Log.i("test","start Connection3");
                byte[] outputInBytes = params[0].getBytes("UTF-8");
                OutputStream os = conn.getOutputStream();
                os.write( outputInBytes );
                os.close();
                Log.i("test","start Connection4");
                int retCode = conn.getResponseCode();
                Log.i("test","start Connection5");
                InputStream is = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                Log.i("test","start Connection6");
                while((line = br.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }

                resultdata = response.toString().split(",");
                //resultdata[0] //프로젝트키
                //1//프로젝트이름
                //프로젝트방장 리더
                //프로젝트 소개
                //멤버수
                //멤버들~

                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            String Member = "프로젝트장: "+resultdata[2]+"\n";
            for(int i=0;i<Integer.parseInt(resultdata[4]);i++)
            {
                Member+="프로젝트 팀원: "+resultdata[i+5]+"\n";
            }
            Log.i("test","result ="+resultdata[1]+" "+resultdata[3]+" member="+Member);
            SetData(resultdata[1],Member,resultdata[3]);

        }
    }

    public class HttpUtil2 extends AsyncTask<String, Void, Void> {
        @Override
        public Void doInBackground(String... params) {
            try {
                Log.i("test","start Connection");
                String url = "http://mattmatt96.dothome.co.kr/joinProject.php";
                URL obj = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
                Log.i("test","start Connection2");
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                //conn.setRequestProperty("Content-Type","application/json");
                Log.i("test","start Connection3");
                byte[] outputInBytes = params[0].getBytes("UTF-8");
                OutputStream os = conn.getOutputStream();
                os.write( outputInBytes );
                os.close();
                Log.i("test","start Connection4");
                int retCode = conn.getResponseCode();
                Log.i("test","start Connection5");
                InputStream is = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                Log.i("test","start Connection6");
                while((line = br.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }

                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

    }

    /* 용국 */
    private class BackgroundTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... arg0) {
            result = request("http://mattmatt96.dothome.co.kr/request_join_project.php");

            return result;
        }

        @Override
        protected void onPostExecute(String a) {
            super.onPostExecute(a);

            if (check(result)) {
                Toast.makeText(getApplicationContext(), "정상적으로 신청되었습니다.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String request(String urlStr) {
        StringBuilder json = new StringBuilder();
        String parameter = "fromid=" + pref.getString("id", "") + "&toid=" + resultdata[2]  + "&type=3";
        Log.i("test","prodetail request FCM = "+parameter);
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (conn != null) {
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                OutputStream os = conn.getOutputStream();
                os.write(parameter.getBytes("utf-8"));
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;

                    while ((line = reader.readLine()) != null) {
                        json.append(line).append("");
                    }

                    reader.close();

                }
                conn.disconnect();
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return json.toString();
    }

    private boolean check(String data) {

        if (data.equals("Authorized")) {

            return true;
        } else {
            Toast.makeText(getApplicationContext(), "신청에 실패하였습니다.", Toast.LENGTH_LONG).show();

            return false;
        }
    }
}