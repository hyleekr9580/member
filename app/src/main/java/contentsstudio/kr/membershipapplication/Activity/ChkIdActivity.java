package contentsstudio.kr.membershipapplication.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import contentsstudio.kr.membershipapplication.DBinterface.DbInterface;
import contentsstudio.kr.membershipapplication.Models.MemberModel;
import contentsstudio.kr.membershipapplication.R;
import contentsstudio.kr.membershipapplication.Util.AES256Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChkIdActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = ChkPwActivity.class.getSimpleName();

    private AES256Util mA256;
    private EditText mEdtChkName;
    private TextView mTextChkId;
    private Button mBtnChkPw;
    private String string_chk_id;
    private DbInterface mDbSelect;
    private String string_chk_name;
    private String mStringA256;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chk_id);


        mEdtChkName = (EditText) findViewById(R.id.chkid_name_edt);
        mTextChkId = (TextView) findViewById(R.id.chkid_name_text);
        mBtnChkPw = (Button) findViewById(R.id.chkid_btn);
        mBtnChkPw.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        select();
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


                MemberModel member = response.body().get(0);
                mTextChkId.setText("고객님 ID : " + member.getUser_id());


            }

            @Override
            public void onFailure(Call<List<MemberModel>> call, Throwable t) {
                Toast.makeText(ChkIdActivity.this, "fail", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }
}
