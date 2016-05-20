package contentsstudio.kr.membershipapplication.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import contentsstudio.kr.membershipapplication.DBinterface.DbInterface;
import contentsstudio.kr.membershipapplication.Models.MemberModel;
import contentsstudio.kr.membershipapplication.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminActivity extends AppCompatActivity implements Callback<List<MemberModel>> {

    private ListView mListView;
    private AdminAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mListView = (ListView) findViewById(R.id.list_admin);

        mAdapter = new AdminAdapter(null);
        mListView.setAdapter(mAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://suwonsmartapp.iptime.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final DbInterface service = retrofit.create(DbInterface.class);
        service.AdminSelect().enqueue(this);
    }

    @Override
    public void onResponse(Call<List<MemberModel>> call, Response<List<MemberModel>> response) {
        if (response.body() != null) {
            mAdapter.mData = response.body();
            mAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onFailure(Call<List<MemberModel>> call, Throwable t) {

    }

    private static class AdminAdapter extends BaseAdapter {
        private List<MemberModel> mData;
        public AdminAdapter(List<MemberModel> data) {
            mData = data;
        }

        @Override
        public int getCount() {
            if (mData == null) {
                return 0;
            }
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin, parent, false);
                holder = new ViewHolder();
                holder.user_id = (TextView) convertView.findViewById(R.id.user_id);
                holder.user_pw = (TextView) convertView.findViewById(R.id.user_pw);
                holder.user_name = (TextView) convertView.findViewById(R.id.user_name);
                holder.user_phone = (TextView) convertView.findViewById(R.id.user_phone);
                holder.user_telecom = (TextView) convertView.findViewById(R.id.user_telecom);
                holder.user_model = (TextView) convertView.findViewById(R.id.user_model);
                holder.user_os = (TextView) convertView.findViewById(R.id.user_os);
                holder.user_account = (TextView) convertView.findViewById(R.id.user_account);
                holder.user_email = (TextView) convertView.findViewById(R.id.user_email);
                holder.user_adid = (TextView) convertView.findViewById(R.id.user_adid);
                holder.user_date = (TextView) convertView.findViewById(R.id.user_date);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            MemberModel memberModel = (MemberModel) getItem(position);

            holder.user_id.setText("아이디 : " + memberModel.getUser_id());
            holder.user_pw.setText("비밀번호 : " + memberModel.getUser_pw());
            holder.user_name.setText("이름 : " + memberModel.getUser_name());
            holder.user_phone.setText("통신사 : " + memberModel.getUser_phone());
            holder.user_telecom.setText("전화번호 : " + memberModel.getUser_telecom());
            holder.user_model.setText("모델명 : " + memberModel.getUser_model());
            holder.user_os.setText("OS : " + memberModel.getUser_os());
            holder.user_account.setText("구글계정 : " + memberModel.getUser_account());
            holder.user_email.setText("이메일 : " + memberModel.getUser_email());
            holder.user_adid.setText("ADID : " + memberModel.getUser_adid());
            holder.user_date.setText("가입일 : " + memberModel.getUser_date());

            return convertView;
        }

        static class ViewHolder {
            TextView user_id;
            TextView user_pw;
            TextView user_name;
            TextView user_phone;
            TextView user_telecom;
            TextView user_model;
            TextView user_os;
            TextView user_account;
            TextView user_email;
            TextView user_adid;
            TextView user_date;
        }
    }
}