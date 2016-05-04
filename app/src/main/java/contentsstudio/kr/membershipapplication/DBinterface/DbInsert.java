package contentsstudio.kr.membershipapplication.DBinterface;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by hoyoung on 2016-04-27.
 * DB등록을 위한 insert
 */
public interface DbInsert {

    //http://clubcoffee.cafe24.com/home/SampleJoa_Test/member_insert.php
    @GET("test/lhy/member_insert.php")
    Call<Result> InsertServer(@Query("user_id") String user_id
            , @Query("user_pw") String user_pw
            , @Query("user_name") String user_name
            , @Query("user_phone") String user_phone
            , @Query("user_telecom") String user_telecom
            , @Query("user_model") String user_model
            , @Query("user_os") String user_os
            , @Query("user_account") String user_account
            , @Query("user_email") String user_email
            , @Query("user_adid") String user_adid
            , @Query("user_date") String user_date);
}
