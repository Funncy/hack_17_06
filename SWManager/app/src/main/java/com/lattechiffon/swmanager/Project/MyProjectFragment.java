package com.lattechiffon.swmanager.Project;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lattechiffon.swmanager.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyProjectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProjectFragment extends Fragment {

    private TextView ProjectName;
    private TextView ProjectContent;
    private TextView ProjectMembers;
    private FloatingActionButton fab;

    private DisplayMetrics mMetrics;


    private SharedPreferences pref;
    private SharedPreferences settingPref;

    private String ProjID;

    private String[] resultdata;

    public MyProjectFragment() {
        // Required empty public constructor
        Log.i("test","Frag constructor");
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MyProjectFragment.
     */
    public static MyProjectFragment newInstance() {
        Log.i("test","Frag instance");
        MyProjectFragment fragment = new MyProjectFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("test","Frag Oncreate");
        pref = getActivity().getSharedPreferences("UserData", Activity.MODE_PRIVATE);
        settingPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);


        new HttpUtil().execute("id="+pref.getString("id",""));



        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i("test","Frag OncreateView");
        View view = inflater.inflate(R.layout.fragment_my_project, container, false);

        ProjectName = (TextView)view.findViewById(R.id.MyProjectName);
        ProjectContent =(TextView)view.findViewById(R.id.MyProjectDetailContent);
        ProjectMembers = (TextView)view.findViewById(R.id.MyProjectMembers);
        fab = (FloatingActionButton)view.findViewById(R.id.MyProjectSendMessageFab);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //((MainActivity)getActivity()).getSupportActionBar().hide();
        ((MainActivity) getActivity()).getSupportActionBar().show();
       ((MainActivity) getActivity()).setTitle("나의 프로젝트");
    }

    public void SetData(String Name,String sMembers,String Content,int total)
    {
        ProjectName.setText(Name);
        ProjectContent.setText(Content);
        ProjectMembers.setText(sMembers);



    }



    public class HttpUtil extends AsyncTask<String, Void, Void> {
        @Override
        public Void doInBackground(String... params) {
            try {
                Log.i("test","myprojcet start Connection params="+params[0].toString());
                String url = "http://mattmatt96.dothome.co.kr/getJoinedProject.php";
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

                resultdata = response.toString().split(",");


                //resultdata[0] //프로젝트키
                //1//프로젝트이름
                //프로젝트방장 리더
                //프로젝트 소개
                //방장여부
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
            //Log.i("test","MyProjectFrag onPost="+resultdata[0]+" "+resultdata[1]+" "+resultdata[2]);
            if(!resultdata[0].contains("empty")) {
                ProjID = resultdata[0];
                String Member = "프로젝트장: " + resultdata[2] + "\n";



                if (Integer.parseInt(resultdata[4]) == 2) { //방장일때 fab 버튼 활성화
                    fab.setVisibility(View.VISIBLE);
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //RecommendMember
                            Intent i = new Intent(getActivity(),RecommenMemberListActivity.class);

                            i.putExtra("ProjID",ProjID);
                            startActivity(i);
                        }
                    });
                }

                if(!resultdata[5].contains("empty")) {
                    for (int i = 0; i < Integer.parseInt(resultdata[5]); i++) {
                        Member += "프로젝트 팀원: " + resultdata[i + 6] + "\n";
                    }
                    Log.i("test", "result =" + resultdata[1] + " " + resultdata[3] + " member=" + Member);
                    SetData(resultdata[1], Member, resultdata[3], Integer.parseInt(resultdata[5]));
                }
            }else{
                SetData("프로젝트가 없습니다.","","프로젝트에 참가해주세요.",0);
            }
        }
    }
}
