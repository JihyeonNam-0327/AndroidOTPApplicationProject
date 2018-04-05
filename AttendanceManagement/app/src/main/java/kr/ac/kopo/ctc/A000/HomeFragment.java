package kr.ac.kopo.ctc.A000;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import kr.ac.kopo.ctc.R;
import kr.ac.kopo.ctc.SessionControl;

public class HomeFragment extends MainActivity {
    private TextView mText1;
    private TextView mText2;
    private TextView mText3;

    String strUrl;
    String strCookie;
    String result;
    XMLTask task;
    String name;
    String dept;

    //String otp;
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

        mText1 = (TextView)findViewById(R.id.textView1OnHome);
        mText2 = (TextView)findViewById(R.id.textView2OnHome);
        mText3 = (TextView)findViewById(R.id.textView3OnHome);

        if(SessionControl.get_id()==null&&SessionControl.getPass()==null){
            mText1.setVisibility(View.GONE);
            mText2.setGravity(Gravity.CENTER);
            mText2.setText("먼저, 로그인해 주세요");
            mText3.setVisibility(View.GONE);
        }else{
            task = new XMLTask();
            task.execute();
            mText1.setVisibility(View.VISIBLE);
            mText2.setVisibility(View.VISIBLE);
            mText3.setVisibility(View.VISIBLE);
        }
    }

    @Override
    int getContentViewId() {
        return R.layout.fragment_home;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_home;
    }

    //XML 파싱 부분
    private class XMLTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            strUrl = "http://iamhpd7.cafe24.com/otp/android/XMLforHome.jsp?user_id="+SessionControl.get_id()+"&user_pwd="+SessionControl.getPass(); //탐색하고 싶은 URL
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL Url = new URL(strUrl);  // URL화
                HttpURLConnection conn = (HttpURLConnection) Url.openConnection(); // URL을 연결한 객체 생성
                conn.setRequestMethod("GET"); // get방식 통신
                conn.setDoOutput(true);       // 쓰기모드 지정
                conn.setDoInput(true);        // 읽기모드 지정
                conn.setUseCaches(false);     // 캐싱데이터를 받을지 안받을지
                conn.setDefaultUseCaches(false); // 캐싱데이터 디폴트 값 설정

                strCookie = conn.getHeaderField("Set-Cookie"); //쿠키데이터 보관

                InputStream is = conn.getInputStream();        //input스트림 개방

                StringBuilder builder = new StringBuilder();   //문자열을 담기 위한 객체
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));  //문자열 셋 세팅
                String line;

                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }

                result = builder.toString();

            } catch (MalformedURLException | ProtocolException exception) {
                exception.printStackTrace();
                Toast.makeText(HomeFragment.this,"백그라운드 첫번째 catch 부분",Toast.LENGTH_SHORT).show();
            } catch (IOException io) {
                io.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            System.out.println(result);
            try {

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); // 객체 생성
                DocumentBuilder builder = factory.newDocumentBuilder(); // 객체 생성

                if(result == null){
                    Toast.makeText(HomeFragment.this, "로그인한 뒤 시도하세요 in onPostExecute", Toast.LENGTH_SHORT).show();
                    return;
                }
                ByteArrayInputStream is = new ByteArrayInputStream(result.getBytes("utf-8"));

                //builder를 이용하여 XML 파싱해서 Document 객체 생성
                Document doc = builder.parse(is);

                Element root = doc.getDocumentElement();
                NodeList tag_data = doc.getElementsByTagName("data"); // xml data tag

                Element elmt = (Element) tag_data.item(0);
                name = elmt.getElementsByTagName("name").item(0).getFirstChild().getNodeValue();
                dept = elmt.getElementsByTagName("dept").item(0).getFirstChild().getNodeValue();
                mText2.setText(dept+" "+name+" 님");

            } catch (SAXException e) {
                e.printStackTrace();
                Toast.makeText(HomeFragment.this,"XML 파싱 첫번째 catch 부분"+e.getMessage(),Toast.LENGTH_LONG).show();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
                Toast.makeText(HomeFragment.this,"XML 파싱 두번째 catch 부분",Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(HomeFragment.this,"XML 파싱 세번째 catch 부분",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
