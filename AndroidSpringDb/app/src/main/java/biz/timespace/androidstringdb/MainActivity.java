package biz.timespace.androidstringdb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import core.AsyncResponse;
import core.PostResponseAsyncTask;

public class MainActivity extends ActionBarActivity {
    EditText etUsername, etPassword; //Java에서 사용할 EditText, Button 오브젝트 정의
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //findViewById 매서드를 이용해서 R.id.etPassword 오브젝트를 위에 정의한 변수로 형변환(cast) 합니다.
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {//btnLogin.setOnClickListener(new ...); 버튼클릭 이벤트(자동완성기능사용)을 추가 합니다.
            @Override
            public void onClick(View v) {
                HashMap postData = new HashMap();
                postData.put("mobile", "android");
                postData.put("txtUsername", etUsername.getText().toString());
                postData.put("txtPassword", etPassword.getText().toString());
                //PostResponseAsyncTask 생성자의 두 번째 파라미터 변수로 HasMap을 전달하기 때문에 HasMap데이터(Key,Value 쌍)를 정의해야 합니다.
                //String url = "http://192.168.0.2/android/login.php";
                String url = "http://192.168.100.33:8080/android/login"; //스프링(자바)한경일때
                PostResponseAsyncTask readTask = new PostResponseAsyncTask(MainActivity.this, postData, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        //Toast 팝업창으로 php의 result 값을 출력 합니다.
                        Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
                        //메인액티비티 로그인 인증 후 서브액티비티로 이동
                        /*if(s.equals("success")){*/ //PHP환경일때
                        String jsonString = s.substring(s.indexOf('{'), s.indexOf('}')+1);//스프링(자바)환경일때
                        if (!jsonString.equals("{}")) {    //스프링(자바)환경일때
                            Intent in = new Intent(MainActivity.this, SubActivity.class);//서브액티비티클래스를 인텐트로 넣어둠.
                            startActivity(in);//인텐트 화면에 뿌려줌.
                        } else {
                            Toast.makeText(MainActivity.this, s+"로그인실패", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                readTask.execute(url);
            }
        });
    }
}