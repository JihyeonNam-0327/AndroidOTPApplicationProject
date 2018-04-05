package kr.ac.kopo.ctc;

/**
 * Created by jihyeon on 2018-03-29.
 */

import java.util.List;
import org.apache.http.client.HttpClient;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

public class SessionControl {

    static public DefaultHttpClient httpclient = null;
    static public List<Cookie> cookies;
    static public String _id;
    static public String pass;
    static public String name;

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        SessionControl.name = name;
    }

    public static String get_id() {
        return _id;
    }
    public static void set_id(String _id) {
        SessionControl._id = _id;
    }


    public static String getPass() {
        return pass;
    }
    public static void setPass(String pass) {
        SessionControl.pass = pass;
    }


    public static HttpClient getHttpclient() {
        if( httpclient == null) {
            SessionControl.setHttpclient(new DefaultHttpClient());
        }
        return httpclient;
    }

    public static void setHttpclient(DefaultHttpClient httpclient) {
        SessionControl.httpclient = httpclient;
    }

    public static void destroyHttpClient(){
        httpclient.getCookieStore().clear(); // important
    }

    public static void setCookies(List<Cookie> list){
        cookies = list;
    }


}