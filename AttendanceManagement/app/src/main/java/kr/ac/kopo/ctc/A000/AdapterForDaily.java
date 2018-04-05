package kr.ac.kopo.ctc.A000;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import kr.ac.kopo.ctc.R;

/**
 * Created by jihyeon on 2018-03-30.
 */

public class AdapterForDaily extends BaseAdapter{

    private String status_text;
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ItemForDaily> listViewItemList = new ArrayList<>();

    // ListViewAdapter의 생성자
    public AdapterForDaily() {
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listitem_daily, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득

        TextView time_in = (TextView) convertView.findViewById(R.id.textView02); // 출석시간
        TextView time_out = (TextView) convertView.findViewById(R.id.textView03); // 퇴실시간
        TextView status = (TextView) convertView.findViewById(R.id.textView04); // 상태

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ItemForDaily listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        time_in.setText(listViewItem.getTimeIn());
        time_out.setText(listViewItem.getTimeOut());
        if(listViewItem.getStatus().equals("9")){
            status_text = "체크 전";
        }else if(listViewItem.getStatus().equals("0")){
            status_text = "입실 체크 완료";
        }else if(listViewItem.getStatus().equals("3")){
            status_text = "퇴실 체크 완료";
        }
        status.setText(status_text);

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수 (원하는대로 작성 가능)
    public void addItem(String time_in, String time_out,String status) {
        ItemForDaily item = new ItemForDaily();

        item.setTimeIn(time_in);
        item.setTimeOut(time_out);
        item.setStatus(status);

        listViewItemList.add(0, item);
    }

}
