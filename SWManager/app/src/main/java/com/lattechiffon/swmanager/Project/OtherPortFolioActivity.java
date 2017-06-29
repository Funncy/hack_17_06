package com.lattechiffon.swmanager.Project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.lattechiffon.swmanager.R;
import com.lattechiffon.swmanager.Track.MainTrackActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class OtherPortFolioActivity extends AppCompatActivity {

    private Context mContext;
    private String responce;
    private SharedPreferences pref;
    private SharedPreferences settingPref;

    private TextView PortName;
    private TextView PortContent;

    private DisplayMetrics mMetrics;

    public  String[] resultdata;
    private String UserID;


    BackgroundTask task;
    String result;
    FloatingActionButton fab;


    private String OtherID;
    private int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_port_folio);
        mContext = this;

        Intent i = getIntent();
        UserID = i.getStringExtra("id");
        OtherID = i.getStringExtra("otherid");

        flag = i.getIntExtra("flag",0);

        Log.i("test","onCreate !!!");
        pref = getSharedPreferences("UserData", Activity.MODE_PRIVATE);
        settingPref = PreferenceManager.getDefaultSharedPreferences(this);
        mMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mMetrics);

        PortName = (TextView)findViewById(R.id.otherportname);
        PortContent = (TextView)findViewById(R.id.otherportcontent);

        new HttpUtil().execute("id="+UserID);

        int []img ={
                R.drawable.profile_1,R.drawable.profile_1,R.drawable.profile_1,R.drawable.profile_1,R.drawable.profile_1,R.drawable.profile_1,R.drawable.profile_1,R.drawable.profile_1
        };

        MyAdapter adapter = new MyAdapter(
                mContext,
                R.layout.myportgridrow,       // GridView 항목의 레이아웃 row.xml
                img);    // 데이터

        ExpandableHeightGridView gv = (ExpandableHeightGridView)findViewById(R.id.otherportgrid);
        gv.setExpanded(true);
        gv.setAdapter(adapter);  // 커스텀 아답타를 GridView 에 적용
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(mContext,"position : " + position,Toast.LENGTH_LONG).show();
            }
        });

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.othersendmessage);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag==0)
                {
                    //fcm 발송
                    task = new BackgroundTask();
                    task.execute();

                }
                else
                {
                    //이 사람 바로 나의 프로젝트에 가입
                    String body= "id="+OtherID+"&memid="+UserID;
                    Log.i("test","other port body="+body);
                    new HttpUtil2().execute(body);
                }

            }
        });



        //Grid View 설정

    }

    public void setPortfolio()
    {
        Log.i("test","result="+resultdata[0]+" "+resultdata[1]+" "+resultdata[2]);
        PortName.setText(resultdata[0]+"\n"+resultdata[1]);
        PortContent.setText(resultdata[2]);
    }




    class MyAdapter extends BaseAdapter {
        Context context;
        int layout;
        int img[];
        LayoutInflater inf;

        public MyAdapter(Context context, int layout, int[] img) {
            this.context = context;
            this.layout = layout;
            this.img = img;
            inf = (LayoutInflater) context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return img.length;
        }

        @Override
        public Object getItem(int position) {
            return img[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int rowWidth = (mMetrics.widthPixels) / 3;

            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(context);
                imageView.setLayoutParams(new GridView.LayoutParams(rowWidth, rowWidth));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setPadding(10, 10, 10, 10);
            } else {
                imageView = (ImageView) convertView;
            }
            imageView.setImageResource(img[position]);
            return imageView;


        }
    }

    public class HttpUtil extends AsyncTask<String, Void, Void> {
        @Override
        public Void doInBackground(String... params) {
            try {
                Log.i("test","start Connection");
                String url = "http://mattmatt96.dothome.co.kr/getPort.php";
                URL obj = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
                Log.i("test","start Connection2");
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                //conn.setRequestProperty("Content-Type","application/json");
                Log.i("test","start Connection3 id="+params[0].toString());
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
                Log.i("test","start Connection6 "+response.toString());
                while((line = br.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                Log.i("test","start Connection6 "+response.toString());
                resultdata = response.toString().split(",");
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }


        @Override
        protected void onPostExecute(Void re) {
            super.onPostExecute(re);
            setPortfolio();

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
                Log.i("test","start Connection6 response="+response.toString());
                finish();
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
            result = request("http://mattmatt96.dothome.co.kr/request_invite_project.php");

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
        String parameter = "fromid=" + pref.getString("id", "") + "&toid=" + resultdata[1]  + "&type=2";
        Log.i("test","otherportfolio request FCM = "+parameter);
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
            Toast.makeText(getApplicationContext(), "초대에 실패하였습니다.", Toast.LENGTH_LONG).show();

            return false;
        }
    }

}


