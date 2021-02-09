package com.dax.union.helper;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.union.helper.R;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreManagerActivity extends AppCompatActivity {
    TextView storeTextView;
    Button bStorePickDate;
    Button bstoreEndPickDate;
    Button bStoreStatis;
    private static List<String> storeList=new ArrayList<>();
    public static final String storeRecordName="storeRecord";

    private Long startDay=null;
    private Long endDay=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        storeTextView = findViewById(R.id.storeTextView);
        storeTextView.setTextColor(Color.RED);
        Toolbar toolbar = findViewById(R.id.toolbar);
        bStorePickDate = findViewById(R.id.bStorePickDate);
        bstoreEndPickDate = findViewById(R.id.bstoreEndPickDate);
        bStoreStatis = findViewById(R.id.bStoreStatis);

        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        readStoreList();

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

                            if(vv.getId()==R.id.bStorePickDate) {
                                bStorePickDate.setText(birthday);
                                bStorePickDate.setTextColor(0xffff0000);
                                startDay =sdf.parse(str).getTime()/1000;

                                endDay = startDay + 24 * 60 * 60;
                                bstoreEndPickDate.setText(birthday);
                                bstoreEndPickDate.setTextColor(0xffff0000);

                            }else{
                                bstoreEndPickDate.setText(birthday);
                                bstoreEndPickDate.setTextColor(0xffff0000);
                                endDay =sdf.parse(str).getTime()/1000+24 * 60 * 60;

                            }
                            bStoreStatis.callOnClick();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        //    tvShowDialog.setText(year+"-"+(++month)+"-"+day);      //将选择的日期显示到TextView中,因为之前获取month直接使用，所以不需要+1，这个地方需要显示，所以+1
                    }
                };
                DatePickerDialog dialog=new DatePickerDialog(StoreManagerActivity.this, 0,listener,year,month,day);//后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
                dialog.show();
            }
        };

        bStorePickDate.setOnClickListener(listener);
        bstoreEndPickDate.setOnClickListener(listener);
        bStoreStatis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readStoreList();
            }
        });
        }




    public void readStoreList()  {
        readStoreDataFromFile();
        reloadView(storeList);
    }

    private void reloadView(List<String> storeList) {
        HashMap<String,StoreEvent> storeMap=new HashMap<>();
        HashMap<String,Long> scoreMap=new HashMap<>();
        StringBuilder sb=new StringBuilder();
        for(String data:storeList){
            try{
                String[] detail=data.split(",");
                long time=Long.parseLong(detail[0]);
                //check time
                if(startDay!=null&&time<startDay){
                    continue;
                }
                if(endDay!=null&&time>endDay){
                    continue;
                }

                String name=detail[1];
                if(name.charAt(0)<32||name.charAt(0)>127){
                    continue;
                }


                StoreEvent storeEvent = storeMap.get(name);
                if(storeEvent==null)storeEvent=new StoreEvent();
                storeMap.put(name,storeEvent);
                int rice=Integer.parseInt(detail[2]);
                int stone=Integer.parseInt(detail[3]);
                int wood=Integer.parseInt(detail[4]);
                int iron=Integer.parseInt(detail[5]);
                int coin=Integer.parseInt(detail[6]);
                storeEvent.addRice(rice);
                storeEvent.addStone(stone);
                storeEvent.addWood(wood);
                storeEvent.addIron(iron);
                storeEvent.addCoin(coin);

            }catch (Exception e){
                Log.e("","",e);
            }



        }
        List<Map.Entry<String, StoreEvent>> list = new ArrayList<Map.Entry<String, StoreEvent>>(storeMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, StoreEvent>>()
        {
            @Override
            public int compare(Map.Entry<String, StoreEvent> o1, Map.Entry<String, StoreEvent> o2)
            {
                return (int)(o2.getValue().getTotal() - o1.getValue().getTotal());
            }
        });


        for (Map.Entry<String, StoreEvent> s : list){
            StoreEvent value = s.getValue();
            String unit;
            sb.append(s.getKey());
            sb.append(" 总量:"+formatUnit(value.getTotal()));
            sb.append("[");
            sb.append("粮:"+formatUnit(value.getRice()));
            sb.append("石:"+formatUnit(value.getStone()));
            sb.append("木:"+formatUnit(value.getWood()));
            sb.append("铁:"+formatUnit(value.getIron()));
            sb.append("金:"+formatUnit(value.getCoin()));
            sb.append("]\n");
        }
        storeTextView.setText(sb.length()==0?"未查询到数据":sb.toString());
    }

    private String formatUnit(long total){

        long divide=1;
        String unit="";
        if(total>999999999){
            divide=1000000000;
            unit="B";
        }else if(total>999999){
            divide=1000000;
            unit="M";
        }else if(total>999){
            divide=1000;
            unit="K";
        }
        return new BigDecimal(total).divide(new BigDecimal(divide)).setScale(2,BigDecimal.ROUND_HALF_UP).toString()+unit;
    }

    private static void readStoreDataFromFile() {
        SystemManager.RootCommand("chmod 777 "+MainActivity.path+"/"+storeRecordName,false);
        FileReader reader = null;
        BufferedReader br = null;
        storeList.clear();
        try {
            reader = new FileReader(MainActivity.path+"/"+storeRecordName);
            br = new BufferedReader(reader);
            String line = null;
            while ((line = br.readLine()) != null) {
                storeList.add(line);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();// finish your activity
        }
        return super.onOptionsItemSelected(item);
    }

}



