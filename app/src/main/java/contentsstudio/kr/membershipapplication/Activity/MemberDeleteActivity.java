package contentsstudio.kr.membershipapplication.Activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import contentsstudio.kr.membershipapplication.DBinterface.DbInterface;
import contentsstudio.kr.membershipapplication.DBinterface.DbSelect;
import contentsstudio.kr.membershipapplication.DBinterface.Result;
import contentsstudio.kr.membershipapplication.Models.MemberModel;
import contentsstudio.kr.membershipapplication.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MemberDeleteActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = LoginActivity.class.getSimpleName();

    private TextView mDelId;
    private TextView mDelName;
    private TextView mDelEmail;
    private EditText mDelEdt;
    private Button mDelBtn;
    private String string_user_id;
    private String PreferencesString;
    private DbInterface mDbDelete;
    private DbSelect mDbSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_delete);

        getPreferences();
        select();

        mDelId = (TextView) findViewById(R.id.del_id);
        mDelName = (TextView) findViewById(R.id.del_name);
        mDelEmail = (TextView) findViewById(R.id.del_email);
        mDelEdt = (EditText) findViewById(R.id.del_edt);
        mDelBtn = (Button) findViewById(R.id.del_btn);
        mDelBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        setOutAlertMsg();

    }

    // Retrofit delete
    public void delete() {
        string_user_id = PreferencesString;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://suwonsmartapp.iptime.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mDbDelete = retrofit.create(DbInterface.class);

        Call<Result> memberModelCall = mDbDelete.DeleteServer(string_user_id);
        memberModelCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
//                Toast.makeText(MemberUpdateActivity.this, response.body().getResult(), Toast.LENGTH_SHORT).show();
                if (response.body().getResult().equals("ok")) {
                    Toast.makeText(MemberDeleteActivity.this, "탈퇴가 완료 되었습니다.", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MemberDeleteActivity.this, "정상적으로 탈퇴가 되지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(MemberDeleteActivity.this, "통신 에러", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //  Retrofit select
    public void select() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://suwonsmartapp.iptime.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mDbSelect = retrofit.create(DbSelect.class);

        Call<List<MemberModel>> call = mDbSelect.selectServer(PreferencesString);
        call.enqueue(new Callback<List<MemberModel>>() {
            @Override
            public void onResponse(Call<List<MemberModel>> call, Response<List<MemberModel>> response) {
                MemberModel member = response.body().get(0);
                mDelId.setText("ID : " + member.getUser_id());
                mDelName.setText("이름 : " + member.getUser_name());
                mDelEmail.setText("이메일 : " + member.getUser_email());
            }

            @Override
            public void onFailure(Call<List<MemberModel>> call, Throwable t) {
                Toast.makeText(MemberDeleteActivity.this, "fail", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }


    // 저장 값 불러오기
    private void getPreferences() {
        SharedPreferences pref = getSharedPreferences("membershipapplication", MODE_PRIVATE);
        PreferencesString = pref.getString("ID", "");
        Log.e(TAG, "저장된 값은? : " + PreferencesString);

    }


    // 종료 알림참
    private void setOutAlertMsg() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);     // 여기서 this는 Activity의 this

        // 여기서 부터는 알림창의 속성 설정
        builder.setMessage("정말로 탈퇴를 하시겠습니까?")        // 메세지 설정
                .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    // 확인 버튼 클릭시 설정
                    public void onClick(DialogInterface dialog, int whichButton) {
                        delete();
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

}
