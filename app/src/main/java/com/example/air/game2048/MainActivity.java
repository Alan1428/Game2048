package com.example.air.game2048;


import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    Button jumpButton;
    Button jumpButton3;
    Button jumpButton5;
    Button DIY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        jumpButton = (Button)findViewById(R.id.jump);
        jumpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("scale", 4);
                intent.setClass(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });
        jumpButton3 = (Button)findViewById(R.id.jump3);
        jumpButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("scale", 3);
                intent.setClass(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });
        jumpButton5 = (Button)findViewById(R.id.jump5);
        jumpButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("scale", 5);
                intent.setClass(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });
        DIY = (Button)findViewById(R.id.DIY);
        DIY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SecondActivity.class);
                EditText editText = (EditText)findViewById(R.id.editText);
                String strScale = editText.getText().toString();
                if(strScale.equals(""))
                {
                    Toast.makeText(MainActivity.this, "请输入1到9之间的数", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    int scale = Integer.valueOf(strScale);
                    if(0 < scale && scale < 10)
                    {
                        intent.putExtra("scale", scale);
                        startActivity(intent);
                    }
                    else
                    {

                        Toast.makeText(MainActivity.this, "请输入1到9之间的数,否则影响游戏体验", Toast.LENGTH_SHORT).show();
                    }
                    editText.setText("");
                }


            }
        });


    }


}



