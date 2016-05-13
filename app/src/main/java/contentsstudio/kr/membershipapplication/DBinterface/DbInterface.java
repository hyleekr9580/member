package contentsstudio.kr.membershipapplication.DBinterface;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by hoyoung on 2016-05-13.
 */
public interface DbInterface {

    //http://clubcoffee.cafe24.com/home/SampleJoa_Test/member_insert.php
    @GET("test/lhy/member_delete.php")
    Call<Result> DeleteServer(@Query("user_id") String user_id
    );

}
