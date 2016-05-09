package contentsstudio.kr.membershipapplication.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    private EditText mEdtName;
    private EditText mEdtEmail;
    private Button mBtnUpdate;
    private DbUpdate mDbUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_update);

        mEdtName = (EditText) findViewById(R.id.update_name);
        mEdtEmail = (EditText) findViewById(R.id.update_email);

        mBtnUpdate = (Button) findViewById(R.id.update_btn);
        mBtnUpdate.setOnClickListener(this);

    }

    // Retrofit
    public void update() {
        String string_user_name = mEdtName.getText().toString();
        String string_user_email = mEdtEmail.getText().toString();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://suwonsmartapp.iptime.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mDbUpdate = retrofit.create(DbUpdate.class);

        Call<Result> memberModelCall = mDbUpdate.UpdateServer(string_user_name, string_user_email);
        memberModelCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Toast.makeText(MemberUpdateActivity.this, response.body().getResult(), Toast.LENGTH_SHORT).show();

                if (response.body().getResult().equals("수정이 완료 되었습니다.")) {
//                    Toast.makeText(MemberUpdateActivity.this, "로그인되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(MemberUpdateActivity.this, "ID/PW가 일치 하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(MemberUpdateActivity.this, "통신 에러", Toast.LENGTH_SHORT).show();
            }
        });

    }



    @Override
    public void onClick(View v) {
        update();
        Toast.makeText(MemberUpdateActivity.this, "수정이 되었습니다.", Toast.LENGTH_SHORT).show();
        finish();
    }
}
