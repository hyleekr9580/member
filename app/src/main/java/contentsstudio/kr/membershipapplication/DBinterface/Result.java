package contentsstudio.kr.membershipapplication.DBinterface;

/**
 * Created by hoyoung on 2016-04-27.
 */
public class Result {

    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        final StringBuffer stringBuffer = new StringBuffer("Result{");
        stringBuffer.append("result='").append(result).append('\'');
        stringBuffer.append('}');
        return stringBuffer.toString();

    }
}
