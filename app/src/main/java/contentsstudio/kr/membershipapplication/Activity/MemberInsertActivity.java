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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import contentsstudio.kr.membershipapplication.DBinterface.DbInterface;
import contentsstudio.kr.membershipapplication.DBinterface.Result;
import contentsstudio.kr.membershipapplication.R;
import contentsstudio.kr.membershipapplication.Util.AES256Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MemberInsertActivity extends AppCompatActivity implements View.OnClickListener  {

    private String TAG = MemberInsertActivity.class.getSimpleName();

    private EditText mEditName;
    private EditText mEditId;
    private EditText mEditPw01;
    private EditText mEditPw02;
    private EditText mEditEmail;

    private Button mButtonNew;
    private DbInterface mDbInsert;

    private String google_id = "";
    private String mPhone;
    private String mAccount;
    private String mDate;
    private CheckBox mCheckBox01;
    private CheckBox mCheckBox02;
    //    Toast 객체 선언
    private Toast mToast;
    private TextView mTextChk01;
    private TextView mTextChk02;
    private Intent mIntent;
    private AES256Util mAes256;
    private String mEncText;
    private Button mButtonChk;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_data);

        mAes256 = AES256Util.getInstance();


        // onCreate() 에서 Toast.makeText()를 이용하여 Toast 객체 초기화
        mToast = Toast.makeText(this, "null", Toast.LENGTH_SHORT);


        mEditId = (EditText) findViewById(R.id.user_id);
        mEditPw01 = (EditText) findViewById(R.id.user_pw01);
        mEditPw02 = (EditText) findViewById(R.id.user_pw02);
        mEditName = (EditText) findViewById(R.id.user_name);
        mEditEmail = (EditText) findViewById(R.id.user_email);

        mButtonNew = (Button) findViewById(R.id.member_btn);
        mButtonNew.setOnClickListener(this);
        mButtonChk = (Button) findViewById(R.id.user_idchk);
        mButtonChk.setOnClickListener(this);

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
        mDbInsert = retrofit.create(DbInterface.class);

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
        // 암호화할 문자
        String pw = mEditPw01.getText().toString();

        try {
            mEncText = mAes256.AES_Encode(pw);
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
        String del ="N";




        switch (v.getId()) {
            //  이용약관 확인
            case R.id.chk01_text:
                mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.dhcomms.com"));
                startActivity(mIntent);
                break;
            //  개인정보취급방침 확인
            case R.id.chk02_text:
                mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.dhcomms.com"));
                startActivity(mIntent);
                break;
            //  아이디체크
            case R.id.user_idchk:
                Toast.makeText(MemberInsertActivity.this, "아이디 중복 체크", Toast.LENGTH_SHORT).show();
                break;
            //  회원가입 서버 저장
            case R.id.member_btn:
                if (chkUserData()) {

                    //서버에 전송
                    Call<Result> call = mDbInsert.InsertServer(id, mEncText, name, mPhone, mTelecom,
                            Build.MODEL, Build.VERSION.RELEASE, mAccount, email, google_id, del,mDate);

                    call.enqueue(new Callback<Result>() {
                        @Override
                        public void onResponse(Call<Result> call, Response<Result> response) {
//                            Toast.makeText(MemberInsertActivity.this, response.body().getResult(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(MemberInsertActivity.this, "회원 가입이 완료 되었습니다. \n 로그인 하여 주시기 바랍니다. 감사합니다.", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(MemberInsertActivity.this, LoginActivity.class);
                            startActivity(intent);
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
        } else if (!checkEmail(mEditEmail.getText().toString())) {
            mToast.setText("정상적인 EMAIL 형식으로 작성해 주세요");
            mToast.show();
            return false;
        }
        return true;
    }


    //  이메일 형식 체크하기
    private boolean checkEmail(String email) {
        String mail = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(mail);
        Matcher m = p.matcher(email);
        return m.matches();
    }

}


