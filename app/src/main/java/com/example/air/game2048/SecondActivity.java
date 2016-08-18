package com.example.air.game2048;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

public class SecondActivity extends AppCompatActivity implements Constant{
    float x1;
    float x2;
    float y1;
    float y2;
    ShapeView shapeView;
    TextView score;
    TextView bestScore;
    Button restartButton;
    Button menuButton;
    Button undoButton;
    File filepath;
    SharedPreferences preferences;
    SharedPreferences timePreferences;
    Date startDate;
    Date endDate;
    RelativeLayout.LayoutParams params;
    protected Handler handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 100:
                    score.setText(Logic.score +"");
                    bestScore.setText(Logic.bestScore+"");
                    shapeView.invalidate();
                    if(!Logic.canMove && !Logic.isOver)
                    {
                        Logic.isOver = true;
                        AlertDialog.Builder builder  = new AlertDialog.Builder(SecondActivity.this);
                        builder.setTitle("游戏结束" ) ;
                        builder.setIcon(R.mipmap.restart);
                        builder.setMessage("分数为:"+Logic.score+""+
                                "是否重新开始?" ) ;
                        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                restart();
                                shapeView.invalidate();
                            }
                        });
                        builder.setNegativeButton("否",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                shapeView.invalidate();
                            }
                        });
                        builder.show();
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);



        filepath = getFilesDir();
        Intent intent = getIntent();
        int scale = intent.getIntExtra("scale",4);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        shapeView = (ShapeView)findViewById(R.id.view);
        int screenwidth = (int)(dm.widthPixels * 0.618);
        int cardsize = (screenwidth - (scale + 1) * shapeView.margin) / scale;

        shapeView.initShapeView(scale, filepath, cardsize);


        screenwidth = cardsize * scale + (scale + 1) * shapeView.margin;

        params = new RelativeLayout.LayoutParams(screenwidth, screenwidth);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_VERTICAL,RelativeLayout.TRUE);
        shapeView.setLayoutParams(params);

        score = (TextView)findViewById(R.id.score);
        bestScore = (TextView)findViewById(R.id.bestScore);
        restartButton = (Button)findViewById(R.id.restart);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder  = new AlertDialog.Builder(SecondActivity.this);
                builder.setTitle("重新开始" ) ;
                builder.setIcon(R.mipmap.restart);
                builder.setMessage("是否重新开始?" ) ;
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        restart();
                        shapeView.invalidate();
                    }
                });
                builder.setNegativeButton("否",null);
                builder.show();
            }
        });

        menuButton = (Button)findViewById(R.id.menu);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        undoButton = (Button)findViewById(R.id.undo);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Logic.isOver)
                    return;
                Logic.undo();
                for(int k = 0; k < Logic.scale;k++)
                {
                    for(int j = 0; j < Logic.scale;j++)
                    {
                        shapeView.cards[k][j].setMyNewPhoto(SecondActivity.this.getResources(), Card.cardSize);
                        shapeView.cards[k][j].setMyFinalPhoto(SecondActivity.this.getResources(), Card.cardSize);
                    }
                }
                score.setText(""+Logic.score);
                bestScore.setText(""+Logic.bestScore);
                shapeView.invalidate();
            }
        });
        firstStart();
    }

    @Override
    protected void onPause(){
        super.onPause();
        int bestScore = preferences.getInt("bestScore", 0);

        timePreferences.edit().putInt("cumulativeMin", Logic.cumulatveMin).commit();

        if(Logic.bestScore > bestScore)
        {
            preferences.edit().putInt("bestScore", Logic.bestScore).commit();
        }
        File file = new File(filepath, "data"+Logic.scale);
        try {
            OutputStream outputStream = new FileOutputStream(file);
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            if(Logic.isOver == true)
            {
                if(file.exists())
                {
                    file.delete();
                    return;
                }
            }
            for(int i = 0;i < Logic.scale;i++)
            {
                for(int j = 0;j < Logic.scale;j++)
                {
                    dataOutputStream.writeInt(Logic.numbers[i][j]);
                }
            }
            dataOutputStream.writeInt(Logic.score);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //当每天游戏时间超过2小时后停止运行
        if(Logic.cumulatveMin > 7200000)
        {
            Toast.makeText(SecondActivity.this,"超过游戏时长2小时",Toast.LENGTH_SHORT).show();
            finish();
            return true;
        }
        if(shapeView.flashtimes != 0)//falshtimes为零确保本次滑动结束才进行下次滑动
            return true;
        if(Logic.isOver)//游戏结束后不能再操作
            return true;
        float Ydistance;
        float Xdistance;

        if(event.getAction() == MotionEvent.ACTION_DOWN) {

            endDate = new Date(System.currentTimeMillis());
            int diff = (int) (endDate.getTime() - startDate.getTime());
            Logic.cumulatveMin += diff;
            startDate = endDate;
            Log.i("====","diff"+Logic.cumulatveMin);


            //当手指按下的时候
            x1 = event.getX();
            y1 = event.getY();
        }
        if(event.getAction() == MotionEvent.ACTION_UP) {
            //当手指离开的时候
            x2 = event.getX();
            y2 = event.getY();
            Ydistance = Math.abs(y2 - y1);
            Xdistance = Math.abs(x2 - x1);
            if(Ydistance < 3 && Xdistance < 3 )
                return true;
            if(Ydistance > Xdistance)
            {
                if(y1 - y2 > 0 ) {
                    shapeView.direction = UP ;
                    for(int i = 0; i < Logic.scale;i++)
                    {
                        for(int j = 0; j < Logic.scale;j++)
                        {
                            shapeView.cards[i][j].calculateDisplacement(shapeView.direction);
                        }
                    }
                    Logic.moveNumbers(shapeView.direction);
                    for(int i = 0; i < Logic.scale;i++)
                    {
                        for(int j = 0; j < Logic.scale;j++)
                        {
                            shapeView.cards[i][j].setMyFinalPhoto(this.getResources(), Card.cardSize);
                        }
                    }
                }
                else
                {
                    if(y2 - y1 >= 0){
                        shapeView.direction = DOWN;
                        for(int i = 0; i < Logic.scale;i++)
                        {
                            for(int j = 0; j < Logic.scale;j++)
                            {
                                shapeView.cards[i][j].calculateDisplacement(shapeView.direction);
                            }
                        }
                        Logic.moveNumbers(shapeView.direction);
                        for(int i = 0; i < Logic.scale;i++)
                        {
                            for(int j = 0; j < Logic.scale;j++)
                            {
                                shapeView.cards[i][j].setMyFinalPhoto(this.getResources(), Card.cardSize);
                            }
                        }
                    }

                }
            }
            else{
                if(x1 - x2 > 0) {
                    shapeView.direction = LEFT;
                    for(int i = 0; i < Logic.scale;i++)
                    {
                        for(int j = 0; j < Logic.scale;j++)
                        {
                            shapeView.cards[i][j].calculateDisplacement(shapeView.direction);
                        }
                    }
                    Logic.moveNumbers(shapeView.direction);
                    for(int i = 0; i < Logic.scale;i++)
                    {
                        for(int j = 0; j < Logic.scale;j++)
                        {
                            shapeView.cards[i][j].setMyFinalPhoto(this.getResources(), Card.cardSize);
                        }
                    }
                }
                else
                {
                    if(x2 - x1 >= 0)
                        shapeView.direction = RIGHT;
                    for(int i = 0; i < Logic.scale;i++)
                    {
                        for(int j = 0; j < Logic.scale;j++)
                        {
                            shapeView.cards[i][j].calculateDisplacement(shapeView.direction);
                        }
                    }
                    Logic.moveNumbers(shapeView.direction);
                    for(int i = 0; i < Logic.scale;i++)
                    {
                        for(int j = 0; j < Logic.scale;j++)
                        {
                            shapeView.cards[i][j].setMyFinalPhoto(this.getResources(), Card.cardSize);
                        }
                    }
                }
            }

                Logic.canMove();
                if(!shapeView.isRun) {
                    shapeView.isRun = true;
                    shapeView.thread = null;
                    shapeView.thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (shapeView.isRun && shapeView.flashtimes < 460) {
                                shapeView.flashtimes++;
                                try {
                                    Thread.sleep(0);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Message message = handler.obtainMessage();
                                message.what = 100;
                                message.arg1 = shapeView.flashtimes;
                                handler.sendMessage(message);
                            }
                            for(int i = 0; i < Logic.scale;i++)
                            {
                                for(int j = 0; j < Logic.scale;j++)
                                {
                                    shapeView.cards[i][j].setMyNewPhoto(shapeView.getResources(), Card.cardSize);
                                }
                            }
                            shapeView.flashtimes = 0;
                            shapeView.isRun = false;

                        }
                    });
                    shapeView.thread.start();
                }
        }
        return true;
    }

    private void restart(){
        score.setText("0");
        bestScore.setText(Logic.bestScore+"");
        Logic.initLogic();
        for(int i = 0; i < Logic.scale;i++)
        {
            for(int j = 0; j < Logic.scale;j++)
            {
                shapeView.cards[i][j].initCard(this.getResources(), Logic.numbers[i][j]);
            }
        }
    }

    private void firstStart(){
        preferences = getSharedPreferences("bestScore"+Logic.scale, MODE_PRIVATE);


        startDate = new Date(System.currentTimeMillis());

        Time time = new Time();
        timePreferences = getSharedPreferences("time", MODE_PRIVATE);


        int date = timePreferences.getInt("date",time.currentDate);
        if(date == time.currentDate)
        {
            Logic.cumulatveMin = timePreferences.getInt("cumulativeMin",0);
        }
        else
        {
            Logic.cumulatveMin = 0;
        }

        Logic.bestScore = preferences.getInt("bestScore", 0);
        score.setText(""+Logic.score);
        bestScore.setText(Logic.bestScore+"");
    }
}
