package com.example.xixihaha5;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BankActivity extends Activity {
    TextView showRecord;
    UtilDao utilDao=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        utilDao=new UtilDao(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_dialog);
        showRecord =(TextView)findViewById(R.id.test);
        Button btn2=(Button)findViewById(R.id.finishBtn);
        List<recordDemo> rd=utilDao.inquireData();
        if(rd.size()!=0)
        {
            Collections.sort(rd, new Comparator<recordDemo>() {
                @Override
                public int compare(recordDemo o1, recordDemo o2) {
                    if(o1.getUseTime()>o2.getUseTime()){
                        return 0;
                    } else {
                        return -1;
                    }
                }
            });
            String str = "";
            for(int i=0;i<rd.size()&&i<10;i++){
                if(i!=9)
                    str+="0";
                str+=i+1+"      "
                        +rd.get(i).getTimeid()+"      "
                        +rd.get(i).getUseTime()/35+" : "+String.format("%02d",(rd.get(i).getUseTime()%35)*2)+'\n';
            }
            showRecord.setText(str);
        }
        btn2.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }


}