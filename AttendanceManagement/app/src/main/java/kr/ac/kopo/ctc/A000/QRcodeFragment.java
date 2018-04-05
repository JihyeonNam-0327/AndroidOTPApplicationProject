package kr.ac.kopo.ctc.A000;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

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

public class QRcodeFragment extends MainActivity {
    private Button mBtn;
    String strUrl;
    String strCookie;
    String result;
    XMLTask task;
    String otp;
    String name;
    String otp_interval;
    boolean isRunning = false;
    private TextView mBarcodeNumber;
    private TextView mCountDown;
    private long MILLISINFUTURE = 11*1000;
    private long COUNT_DOWN_INTERVAL = 1000;
    private int count = 10;
    private CountDownTimer countDownTimer;
    private ImageView mBarcodeImage;

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

        mBarcodeNumber = (TextView)findViewById(R.id.barcode_number);
        mCountDown = (TextView)findViewById(R.id.countDown);
        mBarcodeImage = (ImageView)findViewById(R.id.imageViewForQRcode);
        mBtn = (Button) findViewById(R.id.buttonForQRcode);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SessionControl.get_id()!=null && SessionControl.getPass()!=null){
                    task = new XMLTask();
                    task.execute();
                }else{
                    Toast.makeText(QRcodeFragment.this, "로그인한 뒤 요청하실 수 있습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    int getContentViewId() { return R.layout.fragment_qrcode; }

    @Override
    int getNavigationMenuItemId() { return R.id.navigation_qrcode; }

    public void generateRQCode(String contents) {
        MultiFormatWriter CodeWriter = new MultiFormatWriter();
        try {
            Bitmap bitmap = toBitmap(CodeWriter.encode(contents, BarcodeFormat.CODE_128, 840, 300));
            mBarcodeImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap toBitmap(BitMatrix matrix) {
        int height = matrix.getHeight();
        int width = matrix.getWidth();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bmp.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }

    //XML 파싱 부분
    private class XMLTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            strUrl = "http://iamhpd7.cafe24.com/otp/android/XMLforMakeCode.jsp?user_id="+ SessionControl.get_id()+"&user_pwd="+SessionControl.getPass(); //탐색하고 싶은 URL
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
                Toast.makeText(QRcodeFragment.this,"백그라운드 첫번째 catch 부분",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(QRcodeFragment.this, "로그인한 뒤 다시 시도하세요 in onPostExecute", Toast.LENGTH_SHORT).show();

                    return;
                }
                ByteArrayInputStream is = new ByteArrayInputStream(result.getBytes("utf-8"));
                //builder를 이용하여 XML 파싱해서 Document 객체 생성

                Document doc = builder.parse(is);

                Element root = doc.getDocumentElement();
                NodeList tag_data = doc.getElementsByTagName("data"); // xml data tag
                Element elmt = (Element) tag_data.item(0);
                otp = elmt.getElementsByTagName("otp").item(0).getFirstChild().getNodeValue();
                name = elmt.getElementsByTagName("name").item(0).getFirstChild().getNodeValue();
                otp_interval = elmt.getElementsByTagName("otp_interval").item(0).getFirstChild().getNodeValue();
                MILLISINFUTURE = Long.valueOf(otp_interval);
                MILLISINFUTURE = MILLISINFUTURE * 60 * 1000;
                count = Integer.parseInt(otp_interval) * 60;
                mBtn.setEnabled(false);
                mBarcodeNumber.setText(otp);
                generateRQCode(otp);
                if(isRunning){
                    countDownTimer.cancel();
                    countDownTimer();
                    countDownTimer.start();
                }else{
                    countDownTimer();
                    countDownTimer.start();
                }

            } catch (SAXException e) {
                e.printStackTrace();
                Toast.makeText(QRcodeFragment.this,"XML 파싱 첫번째 catch 부분"+e.getMessage(),Toast.LENGTH_LONG).show();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
                Toast.makeText(QRcodeFragment.this,"XML 파싱 두번째 catch 부분",Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(QRcodeFragment.this,"XML 파싱 세번째 catch 부분",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void countDownTimer(){

        countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL) {

            public void onTick(long millisUntilFinished) {
                isRunning = true;
                long settingTime = count;
                mCountDown.setText(String.valueOf(getTime(settingTime)));
                count --;
            }

            public void onFinish() {
                isRunning = false;
                mCountDown.setText(String.valueOf("유효 시간이 만료되었습니다."));
                mBarcodeImage.setImageBitmap(null);
                mBarcodeNumber.setVisibility(GONE);
                mBtn.setEnabled(true);
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try{
            countDownTimer.cancel();
        } catch (Exception e) {}
        countDownTimer=null;
    }

    private String getTime(long average) {
        // 초,분,시간 단위로 보여주기 위해 "0:00" type 으로 변경해서 return
        StringBuffer sb = new StringBuffer();
        long second = (average) % 60;
        long minute = (average / 60) % 60;

        if(minute == 0){
            sb.append("0:");
        }else if(minute > 0) {
            sb.append((int) minute + ":");
        }

        if(second == 0) {
            sb.append("00");
        }else if(second >= 0){
            if(Long.toString(second).length() == 1){
                sb.append("0"+(int)second);
            }else {
                sb.append((int) second);
            }
        }

        return sb.toString();
    }
}