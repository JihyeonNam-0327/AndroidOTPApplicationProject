package kr.ac.kopo.ctc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //액션바를 툴바로 대체해서 사용합니다.
        Toolbar toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("설정");

        //툴바에 홈버튼을 활성화시킵니다. (뒤로가기 버튼)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //홈버튼의 이미지를 대체할 수 있는 코드. 사이즈 조절에 유의
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_white_24dp);
        //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요
        //actionBar.setHomeAsUpIndicator(R.drawable.button_back);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
