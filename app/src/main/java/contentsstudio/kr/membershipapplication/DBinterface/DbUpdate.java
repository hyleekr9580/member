package contentsstudio.kr.membershipapplication.DBinterface;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by hoyoung on 2016-05-09.
 */
public interface DbUpdate {
    @GET("test/lhy/member_update.php")
    Call<Result> UpdateServer(@Query("user_name") String user_name,
                             @Query("user_email") String user_email);

}
