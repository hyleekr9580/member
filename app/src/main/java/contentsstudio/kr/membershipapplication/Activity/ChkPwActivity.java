package contentsstudio.kr.membershipapplication.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import contentsstudio.kr.membershipapplication.DBinterface.DbInterface;
import contentsstudio.kr.membershipapplication.Models.MemberModel;
import contentsstudio.kr.membershipapplication.R;
import contentsstudio.kr.membershipapplication.Util.AES256Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChkPwActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = ChkPwActivity.class.getSimpleName();


    private TextView mTextChkPw;
    private Button mBtnChkPw;
    private DbInterface mDbSelect;
    private EditText mEdtChkId;
    private String string_chk_id;
    private AES256Util mA256;
    private String mStringA256;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chk_pw);

        mA256 = AES256Util.getInstance();


        mEdtChkId = (EditText) findViewById(R.id.chkpw_id_edt);
        mTextChkPw = (TextView) findViewById(R.id.chkpw_name_text);
        mBtnChkPw = (Button) findViewById(R.id.chkpw_btn);
        mBtnChkPw.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        select();
    }

    //  Retrofit select
    public void select() {
        string_chk_id = mEdtChkId.getText().toString();
        String s = null;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://suwonsmartapp.iptime.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mDbSelect = retrofit.create(DbInterface.class);

        Call<List<MemberModel>> call = mDbSelect.SelectServer(string_chk_id,s);
        call.enqueue(new Callback<List<MemberModel>>() {
            @Override
            public void onResponse(Call<List<MemberModel>> call, Response<List<MemberModel>> response) {
                MemberModel member = response.body().get(0);

                try {
                    mStringA256 = mA256.AES_Decode(member.getUser_pw());
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

                mTextChkPw.setText("비밀번호 : " + mStringA256);


            }

            @Override
            public void onFailure(Call<List<MemberModel>> call, Throwable t) {
                Toast.makeText(ChkPwActivity.this, "fail", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

}
