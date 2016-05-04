package contentsstudio.kr.membershipapplication.DBinterface;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by hoyoung on 2016-05-03.
 * login체크를 위한 select where
 */
public interface Dbwhere {
    @GET("test/lhy/member_login.php")
    Call<Result> WhereServer(@Query("user_id") String user_id,
                             @Query("user_pw") String user_pw);


}
