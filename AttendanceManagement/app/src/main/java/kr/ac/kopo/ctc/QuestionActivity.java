package kr.ac.kopo.ctc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import static android.view.View.GONE;

public class QuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        //액션바를 툴바로 대체해서 사용합니다.
        Toolbar toolbar = (Toolbar) findViewById(R.id.question_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("문의하기");

        //툴바에 홈버튼을 활성화시킵니다.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //홈버튼의 이미지를 대체할 수 있는 코드. 사이즈 조절에 유의
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_white_24dp);

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
