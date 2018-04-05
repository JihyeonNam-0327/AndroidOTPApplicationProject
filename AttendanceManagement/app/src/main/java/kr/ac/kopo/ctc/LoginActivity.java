package kr.ac.kopo.ctc;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
//import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import kr.ac.kopo.ctc.A000.CalendarFragment;
import kr.ac.kopo.ctc.A000.ChatFragment;
import kr.ac.kopo.ctc.A000.HomeFragment;
import kr.ac.kopo.ctc.A000.QRcodeFragment;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    // UI references.
    private AutoCompleteTextView mIdView;   // 로그인 아이디
    private EditText mPasswordView;         // 로그인 패스워드
    private Button mLoginButton;            // 로그인 버튼

    private long lastTimeBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        //login id input
        mIdView = (AutoCompleteTextView)findViewById(R.id._id);
        //login passwd input
        mPasswordView = (EditText)findViewById(R.id.password);
        //login button
        mLoginButton = (Button)findViewById(R.id.sign_in_button);

        mLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendPostRequest(mIdView.getText().toString(), mPasswordView.getText().toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.action_login).setVisible(false);
        menu.findItem(R.id.action_logout).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
            {
                //왔던 곳으로 되돌아갑니다.
                Intent intent = getIntent();
                int classNumber = Integer.parseInt(intent.getStringExtra("classNumber"));

                switch (classNumber) {
                    case 101:
                        intent = new Intent(LoginActivity.this, HomeFragment.class);
                        break;
                    case 201:
                        intent = new Intent(LoginActivity.this, QRcodeFragment.class);
                        break;
                    case 301:
                        intent = new Intent(LoginActivity.this, CalendarFragment.class);
                        break;
                    case 401:
                        intent = new Intent(LoginActivity.this, ChatFragment.class);
                        break;
                }
                if (true) {
                    //System.out.println("여기로 온다면 이동하고 죽이고");
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                }
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        //왔던 곳으로 되돌아갑니다.
        Intent intent = getIntent();
        int classNumber = Integer.parseInt(intent.getStringExtra("classNumber"));

        switch (classNumber) {
            case 101:
                intent = new Intent(LoginActivity.this, HomeFragment.class);
                break;
            case 201:
                intent = new Intent(LoginActivity.this, QRcodeFragment.class);
                break;
            case 301:
                intent = new Intent(LoginActivity.this, CalendarFragment.class);
                break;
            case 401:
                intent = new Intent(LoginActivity.this, ChatFragment.class);
                break;
        }
        if (true) {
            //System.out.println("여기로 온다면 이동하고 죽이고");
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        }
        return;
    }

    private void sendPostRequest(String user_id, String user_pwd) {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            String result;
            @Override
            protected String doInBackground(String... params) {
                String user_id = params[0];
                String user_pwd = params[1];

                //HttpClient httpClient = new DefaultHttpClient();
                HttpClient httpClient = SessionControl.getHttpclient(); // 기존의 DefaultHttpClient 쓰던 것을 변경
                HttpPost httpPost = new HttpPost("http://iamhpd7.cafe24.com/otp/android/loginCheckForAndroid.jsp"); // 여러분의 주소로 변경할 것
                BasicNameValuePair username = new BasicNameValuePair("user_id", user_id); // 서버의 프로그램에 따라 달라질수 있음
                BasicNameValuePair password = new BasicNameValuePair("user_pwd", user_pwd);// 포스트로 보내는 변수

                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(username);
                nameValuePairList.add(password);

                try {
                    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList,"UTF-8");
                    httpPost.setEntity(urlEncodedFormEntity);
                    try {
                        HttpResponse httpResponse = httpClient.execute(httpPost);



                        InputStream inputStream = httpResponse.getEntity().getContent();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        String bufferedStrChunk = null;

                        while((bufferedStrChunk = bufferedReader.readLine()) != null){
                            stringBuilder.append(bufferedStrChunk);
                        }
                        result = stringBuilder.toString();


                        return stringBuilder.toString();
                    }
                    catch (ClientProtocolException cpe) {
                        cpe.printStackTrace();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                } catch (UnsupportedEncodingException uee) {
                    uee.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(String result){
                super.onPostExecute(result);
                //////         성공/실패 여부에 따라 적절히  대응하자.
                if(result.contains("성공")) {
                    SessionControl.cookies = SessionControl.httpclient.getCookieStore().getCookies();//추가
                    //읽어온 쿠키값을 한번 출력 하여 보자.
                    Cookie cookie;
                    if (!SessionControl.cookies.isEmpty()) {
                        for (int i = 0; i < SessionControl.cookies.size(); i++) {
                            cookie = SessionControl.cookies.get(i);
                        }
                    }
                    SessionControl.setCookies(SessionControl.httpclient.getCookieStore().getCookies());
                    SessionControl.set_id(user_id);
                    SessionControl.setPass(user_pwd);


                    //왔던 곳으로 돌아갑니다.
                    Intent intent = getIntent();
                    int classNumber = Integer.parseInt(intent.getStringExtra("classNumber"));

                    switch (classNumber) {
                        case 101:
                            intent = new Intent(LoginActivity.this, HomeFragment.class);
                            break;
                        case 201:
                            intent = new Intent(LoginActivity.this, QRcodeFragment.class);
                            break;
                        case 301:
                            intent = new Intent(LoginActivity.this, CalendarFragment.class);
                            break;
                        case 401:
                            intent = new Intent(LoginActivity.this, ChatFragment.class);
                            break;
                    }
                    if(true) {
            			//System.out.println("여기로 온다면 이동하고 죽이고");
                        intent.putExtra("classNumber",Integer.toString(classNumber));
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                    }
                }else{
                    Toast.makeText(getApplication(), "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }/// onPostExecute
        }

        ///////////////////////////////////
        // 이곳에서 로그인을 위한 웹문서를 비동기 백그라운드로 요청한다.

        SendPostReqAsyncTask sendTask = new SendPostReqAsyncTask();
        sendTask.execute(user_id, user_pwd);     // 비동기 방식 백그라운드로 받아 오기

        ///////////////////////////////////
    }
}