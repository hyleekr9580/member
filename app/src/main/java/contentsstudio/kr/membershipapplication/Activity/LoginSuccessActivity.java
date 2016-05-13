package contentsstudio.kr.membershipapplication.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import contentsstudio.kr.membershipapplication.R;

public class LoginSuccessActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnLogOut;
    private Button mBtnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mBtnLogOut = (Button) findViewById(R.id.logout);
        mBtnLogOut.setOnClickListener(this);
        mBtnUpdate = (Button) findViewById(R.id.update);
        mBtnUpdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout:
                // 로그아웃 시키기
                SharedPreferences preferences = getSharedPreferences("membershipapplication", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(LoginSuccessActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.update:
                Intent intent = new Intent(LoginSuccessActivity.this, MemberUpdateActivity.class);
                startActivity(intent);
                break;
        }
    }

}
