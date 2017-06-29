package com.lattechiffon.swmanager.Project;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lattechiffon.swmanager.R;
import com.lattechiffon.swmanager.Track.ApplicationSwTrack;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyProjectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyMessageFragment extends Fragment {

    private ListView listview ;
    private MessageListViewAdapter adapter;

    private SharedPreferences pref;
    private SharedPreferences settingPref;

    private String UserID;

    public MyMessageFragment() {
        // Required empty public constructor
        Log.i("test", "Frag constructor");
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MyProjectFragment.
     */
    public static MyMessageFragment newInstance() {
        MyMessageFragment fragment = new MyMessageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = getActivity().getSharedPreferences("UserData", Activity.MODE_PRIVATE);
        settingPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        UserID = pref.getString("id","");

        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_message, container, false);

        // Adapter 생성
        adapter = new MessageListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) view.findViewById(R.id.MyMessageListView);





        Log.i("test","httputil start");
        new HttpUtil().execute("id="+UserID);
        Log.i("test","listViewHeightset");

        //Floating Button 설정

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).getSupportActionBar().hide();
        //((MainActivity) getActivity()).getSupportActionBar().show();
        ((MainActivity) getActivity()).setTitle("오픈 프로젝트");
    }

    private static void listViewHeightSet(BaseAdapter listAdapter, ListView listView){
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++){
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public void ReDraw()
    {
   /*     Log.i("test","ReDraw");
        adapter.notifyDataSetChanged();
        adapter.notifyDataSetInvalidated();
        Log.i("test","ReDraw num"+adapter.getCount());
        listview.setAdapter(adapter);
        listViewHeightSet(adapter,listview);*/
    }

    public class HttpUtil extends AsyncTask<String, Void, Void> {
        @Override
        public Void doInBackground(String... params) {
            String[] result;

            try {
                Log.i("test", "start Connection");
                String url = "http://mattmatt96.dothome.co.kr/getMessage.php";
                URL obj = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
                Log.i("test", "start Connection2");
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                //conn.setRequestProperty("Content-Type","application/json");
                Log.i("test", "start Connection3");
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
                Log.i("test","op result = "+response.toString());
                result = response.toString().split(",");
                if (!result[0].contains("empty"))
                    for (int i = 1; i < (Integer.parseInt(result[0]) * 4); i = i + 4) {
                        Log.i("test","add! name = "+result[i]+" text="+result[i+1]+ " "+ result[i+2]+" "+ result[i+3]);
                        adapter.addItem(result[i], result[i + 1] , result[i+2], result[i+3]);
                        //메시지 내용 , 메시지 보낸 사람 아이디, 메시지 타입 , 메시지 보낸 사람 프로젝트 번호
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
                    MessageItem tmp = (MessageItem)adapter.getItem(position);

                    Log.i("test","Item click id = "+tmp.getotherID()+" position="+tmp.getType()+" "+id);

                    // Type 1 - normal 2 - Project Insert   3 - Member add My Project 4 - gongzi
                    int type = Integer.parseInt(tmp.getType());
                    Log.i("test","MyMessage type = "+type);
                    switch (type)
                    {
                        case 1: //normal // 아무 동작 안함
                            break;
                        case 2: // Project 에서 나에게 초대가 날라옴
                            Intent i = new Intent(getActivity(),ProjectDetailActivity.class);
                            //ProjID 구해오기
                            Log.i("test"," projectdetailactivity ");
                            if(!tmp.getotherProjectID().contains("empty")) {
                                Log.i("test"," projectdetailactivity "+tmp.getotherProjectID());
                                i.putExtra("ProjID", tmp.getotherProjectID());
                                i.putExtra("id",UserID);
                                i.putExtra("otherid",tmp.getotherID());
                                i.putExtra("flag",1);
                                startActivity(i);
                            }
                            break;
                        case 3: //Member add My Project
                            Log.i("test"," OtherPortFolioActivity ");
                            Intent i2 = new Intent(getActivity(),OtherPortFolioActivity.class);
                            i2.putExtra("id",tmp.getotherID());
                            i2.putExtra("otherid",UserID);
                            i2.putExtra("flag",1);
                            startActivity(i2);
                            break;
                        case 4:
                            break;
                    }

                    /*Intent i = new Intent(getActivity(),ProjectDetailActivity.class);
                    i.putExtra("ProjID",tmp);
                    startActivity(i);*/
                }
            });
            listview.setAdapter(adapter);
            listViewHeightSet(adapter,listview);

        }

    }


}
