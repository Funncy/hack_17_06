package com.lattechiffon.swmanager.Member;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.lattechiffon.swmanager.Project.MainActivity;
import com.lattechiffon.swmanager.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SplashActivity extends Activity {
    SharedPreferences pref;
    SharedPreferences settingPref;
    BackgroundTask task;
    String id, password;
    String result;
    String token;
    int pushFlag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (!networkConnection()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
            builder.setTitle("네트워크에 연결되지 않았습니다.");
            builder.setMessage("로그인 서버에 접근할 수 없습니다.\n기기의 네트워크 연결 상태를 확인하여 주십시오.").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }

        pref = getSharedPreferences("UserData", Activity.MODE_PRIVATE);
        settingPref = PreferenceManager.getDefaultSharedPreferences(this);

        FirebaseMessaging.getInstance().subscribeToTopic("news");
        token = FirebaseInstanceId.getInstance().getToken();

        if (pref.getBoolean("autoLogin", false)) {
            id = pref.getString("id", "");
            password = pref.getString("pw", "");

            Handler hd = new Handler();
            hd.postDelayed(new Runnable() {
                @Override
                public void run() {
                    task = new BackgroundTask();
                    task.execute();
                }
            }, 1000);
        } else {
            Handler hd = new Handler();
            hd.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    SplashActivity.this.finish();
                }
            }, 1000);


        }

        if (settingPref.getBoolean("notifications_new_message", true) == true) {
            pushFlag = 1;
        } else {
            pushFlag = 0;
        }
    }

    private boolean loginValidation(String loginData) {
        try {
            JSONObject json = new JSONObject(loginData);

            if (json.getString("result").equals("Authorized")) {

                return true;
            } else if (json.getString("result").equals("UpdatesRecommended")) {
                Toast.makeText(SplashActivity.this, "이 버전에서는 더 이상 로그인할 수 없습니다. 구글 플레이에서 최신 버전으로 업데이트해주시기 바랍니다.", Toast.LENGTH_LONG).show();

                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("market://details?id=" + getPackageName()));
                    startActivity(intent);
                } catch (android.content.ActivityNotFoundException e) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
                    startActivity(intent);
                }

                return false;
            } else {
                Toast.makeText(SplashActivity.this, "로그인에 실패하였습니다.", Toast.LENGTH_LONG).show();

                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(SplashActivity.this, "로그인에 실패하였습니다.", Toast.LENGTH_LONG).show();

            return false;
        }
    }

    private class BackgroundTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... arg0) {
            result = request("http://mattmatt96.dothome.co.kr/login_member.php");

            return result;
        }

        @Override
        protected void onPostExecute(String a) {
            super.onPostExecute(a);

            if (loginValidation(result)) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

                SplashActivity.this.finish();
            } else {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                SplashActivity.this.finish();
            }
        }
    }

    private String request(String urlStr) {
        StringBuilder json = new StringBuilder();
        String parameter = "id=" + id + "&pw=" + password  + "&flag=" + pushFlag + "&token=" + token + "";
        Log.i("test" , "splash parameter = "+parameter);
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return json.toString();
    }

    private boolean networkConnection() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                return true;
            }
        }

        return false;
    }
}