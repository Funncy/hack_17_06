package com.lattechiffon.swmanager.Project;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.lattechiffon.swmanager.Member.LoginActivity;
import com.lattechiffon.swmanager.Member.SettingsActivity;
import com.lattechiffon.swmanager.R;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false; // 앱 종료를 판별하기 위한 변수
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
         /* Toolbar 기능 활성화 */
         Log.i("test","start Main");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pref = getSharedPreferences("UserData", Activity.MODE_PRIVATE);
        editor = pref.edit();
        Log.i("test","start 2");
        /* Fragment 처리를 위한 부분 */
        Class fragmentClass = MyPortFolio.class;
        Fragment fragment = null;
        Log.i("test","start Main2");
        try {
            fragment = (Fragment) fragmentClass.newInstance();
            Log.i("test","start frag");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("test","start Main3 fragment = "+fragment);
        Log.i("test","start Main3");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Log.i("test","start Main444");
        fragmentTransaction.replace(R.id.contentContainer, fragment);
        fragmentTransaction.commit();
        Log.i("test","start Main4");
        /* BottomBar 라이브러리 기능 활성화 */
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                /* Fragment 전환 처리 구현 */
                Fragment fragment = null;
                Class fragmentClass = null;

                Log.i("test","start Main5 tabld ="+tabId);
                if (tabId == R.id.tab_MyPortfolio) {
                    fragmentClass = MyPortFolio.class;
                } else if (tabId == R.id.tab_OpenProject) {
                    fragmentClass = OpenProjectFragment.class;
                } else if (tabId == R.id.tab_MyProject) {
                    fragmentClass = MyProjectFragment.class;
                }else if (tabId == R.id.tab_Message) {
                    fragmentClass = MyMessageFragment.class;
                }

                try {
                    assert fragmentClass != null;
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentContainer, fragment).commit();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Toolbar Option 기능 활성화: 모든 아이템들은 res/menu/main.xml 파일에 선언 */
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Toolbar Option 아이템들의 동작 기능 구현 */
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            editor.clear();
            editor.commit();

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);

            finish();
        } else if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        /* 뒤로가기 버튼 두 번 누르면 앱 종료 기능 구현 */
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.toast_exit, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}
