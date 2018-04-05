package kr.ac.kopo.ctc.A000;

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

import kr.ac.kopo.ctc.R;
import kr.ac.kopo.ctc.SessionControl;

import static android.view.View.GONE;

/**
 * Created by jihyeon on 2018-03-29.
 */

public class Fragment02 extends Fragment {
    private WebView mWebVidw;
    private WebSettings mWebSettings;
    private TextView mTextView;

    public Fragment02() {

    }

    // PlaceholderFragment.newInstance() 와 똑같이 추가
    static Fragment02 newInstance(int SectionNumber) {
        Fragment02 fragment = new Fragment02();
        Bundle args = new Bundle();
        args.putInt("section_number", SectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.layout_fg02, container, false);
        mTextView = (TextView)rootView.findViewById(R.id.webView_text);
        mWebVidw = (WebView)rootView.findViewById(R.id.webView_calendar);
        if(SessionControl.get_id()==null && SessionControl.getPass()==null){
            mWebVidw.setVisibility(GONE);

            mTextView.setGravity(Gravity.CENTER);
            mTextView.setTextSize(40);
            mTextView.setText("먼저, 로그인해 주세요.");
        }else{
            mTextView.setVisibility(GONE);
            mWebVidw.setWebViewClient(new WebViewClient()); //클릭 시 새창이 안뜨게
            mWebSettings = mWebVidw.getSettings(); //세부 셋팅 등록
            mWebSettings.setJavaScriptEnabled(true); //자바스크립트 사용 허용

            mWebVidw.loadUrl("http://iamhpd7.cafe24.com/otp/android/XMLforMonthlyCheck.jsp?id="+ SessionControl.get_id());
        }

        return rootView;
    }
}
