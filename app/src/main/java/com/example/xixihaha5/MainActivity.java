package com.example.xixihaha5;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends Activity implements View.OnClickListener,
        View.OnLongClickListener {
    // 最外层布局
    LinearLayout textviews;
    LinearLayout buttons;
    TextView headText;
    Button startBtn;
    Button recordBtn;
    TextView timerView;
    long timeing;
    int[][] map = new int[10][10];
    List<Integer> flag = new ArrayList<Integer>();
    // 用来隐藏所有Button
    List<Button> buttonList = new ArrayList<Button>();
    //设置是否over
    boolean over = false;
    private AlertDialog dialog = null;
    public Handler handler = new Handler();
    UtilDao utilDao = null;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (over == false) {
                timeing++;
                timerView.setText(timeing / 35 + " : " + String.format("%02d", (timeing % 35) * 2));
//(任务内延时)
//每隔1毫秒实现定时操作更改ui页面的数字
                handler.postDelayed(this, 1);
            } else {
                //计时到10秒后关闭此定时器，重置标志位，重置计时0
                handler.removeCallbacks(this);
                timerView.setText("本轮计时已结束:" + timeing / 35 + " : " + String.format("%02d", (timeing % 35) * 2));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        utilDao = new UtilDao(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textviews = (LinearLayout) findViewById(R.id.textviews);
        buttons = (LinearLayout) findViewById(R.id.buttons);
        headText = (TextView) findViewById(R.id.head);
        startBtn = (Button) findViewById(R.id.startBtn);
        timerView = (TextView) findViewById(R.id.timerView);
        recordBtn = (Button) findViewById(R.id.recordBtn);
        headText.setGravity(Gravity.CENTER);
        timeing = 0;
        handler.postDelayed(runnable, 1);
        initData();
        initView();
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                over = false;
                textviews.removeAllViews();
                buttons.removeAllViews();
                buttonList = new ArrayList<Button>();
                flag = new ArrayList<Integer>();
                initData();
                initView();
                timeing = 0;
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 1);

            }
        });
        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BankActivity.class);
                //启动
                startActivity(intent);

            }
        });
    }

    Set<Integer> randomMine;

    private void initData() {
        // 10个地雷 显示* 数组中是-1
        // 90个 雷的边上是数字，其他是空白 0 1-8
        // 100个数字 从里面随机取走10个
        // 初始化
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                map[i][j] = 0;
            }
        }
        // 抽取100个数 99
        randomMine = getRandom();
        // 丢入map
        for (Integer integer : randomMine) {
            int hang = integer / 10;// 98
            int lie = integer % 10;
            // 所有的地雷用-1代替
            map[hang][lie] = -1;
        }
        // 为所有的空白地点去设置数值
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (map[i][j] == -1)
                    continue; // 继续下次循环
                int sum = 0;
                // 左上角
                if (i != 0 && j != 0) {// 防止下标越界
                    if (map[i - 1][j - 1] == -1)
                        sum++;
                }
                // 上面
                if (j != 0) {
                    if (map[i][j - 1] == -1)
                        sum++;
                }
                // 右上角
                if (j != 0 && i != 9) {
                    if (map[i + 1][j - 1] == -1)
                        sum++;
                }
                // 左边
                if (i != 0) {
                    if (map[i - 1][j] == -1)
                        sum++;
                }
                // 右边
                if (i != 9) {
                    if (map[i + 1][j] == -1)
                        sum++;
                }
                // 左下角
                if (j != 9 && i != 0) {
                    if (map[i - 1][j + 1] == -1)
                        sum++;
                }
                if (j != 9) {
                    if (map[i][j + 1] == -1)
                        sum++;
                }
                if (j != 9 && i != 9) {
                    if (map[i + 1][j + 1] == -1)
                        sum++;
                }
                map[i][j] = sum;
            }
        }
    }

    private Set<Integer> getRandom() {
        // 没有重复的
        Set<Integer> set = new HashSet<Integer>();
        while (set.size() != 10) {
            int random = (int) (Math.random() * 100);
            set.add(random);
        }
        return set;
    }

    // 创建视图
    private void initView() {
        int width = getResources().getDisplayMetrics().widthPixels / 10;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,
                width);
        for (int i = 0; i < 10; i++) {
            // 每一条的布局
            LinearLayout tvs = new LinearLayout(this);
            tvs.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout btns = new LinearLayout(this);
            btns.setOrientation(LinearLayout.HORIZONTAL);
            // 添加内层的100个按钮和文本
            for (int j = 0; j < 10; j++) {
                // 底层的TextView
                TextView tv = new TextView(this);
                tv.setTextColor(0xFF8f1537);
                tv.setBackgroundResource(R.drawable.textview_bg);

                tv.setLayoutParams(params);
                tv.setGravity(Gravity.CENTER);
                if (map[i][j] == -1)
                    tv.setText("*");
                else if (map[i][j] != 0)
                    tv.setText(map[i][j] + "");
                tvs.addView(tv);
                // 底层的Button
                Button btn = new Button(this);
                btn.setBackgroundResource(R.drawable.button);

                btn.setLayoutParams(params);
                btn.setTag(i * 10 + j);
                //设置监控
                btn.setOnClickListener(this);
                btn.setOnLongClickListener(this);
                buttonList.add(btn);
                btns.addView(btn);
            }
            textviews.addView(tvs);
            buttons.addView(btns);
        }
    }

    @Override
    public void onClick(View v) {
        if (over == true) return;
        int id = (Integer) v.getTag();
        int hang = id / 10;
        int lie = id % 10;
        // 隐藏按钮，显示底下的数据
        if (map[hang][lie] == -1)
            buttonList.get(id).setBackgroundResource(R.drawable.boom);
        else {
            v.setVisibility(View.INVISIBLE);
        }

        isOver(hang, lie);
        // 判断点击的是否是一个数字
        if (map[hang][lie] == 0) {
            // 开始递归
            showAroundBlank(hang, lie);
        }
        if (isWin() && over == false) {


            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式

            recordDemo rd = new recordDemo();
            rd.setTimeid(df.format(new Date()));
            rd.setUseTime(timeing);
            String[] keys = new String[]{"timeId", "useTime"};
            String[] values = new String[]{df.format(new Date()), String.valueOf(timeing)};
            utilDao.addData(df.format(new Date()), String.valueOf(timeing));
            over = true;
            showWinDialog();
        }
    }

    // 显示周围所有的button
    public void showAroundBlank(int i, int j) {
        // 检测周围的元素，如果为0 继续调用自身,并且显示
        // 不是，就显示button
        // 从左上角开始
        // 左上角
        // 先显示自己
        //System.out.print(223232);
        buttonList.get(i * 10 + j).setVisibility(View.INVISIBLE);
        if (i != 0 && j != 0) {// 防止下标越界
            if (map[i - 1][j - 1] == 0) {
                if (judgeDigui(i - 1, j - 1))
                    showAroundBlank(i - 1, j - 1);
            } else {
                hideButton(i - 1, j - 1);
            }
        }
        // 上面
        if (j != 0) {
            if (map[i][j - 1] == 0) {
                if (judgeDigui(i, j - 1))
                    showAroundBlank(i, j - 1);
            } else {
                hideButton(i, j - 1);
            }
        }
        // 右上角
        if (j != 0 && i != 9) {
            if (map[i + 1][j - 1] == 0) {
                if (judgeDigui(i + 1, j - 1))
                    showAroundBlank(i + 1, j - 1);
            } else {
                hideButton(i + 1, j - 1);
            }
        }
        // 左边
        if (i != 0) {
            if (map[i - 1][j] == 0) {
                if (judgeDigui(i - 1, j))
                    showAroundBlank(i - 1, j);
            } else {
                hideButton(i - 1, j);
            }
        }
        // 右边
        if (i != 9) {
            if (map[i + 1][j] == 0) {
                if (judgeDigui(i + 1, j))
                    showAroundBlank(i + 1, j);
            } else {
                hideButton(i + 1, j);
            }
        }
        // 左下角
        if (j != 9 && i != 0) {
            if (map[i - 1][j + 1] == 0) {
                if (judgeDigui(i - 1, j + 1))
                    showAroundBlank(i - 1, j + 1);
            } else {
                hideButton(i - 1, j + 1);
            }
        }
        if (j != 9) {
            if (map[i][j + 1] == 0) {
                if (judgeDigui(i, j + 1))
                    showAroundBlank(i, j + 1);
            } else {
                hideButton(i, j + 1);
            }
        }
        if (j != 9 && i != 9) {
            if (map[i + 1][j + 1] == 0) {
                if (judgeDigui(i + 1, j + 1))
                    showAroundBlank(i + 1, j + 1);
            } else {
                hideButton(i + 1, j + 1);
            }
        }

    }

    private void hideButton(int i, int j) {
        int position = i * 10 + j;
        buttonList.get(position).setVisibility(View.INVISIBLE);
    }

    boolean judgeDigui(int hang, int lie) {
        // 判断是否是现实的
        int position = hang * 10 + lie;
        if (buttonList.get(position).getVisibility() == View.INVISIBLE)
            return false;
        else
            return true;
    }

    private boolean isOver(int hang, int lie) {
        // OVER
        if (map[hang][lie] == -1) {
            over = true;
            showLoseDialog();
            //Toast.makeText(this, "GameOver", Toast.LENGTH_SHORT).show();
            for (int i = 0; i < buttonList.size(); i++) {
                if (map[i / 10][i % 10] == -1)
                    buttonList.get(i).setBackgroundResource(R.drawable.boom);
                else {
                    buttonList.get(i).setVisibility(View.INVISIBLE);
                }

            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onLongClick(View v) {
        // 插旗子
        // 1. 有了旗子 就取消
        // 2. 没有就插旗
        Button btn = (Button) v;
        int tag = (Integer) v.getTag();
        if (flag.contains(tag)) {
            // 如果使用drawableTop 对应的java代码的方法
            // setCompoundDrawablesWithIntrinsicBounds
            btn.setBackgroundResource(R.drawable.button);
            //btn.setText("");
            // int ArrayList.remove(int);//下标
            flag.remove((Integer) tag);
        } else {
            // 没有插旗就需要插旗，判断数量是否超过了上限
            if (flag.size() != 10) {
                flag.add(tag);
                btn.setBackgroundResource(R.drawable.flag);
                //btn.setText("∉ " + flag.size());
                //btn.setTextColor(Color.RED);
            } else {
                Toast.makeText(this, "没有旗子了", Toast.LENGTH_SHORT).show();
            }

        }
        // 消耗事件，
        return true;
    }

    // 是否胜利
    public boolean isWin() {
        int sum = 0;
        for (int i = 0; i < buttonList.size(); i++) {
            if (buttonList.get(i).getVisibility() == View.INVISIBLE)
                sum++;
        }
        if (sum == 90)
            return true;
        return false;
    }

    //显示已经赢了
    public void showWinDialog() {
        View view_dialog = getLayoutInflater().from(this).inflate(R.layout.view_win_dialog, null);
        // 创建AlertDialog对象
        dialog = new AlertDialog.Builder(this)
                .create();
        dialog.show();
        // 设置点击可取消
        dialog.setCancelable(true);
        // 获取Window对象
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置显示视图内容
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth() / 2); //设置宽度
        dialog.getWindow().setAttributes(lp);
        window.setContentView(view_dialog);
    }

    //显示已经输了
    public void showLoseDialog() {
        View view_dialog = getLayoutInflater().from(this).inflate(R.layout.view_lose_dialog, null);

        // 创建AlertDialog对象
        dialog = new AlertDialog.Builder(this)
                .create();
        dialog.show();
        // 设置点击可取消
        dialog.setCancelable(true);
        // 获取Window对象
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置显示视图内容
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth() / 2); //设置宽度
        dialog.getWindow().setAttributes(lp);
        window.setContentView(view_dialog);
    }

}