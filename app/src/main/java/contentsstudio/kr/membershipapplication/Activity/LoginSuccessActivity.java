package contentsstudio.kr.membershipapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import contentsstudio.kr.membershipapplication.R;

public class LoginSuccessActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mBtnLogOut = (Button) findViewById(R.id.logout);
        mBtnLogOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout:
                finish();
                break;
            case R.id.update:
                Intent intent = new Intent(LoginSuccessActivity.this, MemberUpdateActivity.class);
                startActivity(intent);
                break;
        }
    }
}
