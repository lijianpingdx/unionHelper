package com.dax.union.helper;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
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
import java.util.ArrayList;
import java.util.List;

public class MemberManagerActivity extends AppCompatActivity {

    private static List<String> memberList=new ArrayList<>();
    static final ThreadLocal<Boolean> isCycle = new ThreadLocal<Boolean>();
    EditText searchText;
    TextView memberTextView;
    Button delete;
    Button add;
    {
        isCycle.set(false);
    }

    public static List<String> getMemberList(){
        if(memberList.isEmpty()){
            readMemberDataFromFile();
        }
        return memberList;
    }

    public final static String fileName="member";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_manager);
        searchText = findViewById(R.id.memberManagerSearchText);
        memberTextView = findViewById(R.id.memberTextView);
        memberTextView.setTextColor(Color.RED);
        memberTextView.setTextSize(20);
        readMemberDataFromFile();
        delete=findViewById(R.id.memberManagerDelete);
        add=findViewById(R.id.memberManagerAdd);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用


        reloadView(memberList);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memberList.remove(searchText.getText().toString());
                saveMemberDataToFile();
                search(searchText.getText().toString());
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=searchText.getText().toString();
                if(!memberList.contains(name)){
                    memberList.add(name);
                    saveMemberDataToFile();
                    search(searchText.getText().toString());
                }
            }
        });


        searchText.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(isCycle.get()){
                    isCycle.set(false);
                }else{
                    search(s.toString());
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    String lastSearchStr="";
    private void search(String str){
        List<String> nameList=new ArrayList<>();
        for (String s1 : memberList) {
            if(s1.toUpperCase().startsWith(str.toUpperCase())){
                nameList.add(s1);
            }
        }

        if(nameList.size()==1){
            if(lastSearchStr.length()>str.length()&&lastSearchStr.startsWith(str)){
                lastSearchStr=str;
            }else {
                isCycle.set(true);
                searchText.setText(nameList.get(0));
                lastSearchStr=nameList.get(0);
            }
        }
        reloadView(nameList);
    }

    private void reloadView(List<String> list) {
        StringBuilder sb=new StringBuilder();
        for (String name : list) {
            sb.append("玩家名："+name);
            sb.append("\n");
        }
        if(sb.length()==0){
            sb.append("未查询到结果");
        }
        memberTextView.setText(sb.toString());
    }

    public static void saveMemberDataToFile(){
        String filePath=MainActivity.path+"/"+fileName;
        File file = new File(filePath);

        PrintStream stream=null;
        try {
            stream=new PrintStream(filePath);//写入的文件path
            stream.print(covertListToData());//写入的字符串
        } catch (FileNotFoundException e) {
            Log.e("","",e);
        }finally {
            stream.close();
        }

    }


    public static void readMemberDataFromFile()  {
        FileReader reader = null;
        BufferedReader br = null;
        try {
            reader = new FileReader(MainActivity.path+"/"+fileName);
            br = new BufferedReader(reader);
            String line = null;
            while ((line = br.readLine()) != null) {
                memberList.add(line);
            }
        }catch (Exception e){
            Log.e("","",e);
        }finally {
            try {
                reader.close();
                br.close();
            }catch (Exception e){

            }
        }
    }

    public void readMemberList()  {
        readMemberDataFromFile();
        reloadView(memberList);
    }

    private static String covertListToData(){
        StringBuilder sb=new StringBuilder();
        if(memberList!=null&&!memberList.isEmpty()){
            for (String s : memberList) {
                sb.append(s);sb.append('\n');
            }
        }
        return sb.toString();
    }


    private void saveFile(String content,String filePath){
        File file = new File(filePath);
        try{
            if(!file.exists()){
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(content.getBytes());
                fos.flush();
                fos.close();
            }
        }catch (Exception e){
            Log.e("","",e);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



}



