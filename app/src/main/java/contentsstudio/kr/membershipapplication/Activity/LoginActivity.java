package contentsstudio.kr.membershipapplication.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import contentsstudio.kr.membershipapplication.BroaddCast.BroadcastActivity;
import contentsstudio.kr.membershipapplication.DBinterface.DbWhere;
import contentsstudio.kr.membershipapplication.DBinterface.Result;
import contentsstudio.kr.membershipapplication.R;
import contentsstudio.kr.membershipapplication.Util.AES256Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = LoginActivity.class.getSimpleName();

    private Button mLoginButton;
    private Button mNewButton;
    private EditText mIdEditText;
    private EditText mPwEditText;
    private Button mAdmin;
    private DbWhere mDbWhere;
    private String string_user_id;
    private String string_user_pw;
    private String PreferencesString;
    private String string_user_del;
    private AES256Util mAes256;
    private String mEecText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        String key = "qwjejwqklejqwlkjdasjkhdio3u1e912eq0df0ascjqw30d30ass";       // key는 16자 이상
        try {
            mAes256 = new AES256Util(key);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        mIdEditText = (EditText) findViewById(R.id.login_id_edt);
        mPwEditText = (EditText) findViewById(R.id.login_pw_edt);


        mLoginButton = (Button) findViewById(R.id.login_btn);
        mLoginButton.setOnClickListener(this);
        mNewButton = (Button) findViewById(R.id.login_new);
        mNewButton.setOnClickListener(this);
        mAdmin = (Button) findViewById(R.id.admin);
        mAdmin.setOnClickListener(this);


    }

    // Retrofit
    public void login() {
        string_user_id = mIdEditText.getText().toString();
        //암호화전 문자
        string_user_pw = mPwEditText.getText().toString();
        // 암호화 풀기 (복호화)
        try {
            mEecText = mAes256.aesDecode(string_user_pw);
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
        mDbWhere = retrofit.create(DbWhere.class);

        Call<Result> memberModelCall = mDbWhere.WhereServer(string_user_id, mEecText, string_user_del);
        memberModelCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
//                Toast.makeText(LoginActivity.this, response.body().getResult(), Toast.LENGTH_SHORT).show();

                if (response.body().getResult().equals("ok")) {

                    savePreferences();
                    Log.e(TAG, "savePreferences: 로그인 확인");

                    getPreferences();
                    Log.e(TAG, "getPreferences: ID값 확인");


                    Toast.makeText(LoginActivity.this, "로그인되었습니다.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, BroadcastActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "ID/PW가 일치 하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "통신 에러가 발생 하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //  Click
    @Override
    public void onClick(View v) {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        switch (v.getId()) {
            case R.id.login_btn:
                //  유심체크를 합니다.
                if (telephonyManager.getSimState() == TelephonyManager.SIM_STATE_ABSENT) {
                    // 유심이 없는 경우
                    Toast.makeText(LoginActivity.this, "USIM을 확인 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    if (TextUtils.isEmpty(mIdEditText.getText())) {
                        setAlertMsg("ID를 입력하세요.");
                    } else if (TextUtils.isEmpty(mPwEditText.getText())) {
                        setAlertMsg("PW를 입력하세요.");
                    } else {
                        login();
                    }
                }
                break;

            case R.id.login_new:
                //  유심체크를 합니다.
                if (telephonyManager.getSimState() == TelephonyManager.SIM_STATE_ABSENT) {
                    // 유심이 없는 경우
                    Toast.makeText(LoginActivity.this, "USIM을 확인 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    // 유심이 존재하는 경우
                    Intent intent = new Intent(LoginActivity.this, MemberInsertActivity.class);
                    startActivity(intent);
                }
                break;

            case R.id.admin:
                Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                startActivity(intent);

                break;

        }
    }


    // 단순 알림창
    private void setAlertMsg(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);     // 여기서 this는 Activity의 this

        // 여기서 부터는 알림창의 속성 설정
        builder.setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                .setMessage(message)
                .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                    // 취소 버튼 클릭시 설정
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
        builder.show();    // 알림창 띄우기
    }

    // 종료 알림참
    private void setOutAlertMsg() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);     // 여기서 this는 Activity의 this

        // 여기서 부터는 알림창의 속성 설정
        builder.setMessage("앱을 종료 하시 겠습니까?")        // 메세지 설정
                .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    // 확인 버튼 클릭시 설정
                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    // 취소 버튼 클릭시 설정
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });

        builder.show();    // 알림창 띄우기
    }

    // BACK KEY
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            setOutAlertMsg();
        } else if (keycode == KeyEvent.KEYCODE_MENU) {
            openOptionsMenu();
        }
        return true;
    }


    //  값 저장하기
    private void savePreferences() {
        SharedPreferences pref = getSharedPreferences("membershipapplication", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("ID", string_user_id);
        editor.commit();
    }

    // 값 불러오기
    private void getPreferences() {
        SharedPreferences pref = getSharedPreferences("membershipapplication", MODE_PRIVATE);
        PreferencesString = pref.getString("ID", String.valueOf(MODE_PRIVATE));
        Log.e(TAG, "getPreferences: " + string_user_id);
    }

}
