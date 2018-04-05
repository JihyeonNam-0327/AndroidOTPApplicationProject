package kr.ac.kopo.ctc.A000;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import kr.ac.kopo.ctc.R;
import kr.ac.kopo.ctc.SessionControl;

import static android.view.View.GONE;

/**
 * Created by jihyeon on 2018-03-29.
 */

public class Fragment03 extends Fragment {
    private WebView mWebVidw;
    private WebSettings mWebSettings;
    private TextView mTextView;
    private ProgressDialog dialog;

    public Fragment03(){

    }
    // PlaceholderFragment.newInstance() 와 똑같이 추가
    static Fragment03 newInstance(int SectionNumber) {
        Fragment03 fragment = new Fragment03();
        Bundle args = new Bundle();
        args.putInt("section_number", SectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.layout_fg03, container, false);

        mTextView = (TextView)rootView.findViewById(R.id.webView_chart_text);
        mWebVidw = (WebView)rootView.findViewById(R.id.webView_chart);
        if(SessionControl.get_id()==null && SessionControl.getPass()==null){
            mWebVidw.setVisibility(GONE);
            mWebVidw.setWebContentsDebuggingEnabled(false);
            mTextView.setGravity(Gravity.CENTER);
            mTextView.setTextSize(40);
            mTextView.setText("먼저, 로그인해 주세요.");
        }else{
            mTextView.setVisibility(GONE);
            mWebVidw.setWebViewClient(new WebViewClass()); //클릭 시 새창이 안뜨게
            mWebSettings = mWebVidw.getSettings(); //세부 셋팅 등록
            mWebSettings.setJavaScriptEnabled(true); //자바스크립트 사용 허용
            mWebVidw.setWebContentsDebuggingEnabled(false);
            mWebVidw.loadUrl("http://iamhpd7.cafe24.com/otp/android/ChartJS.jsp?id="+ SessionControl.get_id());
        }
        return rootView;
    }


    private class WebViewClass extends WebViewClient{
        // 페이지 시작
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            dialog = new ProgressDialog(getActivity());
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
            Toast.makeText(getActivity(), "웹 페이지를 불러오는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
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
}