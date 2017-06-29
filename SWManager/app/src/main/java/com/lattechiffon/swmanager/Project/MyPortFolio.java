package com.lattechiffon.swmanager.Project;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.lattechiffon.swmanager.R;
import com.lattechiffon.swmanager.Track.ApplicationSwTrack;
import com.lattechiffon.swmanager.Track.MainTrackActivity;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyProjectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyPortFolio extends Fragment {

    private Context mContext;
    private DisplayMetrics mMetrics;
    private String responce;
    private SharedPreferences pref;
    private SharedPreferences settingPref;

    private TextView PortName;
    private TextView PortContent;

    public  String[] resultdata;

    private TextView textInfo;
    private EditText editInfo;
    private android.support.design.widget.FloatingActionButton floatingButton;

    BackgroundTask task;

    String result;

    public MyPortFolio() {
        // Required empty public constructor

        /* http 통신으로 데이터 가져오기 */
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MyProjectFragment.
     */
    public static MyProjectFragment newInstance() {
        MyProjectFragment fragment = new MyProjectFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("test","onCreate !!!");
        mMetrics = new DisplayMetrics();
        pref = getActivity().getSharedPreferences("UserData", Activity.MODE_PRIVATE);
        settingPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);



        new HttpUtil().execute("id="+pref.getString("id","122088"));
        if (getArguments() != null) {
            Log.i("test","onCreate not null");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_portfolio, container, false);

        PortName = (TextView)view.findViewById(R.id.MyPortName);
        PortContent = (TextView)view.findViewById(R.id.MyPortContentText);

        int img[] = {
                R.drawable.profile_1,R.drawable.profile_1,R.drawable.profile_1,
                R.drawable.profile_1
                      };

        ImageButton trackButton = (ImageButton)view.findViewById(R.id.MyTrackButton);
        textInfo = (TextView) view.findViewById((R.id.MyPortContentText));
        editInfo = (EditText) view.findViewById((R.id.MyPortContentEdit));
        floatingButton = (android.support.design.widget.FloatingActionButton) view.findViewById(R.id.MyPortSetting);

        trackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent i = new Intent(getActivity(), MainTrackActivity.class);
                startActivity(i);

            }
        });

        textInfo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getContext(), "자기소개를 수정해주세요!", Toast.LENGTH_LONG).show();
                v.setVisibility(View.GONE);
                editInfo.setVisibility(View.VISIBLE);
                floatingButton.setVisibility(View.VISIBLE);

                return false;
            }
        });

        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task = new BackgroundTask();
                task.execute();
            }
        });


        MyAdapter adapter = new MyAdapter (
                getActivity(),
                R.layout.myportgridrow,       // GridView 항목의 레이아웃 row.xml
                img);    // 데이터

        ExpandableHeightGridView gv = (ExpandableHeightGridView)view.findViewById(R.id.MyPortImageGrid);
       gv.setExpanded(true);
        gv.setAdapter(adapter);  // 커스텀 아답타를 GridView 에 적용





        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getActivity(),"position : " + position,Toast.LENGTH_LONG).show();
            }
        });


        //Grid View 설정


        return view;


    }





    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).getSupportActionBar().hide();
    }

    public void setPortfolio()
    {
        Log.i("test","result="+resultdata[0]+" "+resultdata[1]+" "+resultdata[2]);
        PortName.setText(resultdata[0]+"\n"+resultdata[1]);
        PortContent.setText(resultdata[2]);
        editInfo.setText(resultdata[2]);

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

    /* 용국 */
    private class BackgroundTask extends AsyncTask<String, Integer, String> {
        ProgressDialog AsycDialog = new ProgressDialog(getContext());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            AsycDialog.setMessage("잠시만 기다려주십시오.");
            AsycDialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            result = request("http://mattmatt96.dothome.co.kr/editPort.php");

            return result;
        }

        @Override
        protected void onPostExecute(String a) {
            super.onPostExecute(a);
            AsycDialog.dismiss();

            if (check(result)) {
                Toast.makeText(getActivity(), "정상적으로 수정되었습니다.", Toast.LENGTH_LONG).show();

                PortContent.setText(editInfo.getText());
                textInfo.setVisibility(View.VISIBLE);
                editInfo.setVisibility(View.GONE);
                floatingButton.setVisibility(View.GONE);
            }
        }
    }

    private String request(String urlStr) {
        StringBuilder json = new StringBuilder();
        String parameter = "id=" + pref.getString("id", "") + "&introduce=" + editInfo.getText().toString()  + "";

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
            Toast.makeText(getContext(), "수정에 실패하였습니다.", Toast.LENGTH_LONG).show();

            return false;
        }
    }

}



