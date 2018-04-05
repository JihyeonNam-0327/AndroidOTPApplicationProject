package kr.ac.kopo.ctc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import org.apache.http.cookie.Cookie;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by jihyeon on 2018-03-29.
 */

public class MyWebViewActivity extends Activity {

    WebView mWebView;

    //@Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        Intent intent = getIntent();
        String myUrlAddress = intent.getStringExtra("myurl");

        mWebView = (WebView) findViewById(R.id.webview);

        /////////////////   추가된 부분  /////////////

        Cookie sessionCookie = null;
        if (SessionControl.cookies == null || SessionControl.cookies.size() == 0) {
            finish();
        }
        if (SessionControl.cookies.size() > 0) {
            sessionCookie = SessionControl.cookies.get(0);
        }

        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        if (sessionCookie != null) {
            cookieManager.removeSessionCookie();
            String cookieString = sessionCookie.getName() + "=" + sessionCookie.getValue() + "; domain=" + sessionCookie.getDomain();
            cookieManager.setCookie(myUrlAddress, cookieString);
            CookieSyncManager.getInstance().sync();
        }

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setInitialScale(1);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebView.setScrollbarFadingEnabled(false);

        // mWebView.addJavascriptInterface(new WebAppInterface(this), "Android");
        mWebView.setWebViewClient(new WebViewClient());//모든 링크도 내 웹뷰에서 열리도록 한다.
        mWebView.loadUrl(myUrlAddress);

    }

    public void onPause() {
        super.onPause();
        try {
            Method method =  Class.forName("android.webkit.WebView").getMethod("onPause");
            method.invoke(mWebView);
            mWebView.loadUrl("file:///android_asset/nonexistent.html"); //비디오/오디오 강제종료하는 트릭

        } catch(ClassNotFoundException cnfe) {

        } catch(NoSuchMethodException nsme) {

        } catch(InvocationTargetException ite) {

        } catch (IllegalAccessException iae) {

        }
    }
}
