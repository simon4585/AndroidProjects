package biz.timespace.androidstringdb;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import core.AsyncResponse;
import core.JsonConverter;
import core.PostResponseAsyncTask;


public class SubActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {
    ListView lvMember;
    ListView lvMember2;
    ArrayList<String> names;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        lvMember = (ListView)findViewById(R.id.lvMember);
        lvMember2 = (ListView)findViewById(R.id.lvMember2);
        HashMap postData = new HashMap();
        //postData.put("format", "json");
        /*String url = "http://192.168.0.2/android/user_list.php?format=json";*/ // 반드시 유효한 IP를 사용해야 합니다. localhost.127.0.0.1불가
        String url = "http://192.168.100.33:8080/android/list"; // 반드시 유효한 IP를 사용해야 합니다. localhost.127.0.0.1불가
        PostResponseAsyncTask readTask = new PostResponseAsyncTask(SubActivity.this, false, new AsyncResponse() {//, postData없이 GET 파라미터로 사용 예
            @Override
            public void processFinish(String result) {

                Toast.makeText(SubActivity.this, result, Toast.LENGTH_LONG).show();
                ArrayList<MemberVO> memberList = //Json result 값을 Member클래스에 매핑하면서 memberList에 입력
                        new JsonConverter<MemberVO>().toArrayList(result, MemberVO.class);

                names = new ArrayList<String>();
                for(MemberVO value: memberList){ //memberList에서 개별 값 매핑하면서 names에 입력
                    names.add(value.user_id);
                }
                //뷰액티비티의 lvMember 컨테이너에 names 1개 필드리스트 바인딩
                adapter = new ArrayAdapter<String> (
                        SubActivity.this,
                        android.R.layout.simple_list_item_1,
                        names
                );
                lvMember.setAdapter(adapter);

                //뷰액티비티의 lvMember2 컨테이너에 여러개 필드리스트 바인딩
                ArrayList<HashMap<String, String>>mapList = new ArrayList<HashMap<String,String>>();
                for(MemberVO m: memberList){ //memberList에서 개별 값 매핑하면서 names에 입력
                    HashMap map = new HashMap();
                    //id와 name을 꺼내서 map타입으로 변경.
                    map.put("user_id", m.user_id);
                    map.put("email", m.email);
                    mapList.add(map);
                }
                SimpleAdapter adapter2 =
                        new SimpleAdapter(SubActivity.this,
                                //string값을 가져옴.
                                mapList,
                                android.R.layout.simple_list_item_2,
                                //자원
                                new String[]{"user_id","email"},
                                //view 1. 첫번째 view를 뿌려줌, 2. 두번째 view를 뿌려줌
                                new int[]{android.R.id.text1,android.R.id.text2});
                lvMember2.setAdapter(adapter2);
            }
        });
        readTask.execute(url);

        lvMember.setOnItemClickListener(this);//리스트뷰에 아이템클릭리스너를 등록한다.
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //리스트에서 삭제에 사용될 user_id 아이디를 받아온다.
        final String data = (String) parent.getItemAtPosition(position);
        //삭제 다이얼로그에 보여줄 메세지를 만든다.
        String message = "해당 회원을 삭제하시겠습니까?<br />" +
                "position : " + position + "<br />" +
                "data : " + data + "<br />";
        final int someParameter = position;
        DialogInterface.OnClickListener deleteListener = new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface arg0, int arg1){
                //DB삭제
                String url = "http://192.168.100.33:8080/android/delete/"+data; //스프링(자바)한경일때
                PostResponseAsyncTask readTask = new PostResponseAsyncTask(SubActivity.this, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        //Toast 팝업창으로 스프링(자바)의 result 값을 출력 합니다.
                        //String jsonString = s.substring(s.indexOf(':')+1, s.indexOf('}'));//스프링(자바)환경일때
                        Toast.makeText(SubActivity.this, s, Toast.LENGTH_LONG).show();
                        //nt numInt = Integer.parseInt(jsonString);//삭제갯수 숫자로 변환
                        if (s.equals("SUCCESS")) {    //스프링(자바)환경일때
                            Toast.makeText(SubActivity.this, s+"DB삭제성공", Toast.LENGTH_LONG).show();
                            //선택한 회원을 리스트에서 삭제한다.
                            names.remove(someParameter);
                            // Adapter에 데이터가 바뀐걸 알리고 리스트뷰에 다시 그린다.
                            adapter.notifyDataSetChanged();
                            //화면리프레쉬가 필요하면 아래와 같이 3출 추가...
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        } else {
                            Toast.makeText(SubActivity.this, s+"DB삭제실패", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                readTask.execute(url);
            }
        };

        //삭제를 물어보는 다이얼로그를 생성한다.
        new AlertDialog.Builder(this)
                .setTitle("선택된 회원을 삭제")
                .setMessage(Html.fromHtml(message))
                .setPositiveButton("삭제", deleteListener)
                .setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                )
                .show();
    }
}
