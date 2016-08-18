package com.example.air.game2048;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;


import java.io.File;


public class ShapeView extends View implements Constant{
    protected int flashtimes = 0;//一次滑动,刷新屏幕的次数
    protected boolean isRun = false;
    protected Thread thread;


    public Logic logic;
    protected int direction;
    protected Card [][]cards;
    protected Paint myPaint;
    protected int margin;
    protected int halfMargin;

    public ShapeView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        margin = 10;
        halfMargin = margin / 2;
        initMyPaint();
        direction = UP;
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        for(int i = 0;i < Logic.scale + 1;i++)
        {
            canvas.drawLine(i * (Card.cardSize + margin) + halfMargin, halfMargin, i * (Card.cardSize + margin) + halfMargin, 800, myPaint);
        }
        for(int i = 0;i < Logic.scale + 1;i++)
        {
            canvas.drawLine(halfMargin, i * (Card.cardSize + margin) + halfMargin, 800, i * (Card.cardSize + margin) + halfMargin, myPaint);
        }

            for(int i = 0; i < Logic.scale;i++)
            {
                for(int j = 0; j < Logic.scale;j++)
                {
                    cards[i][j].draw(canvas, flashtimes ,direction);
                }
            }
    }

    public void initShapeView(int scale, File filepath, int cardSize){
        logic = new Logic(scale, filepath);
        cards = new Card[scale][scale];
        Logic.firstInitLogic();
        Card.setCardSize(cardSize);
        for(int i = 0; i < Logic.scale;i++)
        {
            for(int j = 0; j < Logic.scale;j++)
            {
                cards[i][j] = new Card(j ,i);
                cards[i][j].initCard(this.getResources(),
                        Logic.numbers[i][j],
                        margin * (j + 1),
                        margin * (i + 1));
            }
        }
    }

    public void initMyPaint(){
        myPaint = new Paint();
        myPaint.setAntiAlias(true);
        myPaint.setARGB(255,186,172,158);
        myPaint.setStyle(Paint.Style.STROKE);
        myPaint.setStrokeWidth(margin);
    }
}