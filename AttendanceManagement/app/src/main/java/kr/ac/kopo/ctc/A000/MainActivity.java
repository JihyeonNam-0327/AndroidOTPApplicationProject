package kr.ac.kopo.ctc.A000;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import org.apache.http.cookie.Cookie;

import java.util.List;

import kr.ac.kopo.ctc.LoginActivity;
import kr.ac.kopo.ctc.R;
import kr.ac.kopo.ctc.SessionControl;
import kr.ac.kopo.ctc.etc.QuestionActivity;
import kr.ac.kopo.ctc.etc.SettingActivity;

public abstract class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private long lastTimeBackPressed;
    protected BottomNavigationView navigationView;
    int classNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        navigationView = (BottomNavigationView) findViewById(R.id.navigation1);
        navigationView.setOnNavigationItemSelectedListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateNavigationBarState();
    }

    // Remove inter-activity transition to avoid screen tossing on tapping bottom navigation items
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        navigationView.postDelayed(() -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(this, HomeFragment.class));
            } else if (itemId == R.id.navigation_qrcode) {
                startActivity(new Intent(this, QRcodeFragment.class));
            } else if (itemId == R.id.navigation_calendar) {
                startActivity(new Intent(this, CalendarFragment.class));
            } else if (itemId == R.id.navigation_chat) {
                startActivity(new Intent(this, ChatFragment.class));
            }
            finish();
        }, 100);
        return true;
    }

    private void updateNavigationBarState(){
        int actionId = getNavigationMenuItemId();
        selectBottomNavigationBarItem(actionId);
    }

    void selectBottomNavigationBarItem(int itemId) {
        MenuItem item = navigationView.getMenu().findItem(itemId);
        item.setChecked(true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // super.onBackPressed(); 바로 종료하지 않는다.
            // 1970년 1월 1일부터 지금까지 경과한 시간을 밀리초 단위로 반환하는 메서드
            if (System.currentTimeMillis() - lastTimeBackPressed < 1500) {
                finish();
                return;
            }
            Toast.makeText(this, "'뒤로' 버튼을 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            lastTimeBackPressed = System.currentTimeMillis();
        }
    }

    abstract int getContentViewId();

    abstract int getNavigationMenuItemId();

    //교수님 소스
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_settings).setVisible(false);
        Drawable icon = AppCompatResources.getDrawable(this, R.drawable.ic_lock_outline_black_24dp);
                DrawableCompat.setTint(icon, ContextCompat.getColor(this, R.color.pure_white));
        Drawable icon2 = AppCompatResources.getDrawable(this, R.drawable.ic_lock_open_black_24dp);
                DrawableCompat.setTint(icon2, ContextCompat.getColor(this, R.color.pure_white));
        Cookie cookie;
        //쿠키 값이 없을 때 -> 로그인해야 한다.
        if(SessionControl.cookies == null || SessionControl.cookies.size() == 0) {
            menu.findItem(R.id.action_logout).setVisible(false);
            //Toast.makeText(this, "쿠키값이 없을때", Toast.LENGTH_SHORT).show();
        }else { //쿠키 값이 있으면 로그아웃 해야 한다.
            //Toast.makeText(this, "쿠키값이 있을때", Toast.LENGTH_SHORT).show();
            menu.findItem(R.id.action_login).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_login) {
            String className = getRunActivityClassName(this);
            if(className.contains("Home")){
                classNumber = 101;
            }else if(className.contains("QR")){
                classNumber = 201;
            }else if(className.contains("Calendar")){
                classNumber = 301;
            }else if(className.contains("Chat")){
                classNumber = 401;
            }
            goScreen(501,false,classNumber);
            return true;
        }
        if (id == R.id.action_logout) {
            if(SessionControl.httpclient == null) {
                // 아무것도 일어나지 않습니다 심지어 버튼은 보이지도 않을거야 ㅎ
            }else{
                exitProcess();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // parameter가 한개
    public void goScreen(int screenNum) {
        goScreen(screenNum, true, 0);
    }

    // parameter가 두개
    public void goScreen(int screenNum, boolean FinishorNot) {
        goScreen(screenNum, FinishorNot,0);
    }

    // parameter 두번째 인자가 false면 액티비티 이동시 죽이고 이동.
    public void goScreen(int screenNum, boolean FinishorNot, int ClassNumber) {

        Intent intent = null;

        switch (screenNum) {
            case 101:
                intent = new Intent(this, HomeFragment.class);

                break;
            case 201:
                intent = new Intent(this, QRcodeFragment.class);

                break;
            case 301:
                intent = new Intent(this, CalendarFragment.class);

                break;
            case 401:
                intent = new Intent(this, ChatFragment.class);

                break;
            case 501:
                intent = new Intent(this, LoginActivity.class);
                break;
        }

        /* 이전액티비티에서 보낸값 담는부분
        if (getExtra_value != null) {
            for (int j = 0; j < getExtra_value.length; j++) {
                intent.putExtra(getExtra_name[j], getExtra_value[j]);
            }
        }*/

        if (!FinishorNot) {
//			//System.out.println("여기로 온다면 이동하고 죽이고");
            intent.putExtra("classNumber",Integer.toString(ClassNumber));
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        } else {
            //    _intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //System.out.println("여기로 온다면 재활용가능한 상태로 대기");
            //    _intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra("classNumber",Integer.toString(ClassNumber));
            startActivity(intent);
        }
    }

    public void exitProcess() {
        new AlertDialog.Builder(this)
                .setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        SessionControl.destroyHttpClient();
                        SessionControl.set_id(null);
                        SessionControl.setPass(null);
                        SessionControl.setName(null);
                        Thread.currentThread().interrupt();
                        Intent _intent = new Intent(getApplicationContext(), HomeFragment.class);  //마지막 화면 인트로
                        _intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                        startActivity(_intent);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                }).show();
    }

    public static String getRunActivityClassName(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = activityManager.getRunningTasks(1);
        ComponentName topActivity = taskInfo.get(0).topActivity;
        String ClassName = topActivity.getClassName();

        return ClassName;
    }
}