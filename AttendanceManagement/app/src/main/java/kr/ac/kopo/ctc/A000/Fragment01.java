package kr.ac.kopo.ctc.A000;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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

import static android.view.View.GONE;

/**
 * Created by jihyeon on 2018-03-29.
 */

public class Fragment01 extends Fragment {

    String strUrl;
    String strCookie;
    String result;
    XMLTask task;
    ListView listview;
    AdapterForDaily adapter;
    TextView textView;
    TextView time_in;
    TextView time_out;
    TextView time_status;

    public Fragment01(){
    }

    // PlaceholderFragment.newInstance() 와 똑같이 추가
    static Fragment01 newInstance(int SectionNumber) {
        Fragment01 fragment = new Fragment01();
        Bundle args = new Bundle();
        args.putInt("section_number", SectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(SessionControl.get_id()!=null&&SessionControl.getPass()!=null){
            ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.layout_fg01_1, container, false);
            textView = (TextView) rootView.findViewById(R.id.textView_daily);
            time_in = (TextView) rootView.findViewById(R.id.time_in);
            time_out = (TextView) rootView.findViewById(R.id.time_out);
            time_status = (TextView) rootView.findViewById(R.id.time_status);
            task = new XMLTask();
            task.execute();
            return rootView;
        }else{
            ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.layout_fg01, container, false);
            textView = (TextView) rootView.findViewById(R.id.textView_daily);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(40);
            textView.setText("먼저, 로그인해 주세요.");
            return rootView;
        }

    }

    //XML 파싱 부분
    private class XMLTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            strUrl = "http://iamhpd7.cafe24.com/otp/android/XMLforDailyCheck.jsp?user_id="+SessionControl.get_id()+"&user_pwd="+SessionControl.getPass(); //탐색하고 싶은 URL
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
                Toast.makeText(getContext(),"백그라운드 첫번째 catch 부분",Toast.LENGTH_SHORT).show();
            } catch (IOException io) {
                io.printStackTrace();
                Activity root = getActivity();
                root.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(root, "로그인한 뒤 다시 시도하세요", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            System.out.println(result);
            try {
                // Adapter 생성
                adapter = new AdapterForDaily();

                // 리스트뷰 참조 및 Adapter달기
                listview = (ListView) getView().findViewById(R.id.listview_daily);
                listview.setAdapter(adapter);

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); // 객체 생성
                DocumentBuilder builder = factory.newDocumentBuilder(); // 객체 생성

                if(result == null){
                    Toast.makeText(getActivity(), "로그인한 뒤 다시 시도하세요 in onPostExecute", Toast.LENGTH_SHORT).show();

                    return;
                }
                ByteArrayInputStream is = new ByteArrayInputStream(result.getBytes("utf-8"));
                //builder를 이용하여 XML 파싱해서 Document 객체 생성

                Document doc = builder.parse(is);

                String time_in = "";
                String time_out = "";
                String status = "";

                Element root = doc.getDocumentElement();
                NodeList tag_data = doc.getElementsByTagName("data"); // xml data tag

                for (int i = 0; i < tag_data.getLength(); i++) {
                    Element elmt = (Element) tag_data.item(i);
                    time_in = elmt.getElementsByTagName("time_in").item(0).getFirstChild().getNodeValue();
                    if(time_in.equals("null")){
                        time_in = "-";
                    }
                    time_out = elmt.getElementsByTagName("time_out").item(0).getFirstChild().getNodeValue();
                    if(time_out.equals("null")){
                        time_out = "-";
                    }
                    status = elmt.getElementsByTagName("status").item(0).getFirstChild().getNodeValue();
                    adapter.addItem(time_in, time_out, status);
                }

            } catch (SAXException e) {
                e.printStackTrace();
                Toast.makeText(getContext(),"XML 파싱 첫번째 catch 부분"+e.getMessage(),Toast.LENGTH_LONG).show();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
                Toast.makeText(getContext(),"XML 파싱 두번째 catch 부분",Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(),"XML 파싱 세번째 catch 부분",Toast.LENGTH_SHORT).show();
            }
            textView.setText("오늘의 입퇴실 현황 입니다.");
        }
    }
}