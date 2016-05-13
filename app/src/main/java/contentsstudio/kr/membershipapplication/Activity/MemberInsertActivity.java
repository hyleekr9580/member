package contentsstudio.kr.membershipapplication.Activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.IOException;
import java.text.SimpleDateFormat;

import contentsstudio.kr.membershipapplication.DBinterface.DbInsert;
import contentsstudio.kr.membershipapplication.DBinterface.Result;
import contentsstudio.kr.membershipapplication.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MemberInsertActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = MemberInsertActivity.class.getSimpleName();

    private EditText mEditName;
    private EditText mEditId;
    private EditText mEditPw01;
    private EditText mEditPw02;
    private EditText mEditEmail;

    private Button mButtonNew;
    private DbInsert mDbInsert;

    private String google_id = "";
    private String mPhone;
    private String mAccount;
    private String mDate;
    private CheckBox mCheckBox01;
    private CheckBox mCheckBox02;
    //    Toast 객체 선언
    private Toast mToast;
    private Button mButtonChk;
    private TextView mTextChk01;
    private TextView mTextChk02;
    private WebView mWebView;
    private Intent mIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_data);

        // onCreate() 에서 Toast.makeText()를 이용하여 Toast 객체 초기화
        mToast = Toast.makeText(this, "null", Toast.LENGTH_SHORT);


        mEditId = (EditText) findViewById(R.id.user_id);
        mEditPw01 = (EditText) findViewById(R.id.user_pw01);
        mEditPw02 = (EditText) findViewById(R.id.user_pw02);
        mEditName = (EditText) findViewById(R.id.user_name);
        mEditEmail = (EditText) findViewById(R.id.user_email);

        mButtonNew = (Button) findViewById(R.id.member_btn);
        mButtonNew.setOnClickListener(this);
//        mButtonChk = (Button) findViewById(R.id.user_idchk);
//        mButtonChk.setOnClickListener(this);

        mTextChk01 = (TextView) findViewById(R.id.chk01_text);
        mTextChk01.setOnClickListener(this);
        mTextChk02 = (TextView) findViewById(R.id.chk02_text);
        mTextChk02.setOnClickListener(this);

        mCheckBox01 = (CheckBox) findViewById(R.id.chk01);
        mCheckBox02 = (CheckBox) findViewById(R.id.chk02);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://suwonsmartapp.iptime.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mDbInsert = retrofit.create(DbInsert.class);

        // 구글 ADID를 가지고 위한 AdidAsyncTask
        new AdidAsyncTask().execute();

    }


    @Override
    public void onClick(View v) {

        StringBuilder stringBuilder = new StringBuilder("");

        //  구글계정확인
        Account[] accounts = AccountManager.get(getApplicationContext()).getAccounts();
        Account account = null;
        for (int i = 0; i < accounts.length; i++) {
            account = accounts[i];
            if (account.type.equals("com.google")) {
                mAccount = account.name;
            }
        }

        //  전화번호 확인
        TelephonyManager telephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = telephony.getLine1Number();
        if (phoneNumber != null) {
            //KT통신사의 특이사항 +82 표시가 됩니다.
            mPhone = phoneNumber.replace("+82", "0");
        }

        //  통신사 확인
        String mTelecom = telephony.getNetworkOperatorName();

        //  날짜 확인
        long today = System.currentTimeMillis(); // long 형의 현재시간
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        mDate = simpleDateFormat.format(today);

        //  서버전송을 위한 String 변환
        String id = mEditId.getText().toString();
        String name = mEditName.getText().toString();
        String email = mEditEmail.getText().toString();
        String pw = mEditPw01.getText().toString();

        switch (v.getId()) {
            //  이용약관 확인
            case R.id.chk01_text:
                mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.dhcomms.com/applications/ca/cpa/google/main_agreepage01.html"));
                startActivity(mIntent);
                break;
            //  개인정보취급방침 확인
            case R.id.chk02_text:
                mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.dhcomms.com/applications/ca/cpa/google/main_agreepage02.html"));
                startActivity(mIntent);
                break;
            //  아이디체크
//            case R.id.user_idchk:
//
//                break;
            //  회원가입 서버 저장
            case R.id.member_btn:
                if (chkUserData()) {

                    //서버에 전송
                    Call<Result> call = mDbInsert.InsertServer(id, pw, name, mPhone, mTelecom,
                            Build.MODEL, Build.VERSION.RELEASE, mAccount, email, google_id, mDate);

                    call.enqueue(new Callback<Result>() {
                        @Override
                        public void onResponse(Call<Result> call, Response<Result> response) {
//                            Toast.makeText(MemberInsertActivity.this, response.body().getResult(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(MemberInsertActivity.this, "정상적으로 저장이 되었습니다.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<Result> call, Throwable t) {
                            Toast.makeText(MemberInsertActivity.this, "에러발생", Toast.LENGTH_SHORT).show();

                        }
                    });

                }

                break;
        }


    }

    private class AdidAsyncTask extends AsyncTask<String, Integer, String> {

        @WorkerThread
        @Override
        protected String doInBackground(String... strings) {
            AdvertisingIdClient.Info adInfo = null;
            try {
                adInfo = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            }
            return adInfo.getId();
        }


        @UiThread
        @Override
        protected void onPostExecute(String adid) {
            google_id = adid;


        }
    }

    private boolean chkUserData() {

        if (TextUtils.isEmpty(mEditId.getText())) {
            mToast.setText("ID를 입력하세요.");
            mToast.show();
            return false;
        } else if (TextUtils.isEmpty(mEditPw01.getText())) {
            mToast.setText("비밀번호를 입력하세요.");
            mToast.show();
            return false;
        } else if (TextUtils.isEmpty(mEditPw02.getText())) {
            mToast.setText("비밀번호를 입력하세요.");
            mToast.show();
            return false;
        } else if (TextUtils.isEmpty(mEditName.getText())) {
            mToast.setText("이름을 입력하세요.");
            mToast.show();
            return false;
        } else if (TextUtils.isEmpty(mEditEmail.getText())) {
            mToast.setText("이메일을 입력하세요.");
            mToast.show();
            return false;
        } else if (!mCheckBox01.isChecked()) {
            mToast.setText("이용약관에 동의 하셔야 합니다.");
            mToast.show();
            return false;
        } else if (!mCheckBox02.isChecked()) {
            mToast.setText("개인정보 취급방침에 동의 하셔야 합니다.");
            mToast.show();
            return false;
        } else if (!(mEditPw01.getText().toString().equals(mEditPw02.getText().toString()))) {
            mToast.setText("입력하신 비빌번호가 다릅니다.\n비밀번호를 확인하세요.");
            mToast.show();
            return false;
        }
        return true;
    }


}


