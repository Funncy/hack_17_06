package com.lattechiffon.swmanager.Project;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lattechiffon.swmanager.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Member;
import java.net.HttpURLConnection;
import java.net.URL;

public class RecommenMemberListActivity extends AppCompatActivity {


    private ListView listview ;
    private RecommendListViewAdapter adapter;

    private String ProjectID;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommen_member_list);
        getSupportActionBar().setTitle("멤버 추천");
        context = this;
        Intent i = getIntent();
        ProjectID = i.getStringExtra("ProjID");

        // Adapter 생성
        adapter = new RecommendListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.RecommendMemberListView);





        Log.i("test","httputil start key="+ProjectID);
        new HttpUtil().execute("key="+ProjectID);
        Log.i("test","listViewHeightset");

    }

    public class HttpUtil extends AsyncTask<String, Void, Void> {
        @Override
        public Void doInBackground(String... params) {
            String[] result;

            try {
                Log.i("test", "start Connection");
                String url = "http://mattmatt96.dothome.co.kr/recommendMember.php";
                URL obj = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
                Log.i("test", "start Connection2");
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                //conn.setRequestProperty("Content-Type","application/json");
                Log.i("test", "start Connection3 params[0]="+params[0]);
                byte[] outputInBytes = params[0].getBytes("UTF-8");
                OutputStream os = conn.getOutputStream();
                os.write(outputInBytes);
                os.close();
                Log.i("test", "start Connection4");
                int retCode = conn.getResponseCode();
                Log.i("test", "start Connection5");
                InputStream is = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                Log.i("test", "start Connection6");
                while ((line = br.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                br.close();
                Log.i("test","rec result = "+response.toString());
                result = response.toString().split(",");
                if (!result[0].contains("empty"))
                    for (int i = 1; i < (Integer.parseInt(result[0]) * 2); i = i + 2) {
                        Log.i("test","add! name = "+result[i+1]+" text="+result[i+2]);
                        adapter.addItem(ContextCompat.getDrawable(context, R.drawable.profile_default),
                                result[i + 1], result[i],result[i]);
                    }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Log.i("test","onPostExecute num"+adapter.getCount());
            adapter.notifyDataSetChanged();
            adapter.notifyDataSetInvalidated();
            Log.i("test","onPostExecute2 num"+adapter.getCount());
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    MemberItem tmp = (MemberItem)adapter.getItem(position);

                    Log.i("test","Item click id = "+tmp.getMemberID()+" position="+position+" "+id);
                    Intent i = new Intent(context,OtherPortFolioActivity.class);
                    i.putExtra("id",tmp.getMemberID());
                    startActivity(i);
                }
            });
            listview.setAdapter(adapter);


        }

    }
}
