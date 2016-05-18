package contentsstudio.kr.membershipapplication.Models;

/**
 * Created by hoyoung on 2016-05-02.
 * Model class
 */
public class MemberModel {

    private String user_id;
    private String user_pw;
    private String user_name;
    private String user_phone;
    private String user_telecom;
    private String user_model;
    private String user_os;
    private String user_account;
    private String user_email;
    private String user_adid;
    private String user_del;
    private String user_update_date;
    private String user_date;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_pw() {
        return user_pw;
    }

    public void setUser_pw(String user_pw) {
        this.user_pw = user_pw;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_telecom() {
        return user_telecom;
    }

    public void setUser_telecom(String user_telecom) {
        this.user_telecom = user_telecom;
    }

    public String getUser_model() {
        return user_model;
    }

    public void setUser_model(String user_model) {
        this.user_model = user_model;
    }

    public String getUser_os() {
        return user_os;
    }

    public void setUser_os(String user_os) {
        this.user_os = user_os;
    }

    public String getUser_account() {
        return user_account;
    }

    public void setUser_account(String user_account) {
        this.user_account = user_account;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_adid() {
        return user_adid;
    }

    public void setUser_adid(String user_adid) {
        this.user_adid = user_adid;
    }

    public String getUser_date() {
        return user_date;
    }

    public void setUser_date(String user_date) {
        this.user_date = user_date;
    }

    public String getUser_del() {
        return user_del;
    }

    public void setUser_del(String user_del) {
        this.user_del = user_del;
    }

    public String getUser_update_date() {
        return user_update_date;
    }

    public void setUser_update_date(String user_update_date) {
        this.user_update_date = user_update_date;
    }

    @Override
    public String toString() {
        return "MemberModel{" +
                "user_id='" + user_id + '\'' +
                ", user_pw='" + user_pw + '\'' +
                ", user_name='" + user_name + '\'' +
                ", user_phone='" + user_phone + '\'' +
                ", user_telecom='" + user_telecom + '\'' +
                ", user_model='" + user_model + '\'' +
                ", user_os='" + user_os + '\'' +
                ", user_account='" + user_account + '\'' +
                ", user_email='" + user_email + '\'' +
                ", user_adid='" + user_adid + '\'' +
                ", user_del='" + user_del + '\'' +
                ", user_update_date='" + user_update_date + '\'' +
                ", user_date='" + user_date + '\'' +
                '}';
    }
}
