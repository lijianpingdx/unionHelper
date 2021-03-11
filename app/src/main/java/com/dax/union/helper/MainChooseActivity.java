package com.dax.union.helper;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.union.helper.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.security.CryptoPrimitive;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.crypto.spec.DESedeKeySpec;

public class MainChooseActivity extends AppCompatActivity {
    Button hunt;
    Button store;
    Button member;
    TextView msg;
    TextView status;
    Button bStoreDelete;
    Button bHuntDelete;
    String licensePath="";
    String expireTime;



    private String message="Dax联盟助手，彻底解放管理的双手\n\n" +
            "!!注意：猎魔统计是在开礼包的时候统计!!\n" +
            "!!注意：仓库统计是在运输完成的时候统计!!\n" +
            "使用建议:\n"+
            "建议顶号的时候不要开箱\n"+
            "顶号的时候玩家的运输不会被记录，请自行处理\n\n"+
            "问题反馈/更新/建议 QQ592254266\n\n"+
            "当前版本v1.1\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_choos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        hunt=findViewById(R.id.bHuntManager);
        store=findViewById(R.id.bStoreManager);
        member=findViewById(R.id.bMemberManager);
        status=findViewById(R.id.status);
        msg=findViewById(R.id.textMsg);
        msg.setText(message);
        MainActivity.path=getFilesDir().toString();
        licensePath=MainActivity.path+"/sys.s";
        if(MainActivity.checkCaptorIsWorking()){
            status.setText("当前状态：运行中....");
            status.setTextColor(Color.RED);
        }else{
            status.setText("当前状态：停止....");
            status.setTextColor(Color.BLUE);
        }

        hunt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainChooseActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainChooseActivity.this,StoreManagerActivity.class);
                startActivity(intent);
            }
        });

        member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainChooseActivity.this,MemberManagerActivity.class);
                startActivity(intent);
            }
        });

        bStoreDelete = findViewById(R.id.bStoreDelete);
        bStoreDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemManager.RootCommand("rm -rf "+MainActivity.path+"/"+StoreManagerActivity.storeRecordName,false);
            }
        });
        bHuntDelete = findViewById(R.id.bHuntDelete);
        bHuntDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemManager.RootCommand("rm -rf "+MainActivity.path+"/"+MainActivity.recordName,false);
            }
        });

        cksy();
    }

    public void cksy(){
        //SystemManager.RootCommand("mount -o remount -w /",false);
        String license = SystemManager.RootCommand("cat "+licensePath,true);
        String result=SystemManager.RootCommand("find /data/user/0/ -name igg_login_session.xml |xargs cat|grep IGGId",true);
        if(result==null||result==""||result.indexOf("No such file")!=-1||result.indexOf("IGGId")==-1){
            //请先安装游戏并登录
            AlertDialog.Builder builder=new AlertDialog.Builder(this).setTitle("注意").setMessage("请先安装游戏并登录").setPositiveButton("确定",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    System.exit(0);
                }
            });



        }
        String userId=result.substring(result.indexOf("IGGId")+"IGGId".length()+2,result.indexOf("</"));

        if(license==null||license==""){
            //请输入注册码
            showReg(userId);
        }

        if(valid(userId, license)) {
            return ;
        }else{
            showReg(userId);

        }
    }


    public void showReg(final String userId){
//        ClipboardManager clipboardManager=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
//        clipboardManager.setPrimaryClip(ClipData.newPlainText("code",userId));
        final EditText inputServer = new EditText(this);
        inputServer.setFocusable(true);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("注册码不存在或已过期,请输入注册码").setView(inputServer).setNegativeButton(
                "注册", null);
        builder.setPositiveButton("取消",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                });
        final AlertDialog dialog = builder.show();
        Field field =null;
        try {
        field =
                dialog.getClass().getSuperclass().getDeclaredField("mShowing");
        field.setAccessible(true);

            field.set(dialog, false);
        } catch (Exception e) {
            e.printStackTrace();
        }


        final Field finalField = field;
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(inputServer.getText())) {
                    dialog.setTitle("注册码不能为空");
                    return;
                }
                String code=inputServer.getText().toString();
                dialog.setTitle("验证中...");
                if(valid(userId,code)){
                    SystemManager.RootCommand("mount -o remount -w /",false);
                    SystemManager.RootCommand("rm -rf "+licensePath,false);
                    SystemManager.RootCommand("echo "+code+">> "+licensePath,false);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.setTitle("验证成功，请重新进入");
                            Handler hh = new Handler();
                            hh.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    System.exit(0);
                                }
                            },new Random().nextInt(3000));

                        }
                    }, new Random().nextInt(2000));//3秒后执行Runnable中的run方法

                    return ;

                }else{

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.setTitle("注册码不正确");
                        }
                    }, new Random().nextInt(2000));//3秒后执行Runnable中的run方法
                    return ;
                }


            }
        });

    }


    public static String md5(String input)  {
        try {
            byte[] bb = MessageDigest.getInstance("MD5").digest(input.getBytes());
            return printHexBinary(bb);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "xxx";
        }
    }


    public static String printHexBinary(byte[] data) {
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(String.format("%02X", new Integer(b & 0xFF)));
        }
        return r.toString();
    }


    private String des(String userId, String key){
        try {
            DESedeKeySpec d = new DESedeKeySpec(new byte[10]);
            d.getKey();
            d.toString();
        }catch (java.security.InvalidKeyException e){
    e.printStackTrace();
        }
       return "daxHappy";
        }

    public boolean valid(String userId,String key){
        String result=des(userId,key);
        String cKey=md5(md5(userId)+"huzhichaoguohuipangzi");
        Log.i(userId,"udi:"+userId+"   "+key+" athere "+cKey);
        return rsaValid(userId,key.trim());
    }

    private boolean rsaValid(String userId,String key){
        try {
            String data = RSAUtils.publicDecrypt(key);
            String[] dataArr = data.split(",");
            String iggId = dataArr[0];
            Long licenseTime = Long.parseLong(dataArr[1]);
            expireTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(licenseTime);
            message+="有效时间："+expireTime;
            msg.setText(message);
            if (System.currentTimeMillis() > licenseTime) {
                return false;
            }
            if(iggId.equals("keaidepangzi")){
                return true;
            }
            if (!userId.equals(iggId)){
                return false;
            }
            return true;
        }catch (Throwable t){
            t.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_choose, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_about) {
            AlertDialog alertDialog3 = new AlertDialog.Builder(this)
                    .setTitle("关于")
                    .setIcon(R.mipmap.ic_launcher)
                    .setItems(new String[]{"当前版本v1.0","问题反馈QQ:592254266"},null).create();

            alertDialog3.show();
        }

        return super.onOptionsItemSelected(item);
    }
}
