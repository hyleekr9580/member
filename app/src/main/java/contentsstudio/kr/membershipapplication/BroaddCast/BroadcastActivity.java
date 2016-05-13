package contentsstudio.kr.membershipapplication.BroaddCast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import contentsstudio.kr.membershipapplication.Activity.MemberDeleteActivity;
import contentsstudio.kr.membershipapplication.Activity.MemberUpdateActivity;
import contentsstudio.kr.membershipapplication.R;

public class BroadcastActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String ACTION_MY = "contentsstudio.kr.membershipapplication.ACTION_MY";
    private EditText mEdtMsg;
    private Button mBtnSend;
    private MyReceiver mMyReceiver;
    private TextView mTextMsg;
    private String PreferencesString;
    private Button mBtnOut;
    private Button mBtnChange;
    private Button mBtnDel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);

        mEdtMsg = (EditText) findViewById(R.id.msg_edt);

        mBtnSend = (Button) findViewById(R.id.send_btn);
        mBtnSend.setOnClickListener(this);

        mBtnOut = (Button) findViewById(R.id.out_btn);
        mBtnOut.setOnClickListener(this);
        mBtnChange = (Button) findViewById(R.id.change_btn);
        mBtnChange.setOnClickListener(this);
        mBtnDel = (Button) findViewById(R.id.del_btn);
        mBtnDel.setOnClickListener(this);

        mTextMsg = (TextView) findViewById(R.id.view_text);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 리시버 등록
        mMyReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_MY);
        registerReceiver(mMyReceiver, intentFilter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        // 리시버 해제
        unregisterReceiver(mMyReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_btn:
                // 대화내용 저장
                savePreferences();

                if (!TextUtils.isEmpty(mEdtMsg.getText())) {

                    Intent kakao = new Intent(Intent.ACTION_SEND);
                    kakao.setPackage("com.kakao.talk");
                    kakao.setType("text/plain");
                    kakao.putExtra(Intent.EXTRA_TEXT, mEdtMsg.getText().toString());
                    if (kakao.resolveActivity(getPackageManager()) != null) {
                        startActivity(kakao);
                    } else {
                        Toast.makeText(BroadcastActivity.this, "카카오톡이 설치 되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.squidtooth.gifplayer&hl=ko"));
                        startActivity(intent);
                    }
                    // 대화내용 보이기
                } else {
                    Toast.makeText(BroadcastActivity.this, "메세지 입력 하세요.", Toast.LENGTH_SHORT).show();
                }
                
                getPreferences();
                break;

            case R.id.out_btn:
                SharedPreferences preferences = getSharedPreferences("membershipapplication", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(BroadcastActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                finish();

                break;
            case R.id.change_btn:
                Intent intent = new Intent(BroadcastActivity.this, MemberUpdateActivity.class);
                startActivity(intent);
                break;
            case R.id.del_btn:
                Intent intent1 = new Intent(BroadcastActivity.this, MemberDeleteActivity.class);
                startActivity(intent1);
                break;
        }
    }

    // 값저장하기
    private void savePreferences() {
        SharedPreferences pref = getSharedPreferences("BroadcastReceiver", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("MSG", mEdtMsg.getText().toString());
        editor.commit();
    }

    // 값 불러오기
    private void getPreferences() {
        SharedPreferences pref = getSharedPreferences("BroadcastReceiver", MODE_PRIVATE);
        PreferencesString = pref.getString("MSG", "");
        mTextMsg.setText("전송된 메세지 : " + PreferencesString);
    }

    private static class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
        }
    }
}
