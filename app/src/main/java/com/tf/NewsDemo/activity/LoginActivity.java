package com.tf.NewsDemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tf.NewsDemo.R;
import com.tf.NewsDemo.utils.VolleyUtils;

/**
 * Created by uuu on 2018/7/2.
 */
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginAndRegister";

    private EditText adminEd;

    private EditText passwordEd;

    private CheckBox remenberPassword;

    private CheckBox autoLogin;

    private Button over;

    private Button restart;
    private int TEMP = 0;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String username="";
    private String userpwd="";
    private String type = "";
    private ProgressDialog progressDialog;
    private static int res = 0;
    static ThreadLocal<Integer> local = new ThreadLocal<Integer>();
//调用这个activity的时候，需要intent 传入TEMP的值，TEMP=1是注册，TEMP=0是登录。启动方法选择startActivityforResult
    //使用虚拟机10.0.2.2  真机为电脑ip地址
    public static final String REQUEST_URL = "http://192.168.43.54:8080/android_test/servlet/UserManager?";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        adminEd = (EditText)findViewById(R.id.admin_ed);
        passwordEd = (EditText)findViewById(R.id.password_ed);
        remenberPassword = (CheckBox)findViewById(R.id.remenber_password);
        autoLogin = (CheckBox)findViewById(R.id.autoLogin);
        over = (Button)findViewById(R.id.over);
        restart = (Button)findViewById(R.id.restart);
        preferences = getSharedPreferences("data",MODE_PRIVATE);
        editor = preferences.edit();

        Intent intent = getIntent();
        TEMP = intent.getIntExtra("TEMP",0);
        Log.i(TAG, "onCreate: TEMP"+intent.getIntExtra("TEMP",0));
        switch (TEMP){
            case 0:
                remenberPassword.setVisibility(View.VISIBLE);
                autoLogin.setVisibility(View.VISIBLE);
                Log.i(TAG, "onCreate: rem "+preferences.getBoolean("remenberPassword",false));
                if(preferences.getBoolean("remenberPassword",false)){
                    adminEd.setText(preferences.getString("admin",null));
                    passwordEd.setText(preferences.getString("password",null));
                    remenberPassword.setChecked(true);
                    autoLogin.setChecked(preferences.getBoolean("autoLogin",false));
                }
                break;
            case 1:
                remenberPassword.setVisibility(View.INVISIBLE);
                autoLogin.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
        over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (TEMP){
                    case 0:
                        type = "login";
                        trueorfalse();

                        break;
                    case 1:
                        type = "register";
                        trueorfalse();

                    default:
                        break;
                }
            }
        });
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminEd.setText(null);
                passwordEd.setText(null);
                remenberPassword.setChecked(false);
                autoLogin.setChecked(false);
            }
        });
    }
    private void save(){
        if (autoLogin.isChecked() == true) {
            Log.i(TAG, "save: 1");
            editor.putString("admin", adminEd.getText().toString());
            editor.putString("password", passwordEd.getText().toString());
            editor.putBoolean("autoLogin", true);
            editor.putBoolean("remenberPassword", true);
            editor.apply();
        } else if (remenberPassword.isChecked() == true) {
            Log.i(TAG, "save: 2");
            editor.putString("admin", adminEd.getText().toString());
            editor.putString("password", passwordEd.getText().toString());
            editor.putBoolean("autoLogin", false);
            editor.putBoolean("remenberPassword", true);
            editor.apply();
        } else {
            Log.i(TAG, "save: 3");
            editor.putString("admin", adminEd.getText().toString());
            editor.putString("password",null);
            editor.putBoolean("autoLogin", false);
            editor.putBoolean("remenberPassword", false);
            editor.apply();
        }
    }

    private void trueorfalse(){

        username=adminEd.getText().toString();
        userpwd=passwordEd.getText().toString();
        Log.i(TAG, "trueorfalse: username"+username);
        res = -1;
        if(username.equals("")||userpwd.equals("")){
            Toast.makeText(LoginActivity.this, "用户名或密码为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        showProgressDialog();
        final String params="request_flag="+type+"&username="+username+"&userpwd="+userpwd;
        String url = REQUEST_URL+params;
        Log.i(TAG, "trueorfalse: "+type);
        Log.i(TAG, "trueorfalse: url"+url);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        org.json.JSONObject jsonObject= null;


                        try {
                            jsonObject = new org.json.JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i(TAG, "jsondata: "+response+"jsonObject"+jsonObject.toString());
                           int res=jsonObject.optInt("result");

                        local.set(res);
                        Log.i(TAG, "trueorfalse: res" + res);
                        switch (TEMP) {
                            case 0:
                                if (res == 1) {
                                    save();
                                    setResult(RESULT_OK);
                                    finish();
                                } else {
                                    if (res == 1) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                }
                                break;
                            case 1:
                                if (res == 1) {
                                    save();
                                    setResult(RESULT_OK);
                                    finish();
                                } else {
                                    if (res == 1) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(LoginActivity.this, "请更换用户名", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }

                                }
                            default:
                                break;
                        }


                    }


                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "网络不可用", Toast.LENGTH_SHORT).show();
                    }
                });

        VolleyUtils.newInstance(getApplicationContext()).addQueue(stringRequest);//加入请求队列
        closeProgressDialog();
//        res = local.get();
//        Log.i(TAG, "trueorfalse: 2"+res);
//        if(res == 1){
//            setResult(RESULT_OK);
//            finish();
//        }
    }


    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }}
