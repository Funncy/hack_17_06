package com.lattechiffon.swmanager.Track;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.lattechiffon.swmanager.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainTrackActivity extends AppCompatActivity {
    SharedPreferences pref;
    SharedPreferences settingPref;
    private String id;
    private String result;
    private Context context;
    private  Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_track);

        pref = getSharedPreferences("UserData", Activity.MODE_PRIVATE);
        settingPref = PreferenceManager.getDefaultSharedPreferences(this);

        id = pref.getString("id","");
        Log.i("test","pref id = "+id);
        id = "id="+id;
        result = "";
        context = this;
        new HttpUtil().execute(id);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("test","result code = "+resultCode+" request ="+requestCode+" id="+id);
        if (resultCode == 1) {

            new HttpUtil().execute(id);
            return;
        }
        else{
            finish();
        }
    }



    public class HttpUtil extends AsyncTask<String, Void, Void> {
        @Override
        public Void doInBackground(String... params) {
            try {
                Log.i("test","start Connection");
                String url = "http://mattmatt96.dothome.co.kr/trackInfo.php";
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
                Log.i("test","start Connection7");
                result = response.toString();
                Log.i("test","start Connection 8 "+result);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String[] result_split = result.split(",");
        Log.i("test","result = "+result);
            switch (Integer.parseInt(result_split[0]))
            {
                case 1: //가상현실
                    i = new Intent(context, VirtualRealityTrack.class);
                    i.putExtra("trackdata",result);
                    startActivityForResult(i,1);
                    break;
                case 2: //응용SW
                    i = new Intent(context, ApplicationSwTrack.class);
                    i.putExtra("trackdata",result);
                    startActivityForResult(i,1);
                    break;
            }

            //내 트랙정보 보고 맞는 트랙 activity 실행
            //intent 에 track 정보 넣어서 보내기


            return null;

        }
    }
}
