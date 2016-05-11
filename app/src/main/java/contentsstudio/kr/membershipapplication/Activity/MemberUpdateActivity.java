package contentsstudio.kr.membershipapplication.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import contentsstudio.kr.membershipapplication.DBinterface.DbUpdate;
import contentsstudio.kr.membershipapplication.DBinterface.Result;
import contentsstudio.kr.membershipapplication.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MemberUpdateActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = LoginActivity.class.getSimpleName();

    private EditText mEdtName;
    private EditText mEdtEmail;
    private Button mBtnUpdate;
    private DbUpdate mDbUpdate;
    private TextView mTextId;
    private String PreferencesString;
    private String string_user_id;
    private String string_user_name;
    private String string_user_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_update);


        getPreferences();
        mTextId = (TextView) findViewById(R.id.server_id);
        mTextId.setText("현재 로그인 ID : " + PreferencesString);


        mEdtName = (EditText) findViewById(R.id.update_name);
        mEdtEmail = (EditText) findViewById(R.id.update_email);

        mBtnUpdate = (Button) findViewById(R.id.update_btn);
        mBtnUpdate.setOnClickListener(this);

    }

    // Retrofit
    public void update() {
        string_user_id = PreferencesString;
        string_user_name = mEdtName.getText().toString();
        string_user_email = mEdtEmail.getText().toString();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://suwonsmartapp.iptime.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mDbUpdate = retrofit.create(DbUpdate.class);

        Call<Result> memberModelCall = mDbUpdate.UpdateServer(string_user_name, string_user_email, string_user_id);
        memberModelCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
//                Toast.makeText(MemberUpdateActivity.this, response.body().getResult(), Toast.LENGTH_SHORT).show();
                if (response.body().getResult().equals("ok")) {
                    Toast.makeText(MemberUpdateActivity.this, "수정이 완료 되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();

                } else {
                    Toast.makeText(MemberUpdateActivity.this, "정상적으로 저장이 되지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(MemberUpdateActivity.this, "통신 에러", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_btn:
                if (TextUtils.isEmpty(mEdtName.getText())) {
                    Toast.makeText(MemberUpdateActivity.this, "수정할 이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mEdtEmail.getText())) {

                    Toast.makeText(MemberUpdateActivity.this, "수정할 이메일을 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    update();

                }
        }
    }

    // 값 불러오기
    private void getPreferences() {
        SharedPreferences pref = getSharedPreferences("membershipapplication", MODE_PRIVATE);
        PreferencesString = pref.getString("ID", "");
        Log.e(TAG, "저장된 값은? : " + PreferencesString);

    }

}
