package contentsstudio.kr.membershipapplication.DBinterface;

import java.util.List;

import contentsstudio.kr.membershipapplication.Models.MemberModel;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by hoyoung on 2016-05-02.
 * DB확인을 위한 select
 */
public interface DbSelect {
    @GET("test/lhy/member_select.php")
    Call<List<MemberModel>> SelectServer();

}
