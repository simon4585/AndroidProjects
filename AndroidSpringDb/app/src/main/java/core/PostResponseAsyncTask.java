package core;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by kimilguk on 2015-12-13.
 */
public class PostResponseAsyncTask extends AsyncTask<String, Void, String> {

    private ProgressDialog progressDialog;

    private AsyncResponse delegate;
    private Context context;
    private HashMap<String, String> postData = new HashMap<String, String>();
    private String loadingMessage = "Loading...";
    private boolean showLoadingMessage = true;


    //생성자 시작
    public PostResponseAsyncTask(Context context,
                                 AsyncResponse delegate){
        this.delegate = delegate;
        this.context = context;
    }

    //생성자 시작 제 2 인수에 false를 설정하면 로딩 메시지를 사용할 수 없게됩니다
    public PostResponseAsyncTask(Context context,
                                 boolean showLoadingMessage,
                                 AsyncResponse delegate
    ){
        this.delegate = delegate;
        this.context = context;
        this.showLoadingMessage = showLoadingMessage;
    }

    public PostResponseAsyncTask(Context context,
                                 HashMap<String, String> postData,
                                 AsyncResponse delegate){
        this.context = context;
        this.postData = postData;
        this.delegate = delegate;
    }

    public PostResponseAsyncTask(Context context,
                                 HashMap<String, String> postData,
                                 boolean showLoadingMessage,
                                 AsyncResponse delegate
    ){
        this.context = context;
        this.postData = postData;
        this.delegate = delegate;
        this.showLoadingMessage = showLoadingMessage;
    }

    public PostResponseAsyncTask(Context context,
                                 String loadingMessage,
                                 AsyncResponse delegate){
        this.context = context;
        this.loadingMessage = loadingMessage;
        this.delegate = delegate;
    }

    public PostResponseAsyncTask(Context context,
                                 HashMap<String, String> postData,
                                 String loadingMessage,
                                 AsyncResponse delegate){
        this.context = context;
        this.postData = postData;
        this.loadingMessage = loadingMessage;
        this.delegate = delegate;
    }
    //생성자 끝

    @Override
    protected void onPreExecute() {
        if(showLoadingMessage == true){
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(loadingMessage);
            progressDialog.show();
        }

        super.onPreExecute();
    }//포스트 실행 전

    @Override
    protected String doInBackground(String... urls){

        String result = "";

        for(int i = 0; i <= 0; i++){

            result = invokePost(urls[i], postData);
        }

        return result;
    }//백그라운드 포스트 데이터 호출

    private String invokePost(String requestURL, HashMap<String, String> postDataParams) {

        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="";

                Log.i("PostResponseAsyncTask", responseCode + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }//포스트 호출

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }//포스트 데이터 얻기

    @Override
    protected void onPostExecute(String result) {
        if(result.equals("")){
            progressDialog.dismiss();
            Toast.makeText(getContext(),"서버접속에러",Toast.LENGTH_LONG).show();
            return;
        }
        if(showLoadingMessage == true){
            if(progressDialog.isShowing()){
                progressDialog.dismiss();

            }
        }

        result = result.trim();

        delegate.processFinish(result);
    }//생성자 호출시 포스트 실행

    //겟터 셋터 실행 시작
    public String getLoadingMessage() {
        return loadingMessage;
    }

    public void setLoadingMessage(String loadingMessage) {
        this.loadingMessage = loadingMessage;
    }

    public HashMap<String, String> getPostData() {
        return postData;
    }

    public void setPostData(HashMap<String, String> postData) {
        this.postData = postData;
    }

    public Context getContext() {
        return context;
    }

    public AsyncResponse getDelegate() {
        return delegate;
    }

    //겟터 셋터 실행 끝
}
