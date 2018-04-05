package kr.ac.kopo.ctc.A000;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import kr.ac.kopo.ctc.R;

public class ChatFragment extends MainActivity {
    private WebView mWebVidw;
    private WebSettings mWebSettings;
    private ProgressDialog dialog;

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

        mWebVidw = (WebView)findViewById(R.id.webView_chat);
        mWebVidw.setWebViewClient(new WebViewClass()); //클릭 시 새창이 안뜨게
        mWebSettings = mWebVidw.getSettings(); //세부 셋팅 등록
        mWebSettings.setJavaScriptEnabled(true); //자바스크립트 사용 허용
        mWebVidw.setWebContentsDebuggingEnabled(false);
        mWebVidw.loadUrl("http://iamhpd7.cafe24.com/otp/bbs/gongji_list.jsp");
    }

    private class WebViewClass extends WebViewClient{
        // 페이지 시작
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            dialog = new ProgressDialog(ChatFragment.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setTitle("Notification");
            dialog.setMessage("로딩 중 입니다");
            dialog.setProgress(0);
            dialog.setMax(100);
            dialog.setButton("취소", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            dialog.show();
            super.onPageStarted(view, url, favicon);
        }
        // 페이지 로딩시
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Toast.makeText(ChatFragment.this, "웹 페이지를 불러오는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            if (dialog.isShowing()) {
                dialog.cancel();
            }
        }
        // 페이지 종료
        public void onPageFinished(WebView view, String url) {
            if (dialog.isShowing()) {
                dialog.cancel();
            }
        }
    }

    @Override
    int getContentViewId() {
        return R.layout.fragment_chat;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_chat;
    }
}
