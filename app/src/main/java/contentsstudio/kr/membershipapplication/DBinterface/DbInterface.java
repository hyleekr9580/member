package contentsstudio.kr.membershipapplication.DBinterface;

import java.util.List;

import contentsstudio.kr.membershipapplication.Models.MemberModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by hoyoung on 2016-05-13.
 */
public interface DbInterface {

    //  INSERT
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
            , @Query("user_del") String user_del
            , @Query("user_date") String user_date);

    //  DELETE
    //http://clubcoffee.cafe24.co   m/home/SampleJoa_Test/member_insert.php
    @GET("test/lhy/member_delete.php")
    Call<Result> DeleteServer(@Query("user_id") String user_id,
                              @Query("user_del") String user_del,
                              @Query("user_del_text") String user_del_text,
                              @Query("user_del_date") String user_del_date);

    //  SELECT
    @GET("test/lhy/member_admin.php")
    Call<List<MemberModel>> SelectServer();

    @GET("test/lhy/member_select.php")
    Call<List<MemberModel>> selectServer(@Query("user_id") String userId);

    // UPDATE
    @GET("test/lhy/member_update.php")
    Call<Result> UpdateServer(@Query("user_pw") String pw,
                              @Query("user_name") String user_name,
                              @Query("user_email") String user_email,
                              @Query("user_id") String user_id,
                              @Query("user_update_date") String user_update_date);

    // WHERE
    @GET("test/lhy/member_login.php")
    Call<Result> WhereServer(@Query("user_id") String user_id,
                             @Query("user_pw") String user_pw,
                             @Query("user_del") String user_del);


}
