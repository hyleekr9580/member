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

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import contentsstudio.kr.membershipapplication.DBinterface.DbInterface;
import contentsstudio.kr.membershipapplication.DBinterface.Result;
import contentsstudio.kr.membershipapplication.Models.MemberModel;
import contentsstudio.kr.membershipapplication.R;
import contentsstudio.kr.membershipapplication.Util.AES256Util;
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
    private DbInterface mDbUpdate;
    private TextView mTextId;
    private String PreferencesString;
    private String string_user_id;
    private String string_user_name;
    private String string_user_email;
    private DbInterface mDbSelect;
    private String mDate;
    private EditText mEdtPw;
    private String string_user_pw;
    private AES256Util mAes256;
    private String mEncText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_update);

        mAes256 = AES256Util.getInstance();


        getPreferences();
        select();
        mTextId = (TextView) findViewById(R.id.server_id);
        mTextId.setText("현재 로그인 ID : " + PreferencesString);


        mEdtPw = (EditText) findViewById(R.id.update_pw);
        mEdtName = (EditText) findViewById(R.id.update_name);
        mEdtEmail = (EditText) findViewById(R.id.update_email);

        mBtnUpdate = (Button) findViewById(R.id.update_btn);
        mBtnUpdate.setOnClickListener(this);


    }

    //  Retrofit select
    public void select() {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://suwonsmartapp.iptime.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mDbSelect = retrofit.create(DbInterface.class);

        Call<List<MemberModel>> call = mDbSelect.selectServer(PreferencesString);
        call.enqueue(new Callback<List<MemberModel>>() {
            @Override
            public void onResponse(Call<List<MemberModel>> call, Response<List<MemberModel>> response) {


                MemberModel member = response.body().get(0);
                mEdtName.setText(member.getUser_name());
                mEdtEmail.setText(member.getUser_email());


            }

            @Override
            public void onFailure(Call<List<MemberModel>> call, Throwable t) {
                Toast.makeText(MemberUpdateActivity.this, "fail", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    // Retrofit update
    public void update() {
        //  update 날짜 확인 (최종 업데이트 일자)
        long today = System.currentTimeMillis(); // long 형의 현재시간
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        mDate = simpleDateFormat.format(today);

        string_user_id = PreferencesString;
        string_user_pw = mEdtPw.getText().toString();
        string_user_name = mEdtName.getText().toString();
        string_user_email = mEdtEmail.getText().toString();

        try {
            mEncText = mAes256.AES_Encode(string_user_pw);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://suwonsmartapp.iptime.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mDbUpdate = retrofit.create(DbInterface.class);

        Call<Result> memberModelCall = mDbUpdate.UpdateServer(mEncText, string_user_name, string_user_email, string_user_id, mDate);
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


                if (TextUtils.isEmpty(mEdtPw.getText().toString())) {
                    Toast.makeText(MemberUpdateActivity.this, "변경 비밀번호 입력", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mEdtName.getText())) {
                    Toast.makeText(MemberUpdateActivity.this, "수정할 이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mEdtEmail.getText())) {
                    Toast.makeText(MemberUpdateActivity.this, "수정할 이메일을 입력하세요.", Toast.LENGTH_SHORT).show();
                } else if (!checkEmail(mEdtEmail.getText().toString())) {
                    Toast.makeText(MemberUpdateActivity.this, "정상적인 이메일 형식으로 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    update();
                }
        }
    }

    // 저장 값 불러오기
    private void getPreferences() {
        SharedPreferences pref = getSharedPreferences("membershipapplication", MODE_PRIVATE);
        PreferencesString = pref.getString("ID", "");
        Log.e(TAG, "저장된 값은? : " + PreferencesString);

    }

    //  이메일 형식 체크하기
    private boolean checkEmail(String email) {
        String mail = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(mail);
        Matcher m = p.matcher(email);
        return m.matches();
    }
}
