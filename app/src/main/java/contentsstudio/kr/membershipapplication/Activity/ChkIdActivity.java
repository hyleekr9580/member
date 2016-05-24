package contentsstudio.kr.membershipapplication.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import contentsstudio.kr.membershipapplication.DBinterface.DbInterface;
import contentsstudio.kr.membershipapplication.Mail.GMailSender;
import contentsstudio.kr.membershipapplication.Models.MemberModel;
import contentsstudio.kr.membershipapplication.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChkIdActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = ChkPwActivity.class.getSimpleName();

    private ProgressDialog dialog;
    private GMailSender sender;
    private EditText mEdtChkName;
    private TextView mTextChkId;
    private Button mBtnChkId;
    private DbInterface mDbSelect;
    private String string_chk_name;
    private MemberModel mMember;
    private Toast mToast;
    private Button mBtnChkPwEmail;
    private GMailSender mSender;
    private EditText mEdtChkEmail;
    private List<MemberModel> mMemberModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chk_id);

        // onCreate() 에서 Toast.makeText()를 이용하여 Toast 객체 초기화
        mToast = Toast.makeText(this, "null", Toast.LENGTH_SHORT);

        mEdtChkName = (EditText) findViewById(R.id.chkid_name_edt);
        mEdtChkEmail = (EditText) findViewById(R.id.chkid_email);

        mTextChkId = (TextView) findViewById(R.id.chkid_name_text);
        mBtnChkId = (Button) findViewById(R.id.chkid_btn);
        mBtnChkId.setOnClickListener(this);
        mBtnChkPwEmail = (Button) findViewById(R.id.chkid_btn_email);
        mBtnChkPwEmail.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chkid_btn:
                select();
                if (chkUserData() && mMemberModelList != null && mMemberModelList.size() != 0) {
                    mEdtChkEmail.setVisibility(View.VISIBLE);
                    mBtnChkPwEmail.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.chkid_btn_email:

                if (TextUtils.isEmpty(mEdtChkEmail.getText().toString())) {
                    Toast.makeText(ChkIdActivity.this, "이메일을 입력하여 주시기 바랍니다.", Toast.LENGTH_SHORT).show();
                } else if (!checkEmail(mEdtChkEmail.getText().toString())) {
                    Toast.makeText(ChkIdActivity.this, "정상적인 이메일을 입력하여 주시기 바랍니다.", Toast.LENGTH_SHORT).show();
                } else {
                    // SUBSTITUTE HERE
                    mSender = new GMailSender("hyleekr9580@gmail.com", "athdtnjfbarwjubv");
                    timeThread();
                    break;
                }
        }


    }

    //  Retrofit select
    public void select() {
        string_chk_name = mEdtChkName.getText().toString();
        String s = null;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://suwonsmartapp.iptime.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mDbSelect = retrofit.create(DbInterface.class);

        Call<List<MemberModel>> call = mDbSelect.SelectServer(s, string_chk_name);
        call.enqueue(new Callback<List<MemberModel>>() {
            @Override
            public void onResponse(Call<List<MemberModel>> call, Response<List<MemberModel>> response) {
                //  이름이 있는지 없는지 null을 체크 합니다.


                mMemberModelList = response.body();

                if (mMemberModelList != null && mMemberModelList.size() != 0) {
                    mMember = mMemberModelList.get(0);
                    Log.e(TAG, "onResponse: " + response.body().get(0));
                    Log.e(TAG, "onResponse: " + mMember.getUser_id());
                    mTextChkId.setText("고객님의 ID : " + mMember.getUser_id());


                } else {
                    Toast.makeText(ChkIdActivity.this, "찾으시는 정보가 없습니다.", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<List<MemberModel>> call, Throwable t) {
                Toast.makeText(ChkIdActivity.this, "E000 통신 에러가 발생 하였습니다.", Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    private boolean chkUserData() {

        if (TextUtils.isEmpty(mEdtChkName.getText())) {
            mToast.setText("이름을 입력하세요.");
            mToast.show();
            return false;
        }
        return true;

    }

    public void timeThread() {

        dialog = new ProgressDialog(this);
        dialog = new ProgressDialog(this);
        dialog.setTitle("Wait...");
        dialog.setMessage("의견을 보내는 중입니다.");
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.show();
        new Thread(new Runnable() {

            public void run() {
                // TODO Auto-generated method stub
                try {
                    mSender.sendMail("고객님의 ID 입니다. ", // subject.getText().toString(), 메일제목
                            mTextChkId.getText().toString(), // body.getText().toString(), 발송내용
                            "hyleekr9580@gmail.com", // from.getText().toString(), 보내는사람
                            mEdtChkEmail.getText().toString() //받는사람
//                            "hyleekr@nate.com" // to.getText().toString()
                    );
                    sleep(3000);
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                    Toast.makeText(ChkIdActivity.this, "신청 실패", Toast.LENGTH_SHORT).show();


                }
                dialog.dismiss();

            }

            private void sleep(int i) {
                // TODO Auto-generated method stub

            }

        }).start();
        Toast.makeText(ChkIdActivity.this, "이메일 발송이 완료 되었습니다.", Toast.LENGTH_SHORT).show();

    }

    //  이메일 형식 체크하기
    private boolean checkEmail(String email) {
        String mail = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(mail);
        Matcher m = p.matcher(email);
        return m.matches();
    }


}
