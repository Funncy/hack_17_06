package com.lattechiffon.swmanager.Track;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lattechiffon.swmanager.R;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class VirtualRealityTrack extends AppCompatActivity {

    ImageButton[] imageButtons;
    private String trackData;
    private String[] trackRealData;

    private SharedPreferences pref;
    private SharedPreferences settingPref;
    private String trackNum;
    private String userID;
    private TextView textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virtual_reality_track);

        pref = getSharedPreferences("UserData", Activity.MODE_PRIVATE);
        settingPref = PreferenceManager.getDefaultSharedPreferences(this);

        userID = pref.getString("id","122088");

        imageButtons = new ImageButton[10];
        textview = (TextView)findViewById(R.id.vrtrackText);

        imageButtons[0] = (ImageButton)findViewById(R.id.virtual_node1);
        imageButtons[1] = (ImageButton)findViewById(R.id.virtual_node2);
        imageButtons[2] = (ImageButton)findViewById(R.id.virtual_node3);
        imageButtons[3] = (ImageButton)findViewById(R.id.virtual_node4);
        imageButtons[4] = (ImageButton)findViewById(R.id.virtual_node5);
        imageButtons[5] = (ImageButton)findViewById(R.id.virtual_node6);
        imageButtons[6] = (ImageButton)findViewById(R.id.virtual_node7);
        imageButtons[7] = (ImageButton)findViewById(R.id.virtual_node8);
        imageButtons[8] = (ImageButton)findViewById(R.id.virtual_node9);
        imageButtons[9] = (ImageButton)findViewById(R.id.virtual_node10);


        Intent i =getIntent();
        trackData = i.getStringExtra("trackdata");

        trackRealData = trackData.split(",");
        trackNum = trackRealData[0];
        Log.i("test","trackdata  = "+trackRealData[0] + " "+trackRealData[1]);
        if(!trackRealData[1].contains("empty")) {

            if(Integer.parseInt(trackRealData[1])>=6)
                textview.setText("수료 하셨습니다.");
            else
            {
                int num = Integer.parseInt(trackRealData[1]);
                textview.setText(" "+num+"/6 개 수강하셨습니다.");
            }

            Log.i("test","track!! track"+trackRealData[1]);
            for (int j = 0; j < Integer.parseInt(trackRealData[1]); j++) {
                ImageNode(Integer.parseInt(trackRealData[j + 2]) - 1);
            }
        }



        ImageSetonClick();

    }

    public void ImageNode(int i)
    {
        //data 가져온걸로 이미지 설정
        Log.i("test","imageNode i="+i+" ");
        imageButtons[i].setImageResource(R.drawable.checknode);

    }

    public void ImageSetonClick()
    {

        imageButtons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSimple(1);
            }
        });
        imageButtons[0].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.i("test","onLongClick");
                DialogDelete(1);
                return true;
            }
        });

        imageButtons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSimple(2);
            }
        });
        imageButtons[1].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DialogDelete(2);
                return true;
            }
        });
        imageButtons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSimple(3);
            }
        });
        imageButtons[2].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.i("test","onLongClick");
                DialogDelete(3);
                return true;
            }
        });
        imageButtons[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSimple(4);
            }
        });
        imageButtons[3].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.i("test","onLongClick");
                DialogDelete(4);
                return true;
            }
        });
        imageButtons[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSimple(5);
            }
        });
        imageButtons[4].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.i("test","onLongClick");
                DialogDelete(5);
                return true;
            }
        });
        imageButtons[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSimple(6);
            }
        });
        imageButtons[5].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.i("test","onLongClick");
                DialogDelete(6);
                return true;
            }
        });
        imageButtons[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSimple(7);
            }
        });
        imageButtons[6].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.i("test","onLongClick");
                DialogDelete(7);
                return true;
            }
        });
        imageButtons[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSimple(8);
            }
        });
        imageButtons[7].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.i("test","onLongClick");
                DialogDelete(8);
                return true;
            }
        });
        imageButtons[8].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSimple(9);
            }
        });
        imageButtons[8].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.i("test","onLongClick");
                DialogDelete(9);
                return true;
            }
        });
        imageButtons[9].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSimple(10);
            }
        });
        imageButtons[9].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.i("test","onLongClick");
                DialogDelete(10);
                return true;
            }
        });
    }

    private void DialogSimple(final int nodenum){
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setMessage("수강을 완료 하셨습니까 ?").setCancelable(
                false).setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String body = "id="+userID+"&track="+trackNum+"&node="+nodenum;
                        Log.i("test","Connect Add Track body = "+body);
                        try {
                            new HttpUtil().execute(body);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'NO' Button
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alt_bld.create();
        // Title for AlertDialog
        alert.setTitle("수강 완료");
        // Icon for AlertDialog
        alert.setIcon(R.drawable.logoicon);
        alert.show();
    }

    private void DialogDelete(final int nodenum){
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setMessage("수강을 취소하시겠습니까 ?").setCancelable(
                false).setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String body = "id="+userID+"&track="+trackNum+"&node="+nodenum;
                        Log.i("test","Connect delete Track body = "+body);
                        try {
                            new HttpUtilDelete().execute(body);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'NO' Button
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alt_bld.create();
        // Title for AlertDialog
        alert.setTitle("수강 완료");
        // Icon for AlertDialog
        alert.setIcon(R.drawable.logoicon);
        alert.show();
    }

    public class HttpUtil extends AsyncTask<String, Void, Void> {
        @Override
        public Void doInBackground(String... params) {
            try {
                Log.i("test","start Connection");
                String url = "http://mattmatt96.dothome.co.kr/attendTrack.php";
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
            Intent intent = new Intent();
            setResult(1, intent);
            finish();
            return null;

        }
    }

    public class HttpUtilDelete extends AsyncTask<String, Void, Void> {
        @Override
        public Void doInBackground(String... params) {
            try {
                Log.i("test","start delete Connection body"+params[0]);
                String url = "http://mattmatt96.dothome.co.kr/deleteInfo.php";
                URL obj = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
                Log.i("test","start delete Connection2");
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                //conn.setRequestProperty("Content-Type","application/json");
                Log.i("test","start delete Connection3");
                byte[] outputInBytes = params[0].getBytes("UTF-8");
                OutputStream os = conn.getOutputStream();
                os.write( outputInBytes );
                os.close();
                Log.i("test","start delete Connection4");
                int retCode = conn.getResponseCode();
                Log.i("test","start delete Connection5");
                InputStream is = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                Log.i("test","start delete Connection6");
                while((line = br.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                Log.i("test"," "+response);
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Intent intent = new Intent();
            setResult(1, intent);
            finish();
            return null;

        }
    }

}

