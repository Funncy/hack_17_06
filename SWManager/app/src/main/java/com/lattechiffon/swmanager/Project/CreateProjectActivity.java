package com.lattechiffon.swmanager.Project;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.lattechiffon.swmanager.Member.LoginActivity;
import com.lattechiffon.swmanager.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CreateProjectActivity extends AppCompatActivity {

    SharedPreferences pref;
    EditText createProjectName, createProjectInfo;
    CheckBox createProjectAndroid, createProjectWeb, createProjectiOS, createProjectDesigner;
    ImageButton createProjectButton;
    int a = 0, w = 0, i = 0, d = 0;
    String result;
    BackgroundTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);

        pref = getSharedPreferences("UserData", Activity.MODE_PRIVATE);

        createProjectName = (EditText) findViewById(R.id.createProjectName);
        createProjectInfo = (EditText) findViewById(R.id.createProjectInfo);

        createProjectAndroid = (CheckBox) findViewById(R.id.createProjectAndroid);
        createProjectWeb = (CheckBox) findViewById(R.id.createProjectWeb);
        createProjectiOS = (CheckBox) findViewById(R.id.createProjectiOS);
        createProjectDesigner = (CheckBox) findViewById(R.id.createProjectDesigner);

        createProjectButton = (ImageButton) findViewById(R.id.createProjectButton);

        createProjectAndroid.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (createProjectAndroid.isChecked() == true) {
                    a = 1;
                } else {
                    a = 0;
                }
            }
        });

        createProjectWeb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (createProjectWeb.isChecked() == true) {
                    w = 1;
                } else {
                    w = 0;
                }
            }
        });

        createProjectiOS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (createProjectiOS.isChecked() == true) {
                    i = 1;
                } else {
                    i = 0;
                }
            }
        });

        createProjectDesigner.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (createProjectDesigner.isChecked() == true) {
                    d = 1;
                } else {
                    d = 0;
                }
            }
        });

        createProjectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                task = new BackgroundTask();
                task.execute();
            }
        });
    }

    private class BackgroundTask extends AsyncTask<String, Integer, String> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            result = request("http://mattmatt96.dothome.co.kr/makeProject.php");

            return result;
        }

        @Override
        protected void onPostExecute(String a) {
            super.onPostExecute(a);

            if (check(result)) {
                Toast.makeText(getApplicationContext(), "정상적으로 등록되었습니다.", Toast.LENGTH_LONG).show();

                finish();
            }
        }
    }

    private String request(String urlStr) {
        StringBuilder json = new StringBuilder();
        String parameter = "id=" + pref.getString("id", "") + "&name=" + createProjectName.getText().toString()  + "&introduce=" + createProjectInfo.getText().toString()  + "&android=" + a + "&web=" + w + "&ios=" + i + "&design=" + d + "";

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

    private boolean check(String data) {

        if (data.equals("Authorized")) {

            return true;
        } else {
            Toast.makeText(getApplicationContext(), "등록에 실패하였습니다.", Toast.LENGTH_LONG).show();

            return false;
        }
    }
}