package com.lattechiffon.swmanager.Member;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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


public class LoginActivity extends Activity {

    EditText idInput, passwordInput;
    CheckBox autoLogin;
    Button loginButton;
    Button joinButton;
    Boolean loginChecked = false;
    SharedPreferences pref;
    SharedPreferences settingPref;
    SharedPreferences.Editor editor;
    String result;
    BackgroundTask task;
    String token;
    int pushFlag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        if (!networkConnection()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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
        editor = pref.edit();

        FirebaseMessaging.getInstance().subscribeToTopic("news");
        token = FirebaseInstanceId.getInstance().getToken();

        if (settingPref.getBoolean("notifications_new_message", true) == true) {
            pushFlag = 1;
        } else {
            pushFlag = 0;
        }

        idInput = (EditText) findViewById(R.id.idInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);
        autoLogin = (CheckBox) findViewById(R.id.checkBox);
        loginButton = (Button) findViewById(R.id.loginButton);
        joinButton = (Button) findViewById(R.id.signupButton);

        passwordInput.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    loginButton.callOnClick();

                    return true;
                }
                return false;
            }
        });

        if (pref.getBoolean("autoLogin", false)) {
            idInput.setText(pref.getString("id", ""));
            passwordInput.setText(pref.getString("pw", ""));
            autoLogin.setChecked(true);

            task = new BackgroundTask();
            task.execute();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task = new BackgroundTask();
                task.execute();
            }
        });

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
            }
        });

        autoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    loginChecked = true;
                } else {
                    loginChecked = false;
                    editor.clear();
                    editor.commit();
                }
            }
        });
    }

    private boolean loginValidation(String loginData) {
        try {
            JSONObject json = new JSONObject(loginData);

            if (json.getString("result").equals("Authorized")) {
                editor.putString("no", json.getString("no"));
                editor.putString("name", json.getString("name"));
                editor.putString("id", idInput.getText().toString());
                editor.putString("pw", passwordInput.getText().toString());
                editor.putString("phone", json.getString("phone"));
                editor.putString("major", json.getString("major"));
                editor.commit();

                return true;
            } else {
                Toast.makeText(LoginActivity.this, "로그인에 실패하였습니다.", Toast.LENGTH_LONG).show();

                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(LoginActivity.this, "로그인에 실패하였습니다.", Toast.LENGTH_LONG).show();

            return false;
        }
    }

    private class BackgroundTask extends AsyncTask<String, Integer, String> {
        ProgressDialog AsycDialog = new ProgressDialog(LoginActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            AsycDialog.setMessage("잠시만 기다려주십시오.");
            AsycDialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            result = request("http://mattmatt96.dothome.co.kr/login_member.php");

            return result;
        }

        @Override
        protected void onPostExecute(String a) {
            super.onPostExecute(a);
            AsycDialog.dismiss();

            if (loginValidation(result)) {
                Toast.makeText(LoginActivity.this, "로그인에 성공하였습니다.", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

                if (loginChecked) {
                    editor.putBoolean("autoLogin", true);
                    editor.commit();
                }

                finish();
            } /*else {
                // autoLogin.setChecked(false);

                //Toast.makeText(LoginActivity.this, "로그인에 실패하였습니다.", Toast.LENGTH_LONG).show();
            }*/
        }
    }

    private String request(String urlStr) {
        StringBuilder json = new StringBuilder();
        String parameter = "id=" + idInput.getText().toString() + "&pw=" + passwordInput.getText().toString()  + "&flag=" + pushFlag + "&token=" + token + "";

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("종료").setMessage("정말 앱을 종료하시겠습니까?").setPositiveButton(getString(R.string.alert_positive_exit), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    moveTaskToBack(true);
                    finish();
                    android.os.Process.killProcess(android.os.Process.myPid());
                }

            }).setNegativeButton(getString(R.string.alert_negative_cancel), null).show();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}