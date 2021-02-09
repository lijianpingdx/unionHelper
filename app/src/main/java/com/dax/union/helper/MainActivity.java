package com.dax.union.helper;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;

import com.union.helper.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;


import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
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
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    Button bStart;
    Button bEnd;
    Button bStatis;
    Button bCmd;
    public static String path;
    TextView statisText;

    EditText cmdText;

    Long startDay=null;
    Long endDay=null;
    boolean haveShopping;

    public final static String recordName="record.txt";

    public final static String storeRecordName="storeRecord";

    String configName="config";

    int exportFormatType=1;

    private final static int TOTAL=1;
    private final static int UNCOMPLETE=2;
    private final static int DETAIL=3;

    static HashMap<String,String> userConfig=new HashMap<>();


    static HashMap<Integer,String> dataMap=new HashMap();
    static HashMap<String,Integer> typeMap=new HashMap();
    static {
        userConfig.put("score","0");


        dataMap.put(2507, "冰龙");

        dataMap.put(2512, "购物");
        dataMap.put(2527, "雪怪");
        dataMap.put(2532, "格里芬");
        dataMap.put(2567, "云蛟");
        dataMap.put(2572, "花妖");
        dataMap.put(2577, "巨人");
        dataMap.put(2582, "蛆虫");


        dataMap.put(2599, "地狱公爵");
        dataMap.put(2604, "夺魂使者");

        dataMap.put(2619, "哈普刚肯");


        dataMap.put(2624, "布莱斯");
        dataMap.put(2630, "英雄购物");

        dataMap.put(2647, "双头冥龙");
        dataMap.put(2652, "机关木马");


        dataMap.put(2665, "女王蜂");
        dataMap.put(2680, "贪食天使");


        dataMap.put(2690, "潮汐巨人");

        dataMap.put(2755, "巫毒萨满");

        dataMap.put(2794, "远古魔像");


        dataMap.put(2830, "蒸汽狂想购物");

        dataMap.put(2850, "啊库克苏购物");

        dataMap.put(2855, "啊库克苏");

        dataMap.put(2865, "怪怪屋");


        List<Integer> keys = new ArrayList<Integer>();
        keys.addAll(dataMap.keySet());
        for(Integer key:keys) {
            typeMap.put(dataMap.get(key), key);
            int stop = key + 4;
            while (key < stop) {
                key += 1;
                dataMap.put(key, dataMap.get(key - 1));
            }
        };
    }

    public static int bytes2IntLittle(byte[] bytes)
    {
        int int1=bytes[0]&0xff;
        int int2=(bytes[1]&0xff)<<8;
        int int3=(bytes[2]&0xff)<<16;
        int int4=(bytes[3]&0xff)<<24;
        return int1|int2|int3|int4;
    }

    private  void saveUserConfig(){
        Set<String> strings = userConfig.keySet();
        StringBuilder sb=new StringBuilder();
        for (String key:strings){
            sb.append(key);
            sb.append(":");
            sb.append(userConfig.get(key));
            sb.append("\n");
        }


            String filePath=getFilesDir().toString()+"/"+configName;
            File file = new File(filePath);

            PrintStream stream=null;
            try {
                stream=new PrintStream(filePath);//写入的文件path
                stream.print(sb.toString());//写入的字符串
            } catch (FileNotFoundException e) {
                Log.e("","",e);
            }finally {
                stream.close();
            }


    }

    public void loadUserConfig(){
            FileReader reader = null;
            BufferedReader br = null;
            try {
                reader = new FileReader(getFilesDir().toString() + "/" + configName);
                br = new BufferedReader(reader);
                String line = null;
                while ((line = br.readLine()) != null) {
                    try{
                        String[] split = line.split(":");
                        if(split.length>1) {
                            userConfig.put(split[0], split[1] == null ? "0" : split[1]);
                        }else{
                            userConfig.put(split[0],"0");
                        }
                    }catch (Exception e){
                        Log.e("","",e);
                    }

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


    public void parseData(int a,int b){
        //bytes2IntLittle(new byte[] {(byte) 0x59,0x0a,0,0})
        int data = bytes2IntLittle(new byte[]{(byte) a, (byte) b, 0, 0});
        String type = dataMap.get(data);
        Integer typeStart = typeMap.get(type);
        int level=data-typeStart+1;
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
       }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
           String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }

        return stringBuilder.toString();

    }

    public void copyFilesFromRaw(int id, String fileName,String storagePath){
        InputStream inputStream=getResources().openRawResource(id);
        File file = new File(storagePath);
        if (!file.exists()) {//如果文件夹不存在，则创建新的文件夹
            file.mkdirs();
        }
        readInputStream(storagePath + "/" + fileName, inputStream);
    }

    public static boolean checkFileExists(String filePath){

        File file = new File(filePath);
        if(file.exists()){
            return true;
        }else{
            return false;
        }
    }

    public void saveFile(String content,String filePath){
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

    public static void readInputStream(String storagePath, InputStream inputStream) {
        File file = new File(storagePath);
        try {
            if (!file.exists()) {
                // 1.建立通道对象
                FileOutputStream fos = new FileOutputStream(file);
                // 2.定义存储空间biru我
                byte[] buffer = new byte[inputStream.available()];
                // 3.开始读文件
                int lenght = 0;
                while ((lenght = inputStream.read(buffer)) != -1) {// 循环从输入流读取buffer字节
                    // 将Buffer中的数据写到outputStream对象中
                    fos.write(buffer, 0, lenght);
                }
                fos.flush();// 刷新缓冲区
                // 4.关闭流
                fos.close();
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkCaptorIsWorking(){
        String result=SystemManager.RootCommand("ps |grep daxrecorder",true);

        Log.i("result",result);
        if(result.indexOf("files/daxrecorder")!=-1){
            return true;
        }else{
            return false;
        }
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
       // cksy();
        path=getFilesDir().toString();
        final String ip=NetWorkUtils.getLocalIpAddress();
       //new AlertDialog.Builder(MainActivity.this).setTitle("注意").setMessage("准备监听本机ip:"+ip).setPositiveButton("确定",null).show();
        statisText=findViewById(R.id.textView);

       // cmdText=findViewById(R.id.cmdView);
        final Button bPickDate=findViewById(R.id.btn_startDate);
        final Button endPickDate=findViewById(R.id.btn_endDate);

        RadioGroup  radioGroup  = findViewById(R.id.radioGroupDataType);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton r=(RadioButton)findViewById(checkedId);
                switch(checkedId) {
                    case R.id.radioScoreTotal:
                        exportFormatType=1;
                        break;
                    case R.id.radioUnComplete:
                        exportFormatType=2;
                        break;
                    case R.id.radioDetail:
                        exportFormatType=3;
                        break;
                }
                printfToEditView();

            }
        });


        loadUserConfig();

        findViewById(R.id.radioGroupDataType);


        final EditText scoreSet=findViewById(R.id.editTextScore);
        scoreSet.setText(userConfig.get("score"));
        scoreSet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String setValue = scoreSet.getText().toString();
                String score = userConfig.get("score");
                userConfig.put("score",setValue.equals("")?"0":setValue);
                saveUserConfig();
                printfToEditView();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        bCmd = findViewById(R.id.bCmd);
//        bCmd.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                String cmd=MainActivity.this.cmdText.getText().toString();
//                String result=SystemManager.RootCommand(cmd,true);
//                new AlertDialog.Builder(MainActivity.this).setTitle("执行结果").setMessage(result).setPositiveButton("确定",null).show();
//            }
//        });

        bEnd = findViewById(R.id.bEnd);
        bEnd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String killCaptorCmd="ps |grep -v grep|grep daxrecorder|awk '{print $2}'|xargs kill -9";
                String cleanCron="\n crontab -r";
                SystemManager.RootCommand(killCaptorCmd+cleanCron,false);
                checkStatus();
            }
        });

        bStatis = findViewById(R.id.bStatis);
        bStatis.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                printfToEditView();
            }

        });

        View.OnClickListener listener=new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);       //获取年月日时分秒
                Log.i("wxy","year"+year);
                int month=cal.get(Calendar.MONTH);   //获取到的月份是从0开始计数
                int day=cal.get(Calendar.DAY_OF_MONTH);
                final View vv=v;
                DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker picker, int year, int month, int day) {
                        int m=picker.getMonth()+1;
                        int d=picker.getDayOfMonth();
                        String birthday = picker.getYear() + "-" + (m<10?"0"+m:m+"") +"-" + (d<10?"0"+d:d+"");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                        String str = birthday.replace("-", "");
                        try {

                            if(vv.getId()==R.id.btn_startDate) {
                                bPickDate.setText(birthday);
                                bPickDate.setTextColor(0xffff0000);
                                startDay =sdf.parse(str).getTime()/1000;

                                endDay = startDay + 24 * 60 * 60;
                                endPickDate.setText(birthday);
                                endPickDate.setTextColor(0xffff0000);

                            }else{
                                endPickDate.setText(birthday);
                                endPickDate.setTextColor(0xffff0000);
                                endDay =sdf.parse(str).getTime()/1000+24*60*60;

                            }
                            bStatis.callOnClick();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        //    tvShowDialog.setText(year+"-"+(++month)+"-"+day);      //将选择的日期显示到TextView中,因为之前获取month直接使用，所以不需要+1，这个地方需要显示，所以+1
                    }
                };
                DatePickerDialog dialog=new DatePickerDialog(MainActivity.this, 0,listener,year,month,day);//后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
                dialog.show();
            }
        };

        bPickDate.setOnClickListener(listener);
        endPickDate.setOnClickListener(listener);

        bStart = (Button)findViewById(R.id.bStart);
        bStart.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                String msg="NULL";

                try {
                    String fileName="daxrecorder";
                    String fullPath=path+"/"+fileName;
                    String scriptPath=path+"/"+"check.sh";
                    String cronPath=path+"/"+"cron";
                    copyFilesFromRaw(R.raw.daxrecorder,fileName,path);
                    String recordPath=path+"/"+recordName;
                    String storePath=path+"/"+storeRecordName;
                    final String runCaptor="chmod 777 "+fullPath+" \n" +
                            "chmod 777 "+scriptPath+" \n"+
                            "nohup "+fullPath+" "+ip+" "+recordPath+" "+storePath+" >/dev/null 2>&1 &\n";
                    String checkScriptStr="#!/system/bin/sh\n" +
                            "\n" +
                            "if [ -n \"$(pgrep daxrecorder)\" ]\n" +
                            "then\n" +
                            "echo \"yes\"\n" +
                            "else\n" +
                            runCaptor+"\n" +
                            "fi";
                    if(!checkFileExists(scriptPath)){
                        saveFile(checkScriptStr,scriptPath);
                    }
                    String cronScript="* * * * * "+scriptPath;

                    if(!checkFileExists(cronPath)){
                        saveFile(cronScript,cronPath);
                    }

                    String addCron="\ncrontab "+cronPath;
                    final String finalCmd=runCaptor+addCron;

                    //new AlertDialog.Builder(MainActivity.this).setTitle("hhh").setMessage(finalCmd).setPositiveButton(msg+getFilesDir().toString(),null).show();
                    SystemManager.RootCommand(finalCmd,false);
                    checkStatus();
                }catch(Throwable e){
                    msg = e.getMessage();
                }
                //new AlertDialog.Builder(MainActivity.this).setTitle("hhh").setMessage("msg").setPositiveButton(msg+getFilesDir().set.toString(),null).show();
               }
        });
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        startDay=dayTimeInMillis();
        endDay=startDay+24*60*60;
        printfToEditView();
        checkStatus();

    }

    private Long dayTimeInMillis() {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Long time = calendar.getTimeInMillis();
        return time/1000;
    }

    public void checkStatus(){
        if(checkCaptorIsWorking()){
            bStart.setEnabled(false);
            bEnd.setEnabled(true);
        }else{
            bStart.setEnabled(true);
            bEnd.setEnabled(false);
        }
    }



    private String formatCountGift(Map<String, List<GiftEvent>> userGift){
        Set<String> keys = userGift.keySet();
        Map<String,String> countMap = new HashMap<>();
        StringBuilder sb=new StringBuilder();
        sb.append("玩家,1级,2级,3级,4级,5级\n");
        for(String key:keys){
            //  sb.append("玩家"+key+"赠送了:[");
            List<GiftEvent> gifts=userGift.get(key);
            Map<Integer,Integer> levelMap=new HashMap<>();
            int score=0;
            for(GiftEvent gift:gifts){
                Integer count=levelMap.get(gift.getLevel());
                if(count==null){
                    count =0;
                }
                count+=1;
                levelMap.put(gift.getLevel(),count);
            }
            //  sb.append("]\n");
            Integer i=1;
            sb.append(key);
            while(i<6){
                 sb.append(","+getOrdefault(levelMap,i,""));
                i++;

            }
            sb.append("\n");

        }
        return sb.toString();

    }

    public Object getOrdefault(Map map,Object key,Object dft){
            return map.get(key)==null?dft:map.get(key);
    }


    private String formatScoreGift(Map<String, List<GiftEvent>> userGift){
        StringBuilder sb=new StringBuilder();
        Set<String> keys = userGift.keySet();
        Map<String,Integer> scoreMap = new HashMap<>();
        Map<String,String> countMap = new HashMap<>();
        //没有记录的玩家
        for(String name:MemberManagerActivity.getMemberList()){
            if(!keys.contains(name)){
                userGift.put(name,new ArrayList<GiftEvent>());
            }
        }
        //新增的玩家
        List<String> newMember=new ArrayList<>();
        List<String> memberList = MemberManagerActivity.getMemberList();
        for (String key:keys){
            if(!memberList.contains(key)){
                newMember.add(key);
            }
        }
        if(!newMember.isEmpty()){
            MemberManagerActivity.getMemberList().addAll(newMember);
            MemberManagerActivity.saveMemberDataToFile();
        }

        keys = userGift.keySet();

        for(String key:keys){
          //  sb.append("玩家"+key+"赠送了:[");
            List<GiftEvent> gifts=userGift.get(key);
            Map<Integer,Integer> levelMap=new HashMap<>();
            int score=0;
            for(GiftEvent gift:gifts){
                switch (gift.getLevel()){
                    case 1:
                        score+=1;
                        break;
                    case 2:
                        score+=3;
                        break;
                    case 3:
                        score+=9;
                        break;
                    case 4:
                        score+=27;
                        break;
                    case 5:
                        score+=81;
                        break;

                }


                Integer count=levelMap.get(gift.getLevel());
                if(count==null){
                    count =0;
                }
                count+=1;
                levelMap.put(gift.getLevel(),count);
            }
          //  sb.append("]\n");


            //map 次数统计
            StringBuilder countStr=new StringBuilder();
            for(Integer lv:levelMap.keySet()){
                countStr.append(levelMap.get(lv)+"只"+lv+"级、");
            }
            if(countStr.length()>0) {
                String ctStr = countStr.substring(0, countStr.length() - 1);
                countMap.put(key, ctStr);
            }

            //map 分数统计
            scoreMap.put(key,score);

        }

        //分数排序
        List<Map.Entry<String, Integer>> list = new ArrayList<>(scoreMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>()
        {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2)
            {
               return o2.getValue() - o1.getValue();
            }
        });

        long score=Integer.parseInt(userConfig.get("score"));
        score=score*(endDay-startDay)/(24*60*60);

        //注意这里遍历的是list，也就是我们将map.Entry放进了list，排序后的集合
        for (Map.Entry s : list){
            String count = countMap.get(s.getKey());
            if(count==null){
                count="";
            }
            switch(exportFormatType) {
                case TOTAL:
                sb.append("玩家:" + s.getKey() + ",分数" + s.getValue() + "(" + count + ")\n");
                break;
                case UNCOMPLETE:
                    if(Integer.parseInt(s.getValue().toString())<score){
                        sb.append("玩家:" + s.getKey() + ",分数" + s.getValue() + "(" + count+ ")\n");
                    }
                    break;
                case DETAIL:
                sb.append("玩家:" + s.getKey());
                sb.append("[");
                    List<GiftEvent> giftEvents = userGift.get(s.getKey());
                    for(GiftEvent event:giftEvents){
                        event.time=event.time.substring(5);
                        event.time=event.time.substring(0,event.time.length()-3);
                        sb.append(event.time+" ");
                        sb.append(event.level+"级");
                        sb.append(event.getType());
                        sb.append(" ");
                    }
                    sb.append("]");
                    sb.append("\n");
                    break;
            }


        }


        String rt=sb.toString();
        return rt==""?"未查询到数据":rt;
    }
    //格式化输出内容
    //1==总分排行输出  2==未完成分数输出 3==详细记录输出

    private void printToEditText(final Map<String, List<GiftEvent>> userGift) {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MainActivity.this.statisText.setText(formatScoreGift(userGift));
            }
        });
    }

    private void printfToEditView(){
        FileReader reader = null;
        BufferedReader br = null;
        try {
            CheckBox cb= findViewById(R.id.cbShop);
            haveShopping=cb.isChecked();
            SystemManager.RootCommand("chmod 777 "+path+"/"+recordName,false);
            Map<String,List<GiftEvent>> userGift=new HashMap<>();
            reader = new FileReader(path+"/"+recordName);
            br = new BufferedReader(reader);
            String line=null;
            while((line = br.readLine()) != null){
                consume(line,userGift);
            }
            printToEditText(userGift);
        } catch (FileNotFoundException e) {
            printToEditText(new HashMap<String, List<GiftEvent>>());
            Log.e("","",e);
        } catch(IOException e){
            Log.e("","",e);
        } finally {
            try {
                if(reader!=null) {
                    reader.close();
                }
                if(br!=null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void consume(String line, Map<String, List<GiftEvent>> userGift) {
        try {
            String[] data = line.split(",");
            Long sec=Long.parseLong(data[0]);
            if(startDay!=null&&(sec<startDay)){
                return;
            }
            if(endDay!=null&&(sec>endDay)){
                return;
            }
            String time=secondToDate(sec,"yyyy-MM-dd hh:mm:ss");
            String name=data[1];
            if(name.equals("")){
                name="匿名";
            }
            int d1=Integer.parseInt(data[2]);
            int d2=Integer.parseInt(data[3]);

            int num = bytes2IntLittle(new byte[]{(byte) d1, (byte) d2, 0, 0});
            String type = dataMap.get(num);
            if("购物".equals(type)&&!haveShopping){
                return;
            }
            Integer typeStart = typeMap.get(type);
            int level=num-typeStart+1;

            List<GiftEvent> giftEvents = userGift.get(name);
            if(giftEvents==null){
                giftEvents=new ArrayList<>();
            }
            giftEvents.add(new GiftEvent(name,time,level,type));
            userGift.put(name,giftEvents);

        }catch (Exception e){

        }
    }


    private String secondToDate(long second,String patten) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(second * 1000);//转换为毫秒
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat(patten);
        String dateString = format.format(date);
        return dateString;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send) {
            Intent sendIntent =new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, statisText.getText().toString());
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "发送结果到"));
        }
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
