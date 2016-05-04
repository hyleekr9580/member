package contentsstudio.kr.membershipapplication.DBinterface;

import contentsstudio.kr.membershipapplication.Models.MemberModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by hoyoung on 2016-05-03.
 * login체크를 위한 select where
 */
public interface Dbwhere {
    @GET("test/lhy/member_login.php")
    Call<MemberModel> WhereServer(@Path("user_id") String user_id);


}
