package core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kimilguk on 2015-12-13.
 */
public class JsonConverter<T> {
    /**
     * 컴파일 의존성에 아래 행을 추가
     * compile 'com.google.code.gson:gson:2.2.4' = 반드시 인터넷이 실행 되어야 함.
     * 또는 프로젝트 libs에 포함시켰을 경우
     * compile files('libs/gson-2.2.4.jar') = 로컬에서 실행 가능
     * 사용법시작:
     * //Json result 값을 Member클래스에 매핑하면서 memberList에 입력
     * ArrayList<MemberVO> memberList = new JsonConverter<MemberVO>().toArrayList(result, MemberVO.class);
     *
     * Model 클래스 정의:
     * User.java에서, @SerializedName() 을 사용합니다.
     * 필드 이름이 JSON 속성과 같은 경우 setters/getters 를 만들 필요가 없음.
     * 아래는 예제 코드입:
     * import com.google.gson.annotations.SerializedName; // include gson-2.2.4.jar in libs folder
     * public class MemberVO {
     * @SerializedName("id")//Json 데이터의 Key와 같은 명칭
     * public int id;
     * @SerializedName("name")//Json 데이터의 Key와 같은 명칭
     * public String name;
     * @SerializedName("username")//Json 데이터의 Key와 같은 명칭
     * public String username;
     * @SerializedName("password")//Json 데이터의 Key와 같은 명칭
     * public String password;
     * }
     * 사용법 끝
     **/
    public ArrayList<T> toArrayList(String jsonString, Class<T> clazz){
        jsonString = jsonString.substring(jsonString.indexOf('['), jsonString.indexOf(']')+1);
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("dd/MM/yy HH:mm:ss");
        Gson gson = builder.create();
        Type type = new ListParameterizedType(clazz);
        ArrayList<T> list = gson.fromJson(jsonString, type);

        return list;
    }

    public List<T> toList(String jsonString, Class<T> clazz) {
        jsonString = jsonString.substring(jsonString.indexOf('['), jsonString.indexOf(']')+1);
        List<T> list = toArrayList(jsonString, clazz);

        return list;
    }

    private static class ListParameterizedType implements ParameterizedType {

        private Type type;

        private ListParameterizedType(Type type) {
            this.type = type;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[] {type};
        }

        @Override
        public Type getRawType() {
            return ArrayList.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }

    }
}
