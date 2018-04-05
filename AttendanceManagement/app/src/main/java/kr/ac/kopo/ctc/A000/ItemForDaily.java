package kr.ac.kopo.ctc.A000;

/**
 * Created by jihyeon on 2018-03-30.
 */

public class ItemForDaily {

    private String time_in; // 이름
    private String time_out; // 비밀번호
    private String status; //학과

    public void setTimeIn(String time_in){ // 종가
        this.time_in = time_in;
    }
    public void setTimeOut(String time_out){ // 전일비
        this.time_out = time_out;
    }
    public void setStatus(String status) { this.status = status; }

    public String getTimeIn() {
        return time_in;
    }
    public String getTimeOut() {
        return time_out;
    }
    public String getStatus() { return status; }
}
