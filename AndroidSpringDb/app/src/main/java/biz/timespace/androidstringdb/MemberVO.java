package biz.timespace.androidstringdb;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kimilguk on 2015-12-09.
 */
public class MemberVO {
    @SerializedName("user_id")//Json 데이터의 Key와 같은 명칭
    public String user_id;
    //@SerializedName("user_pw")//Json 데이터의 Key와 같은 명칭
    //public String user_pw;
    @SerializedName("user_name")//Json 데이터의 Key와 같은 명칭
    public String user_name;
    @SerializedName("email")//Json 데이터의 Key와 같은 명칭
    public String email;
    //@SerializedName("point")//Json 데이터의 Key와 같은 명칭
    //public int point;
    //@SerializedName("enabled")//Json 데이터의 Key와 같은 명칭
    //public boolean enabled;
    @SerializedName("level")//Json 데이터의 Key와 같은 명칭
    public String level;
    //@SerializedName("reg_date")//Json 데이터의 Key와 같은 명칭
    //public Date reg_date;
    //@SerializedName("update_date")//Json 데이터의 Key와 같은 명칭
    //public Date update_date;
}
