package com.example.air.game2048;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

/**
 * Created by gcx on 16/7/22.
 */


public class Card implements Constant{
    private MyPhoto beforeMovingPhoto;
    private MyPhoto afterMovingPhoto;
    protected int beforeMovingNumber;
    protected int afterMovingNumber;



    static int cardSize;
    int cardX;
    int cardY;
    int cardLeft;
    int cardTop;

    int displacement;

    public Card(int X, int Y){
        cardX = X;
        cardY = Y;
        cardLeft = cardX * cardSize;
        cardTop = cardY * cardSize;
        displacement = 0;
    }

    public static void setCardSize(int cardSize){
        Card.cardSize = cardSize;
    }

    protected void setDisplacement(int displacement){
        this.displacement = displacement;
    }

    protected void calculateDisplacement(int direction){
        int occurredcard = 0;
        this.beforeMovingNumber = Logic.numbers[cardY][cardX];//记录下移动前本卡片的数字
        if(Logic.numbers[cardY][cardX] == 0)//当卡片上的数字为0时,不移动
        {
            setDisplacement(0);
            return;
        }
        switch (direction)
        {
            case UP:
               for(int counter = cardY - 1;counter >= 0;counter--)
               {
                    if(Logic.numbers[counter][cardX] != 0)
                        occurredcard++;
               }
                this.displacement = cardSize * (cardY - occurredcard);
            break;

            case DOWN:
                for(int counter = cardY + 1;counter <= Logic.scale - 1;counter++)
                {
                    if(Logic.numbers[counter][cardX] != 0)
                        occurredcard++;
                }
                this.displacement = cardSize * (Logic.scale - 1 - cardY - occurredcard);
                break;

            case LEFT:
                for(int counter = cardX - 1;counter >= 0;counter--)
                {
                    if(Logic.numbers[cardY][counter] != 0)
                        occurredcard++;
                }
                this.displacement = cardSize * (cardX - occurredcard);
                break;

            case RIGHT:
                for(int counter = cardX + 1;counter <= Logic.scale - 1;counter++)
                {
                    if(Logic.numbers[cardY][counter] != 0)
                        occurredcard++;
                }
                this.displacement = cardSize * (Logic.scale - 1 - cardX - occurredcard);
                break;
        }

    }

    protected void setMyFinalPhoto(Resources resources, int bitmapSize){
        afterMovingNumber = Logic.numbers[cardY][cardX];
        afterMovingPhoto = new MyPhoto(resources, bitmapSize, afterMovingNumber);
    }

    protected void setMyNewPhoto(Resources resources, int bitmapSize){
        beforeMovingNumber = Logic.numbers[cardY][cardX];
        beforeMovingPhoto = new MyPhoto(resources, bitmapSize, beforeMovingNumber);
    }

    protected void draw(Canvas canvas, int flashtimes, int direction){
        switch (direction)
        {
            case UP:
                if(flashtimes < displacement && flashtimes != 0){
                    if(beforeMovingNumber == 0)
                        return;
                    canvas.drawBitmap(beforeMovingPhoto.bitmap, cardLeft, cardTop - flashtimes, null);
                }
                else {
                    if(afterMovingNumber == 0)
                        return;
                    canvas.drawBitmap(afterMovingPhoto.bitmap, cardLeft, cardTop, null);
                }


                break;
            case DOWN:
                if(flashtimes < displacement && flashtimes != 0){
                    if(beforeMovingNumber == 0)
                        return;
                    canvas.drawBitmap(beforeMovingPhoto.bitmap, cardLeft, cardTop + flashtimes, null);
                }
                else {
                    if(afterMovingNumber == 0)
                        return;
                    canvas.drawBitmap(afterMovingPhoto.bitmap, cardLeft, cardTop, null);
                }
                break;
            case LEFT:
                if(flashtimes < displacement && flashtimes != 0){
                    if(beforeMovingNumber == 0)
                        return;
                    canvas.drawBitmap(beforeMovingPhoto.bitmap, cardLeft - flashtimes, cardTop, null);
                }
                else {
                    if(afterMovingNumber == 0)
                        return;
                    canvas.drawBitmap(afterMovingPhoto.bitmap, cardLeft, cardTop, null);
                }
                break;
            case RIGHT:
                if(flashtimes < displacement && flashtimes != 0){
                    if(beforeMovingNumber == 0)
                        return;
                    canvas.drawBitmap(beforeMovingPhoto.bitmap, cardLeft + flashtimes, cardTop, null);
                }
                else {
                    if(afterMovingNumber == 0)
                        return;
                    canvas.drawBitmap(afterMovingPhoto.bitmap, cardLeft, cardTop, null);
                }
                break;
        }

    }

    protected void initCard(Resources resources, int InitNumber){
        beforeMovingNumber = InitNumber;
        setMyNewPhoto(resources, cardSize);
        setMyFinalPhoto(resources,cardSize);
    }

    protected void initCard(Resources resources, int InitNumber, int marginLeft, int marginTop){
        this.cardLeft += marginLeft;
        this.cardTop += marginTop;
        beforeMovingNumber = InitNumber;
        setMyNewPhoto(resources, cardSize);
        setMyFinalPhoto(resources,cardSize);
    }

}
