package com.example.air.game2048;


import android.util.Log;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

import static java.lang.Math.pow;

/**
 * Created by gcx on 16/7/23.
 */
public class Logic implements Constant{
    protected static Boolean isOver;
    protected static boolean canMove;
    protected static int scale;
    protected static int score;
    protected static int bestScore;
    protected static int [][]numbers;
    protected static int ids[];
    protected static int turns;

    protected static int []emptyIndex;
    protected static int emptyLength;
    protected static Stack<Integer> stacks[][];
    protected static Stack<Integer> scoreStack;
    protected static File filepath;
    protected static int cumulatveMin;
    private static class XY{
        int x;
        int y;
    }
    public Logic(int scale, File filepath){
        this.scale = scale;
        numbers = new int[scale][scale];
        emptyIndex = new int[scale * scale];
        Logic.filepath = filepath;
        ids = new int[12];
        ids[0] = R.drawable.zero;
        ids[1] = R.drawable.one;
        ids[2] = R.drawable.two;
        ids[3] = R.drawable.three;
        ids[4] = R.drawable.four;
        ids[5] = R.drawable.five;
        ids[6] = R.drawable.six;
        ids[7] = R.drawable.seven;
        ids[8] = R.drawable.eight;
        ids[9] = R.drawable.nine;
        ids[10] = R.drawable.ten;
        ids[11] = R.drawable.zero;

        stacks = new Stack[scale][scale];
        for (int i = 0; i < scale;i++)
        {
            for(int j = 0; j < scale;j++)
            {
                stacks[i][j] = new Stack<Integer>();
            }
        }
        scoreStack = new Stack<>();
    }

    static protected void moveNumbers(int direction){
        emptyLength = 0;
        Stack<Integer> stack = new Stack();
        boolean isContinuous = false;
        score = scoreStack.peek();
        switch (direction)
        {
            case UP:
            for(int j = 0;j < scale;j++)
            {
                for(int i = 0; i < scale;i++)
                {
                    if(numbers[i][j] == 0)
                        continue;
                    if(stack.empty())
                    {
                        stack.push(numbers[i][j]);
                    }
                    else
                    {
                        if(numbers[i][j] == stack.peek() && isContinuous == false)
                        {
                            stack.push(stack.pop() + 1);
                            score += pow(2, stack.peek());
                            if(score > bestScore)
                                bestScore = score;
                            isContinuous = true;
                        }
                        else
                        {
                            stack.push(numbers[i][j]);
                            isContinuous = false;
                        }
                    }
                }
                for(int i = stack.size();i < scale;i++)
                {
                    numbers[i][j] = 0;
                    emptyIndex[emptyLength++] = translateToOneDimension(i, j);
                }
                for(int i = stack.size() - 1; i >= 0;i--)
                    numbers[i][j] = stack.pop();
                isContinuous = false;
            }
            break;

            case DOWN:
            for(int j = 0;j < scale;j++)
            {
                for(int i = scale - 1;i >= 0;i--)
                {
                    if(numbers[i][j] == 0)
                        continue;
                    if(stack.empty())
                    {
                        stack.push(numbers[i][j]);
                    }
                    else
                    {
                        if(numbers[i][j] == stack.peek() && isContinuous == false)
                        {
                            stack.push(stack.pop() + 1);
                            score += pow(2, stack.peek());
                            if(score > bestScore)
                                bestScore = score;
                            isContinuous = true;
                        }
                        else
                        {
                            stack.push(numbers[i][j]);
                            isContinuous = false;
                        }
                    }
                }
                for(int i = 0; i < scale - stack.size();i++)
                {
                    numbers[i][j] = 0;
                    emptyIndex[emptyLength++] = translateToOneDimension(i, j);
                }
                for(int i = scale - stack.size();i < scale;i++)
                    numbers[i][j] = stack.pop();
                isContinuous = false;
            }
            break;

            case LEFT:
            for(int i = 0;i < scale;i++)
            {
                for(int j = 0; j < scale;j++)
                {
                    if(numbers[i][j] == 0)
                        continue;
                    if(stack.empty())
                    {
                        stack.push(numbers[i][j]);
                    }
                    else
                    {
                        if(numbers[i][j] == stack.peek() && isContinuous == false)
                        {
                            stack.push(stack.pop() + 1);
                            score += pow(2, stack.peek());
                            if(score > bestScore)
                                bestScore = score;
                            isContinuous = true;
                        }
                        else
                        {
                            stack.push(numbers[i][j]);
                            isContinuous = false;
                        }
                    }
                }
                for(int j = stack.size();j < scale;j++)
                {
                    numbers[i][j] = 0;
                    emptyIndex[emptyLength++] = translateToOneDimension(i, j);
                }
                for(int j = stack.size() - 1; j >= 0;j--)
                    numbers[i][j] = stack.pop();
                isContinuous = false;
            }
            break;

            case RIGHT:
            for(int i = 0;i < scale;i++)
            {
                for(int j = scale - 1; j >= 0;j--)
                {
                    if(numbers[i][j] == 0)
                        continue;
                    if(stack.empty())
                    {
                        stack.push(numbers[i][j]);
                    }
                    else
                    {
                        if(numbers[i][j] == stack.peek() && isContinuous == false)
                        {
                            stack.push(stack.pop() + 1);
                            score += pow(2, stack.peek());
                            if(score > bestScore)
                                bestScore = score;
                            isContinuous = true;
                        }
                        else
                        {
                            stack.push(numbers[i][j]);
                            isContinuous = false;
                        }
                    }
                }
                for(int j = 0; j < scale - stack.size();j++)
                {
                    numbers[i][j] = 0;
                    emptyIndex[emptyLength++] = translateToOneDimension(i, j);
                }
                for(int j = scale - stack.size();j < scale;j++)
                    numbers[i][j] = stack.pop();
                isContinuous = false;
            }
            break;
        }
        if(emptyLength > 0)
        {
            setRandomNumber();
        }
        storeNumber();
    }

    static private int translateToOneDimension(int Y, int X){
        return Y * scale + X;
    }

    static private XY translateToTwoDimension(int index){
        XY xy = new XY();
        xy.y = index / scale;
        xy.x = index % scale;
        return xy;
    }

    static protected void setRandomNumber(){
        XY tworandplace = new XY();
        int index = (int)(Math.random() * emptyLength);
        int randplace = emptyIndex[index];
        tworandplace = translateToTwoDimension(randplace);
        numbers[tworandplace.y][tworandplace.x] = percentageRandom();
    }

    static protected void canMove(){
        canMove = false;
        for(int i = 0;i < scale;i++)
        {
            for(int j = 0; j < scale;j++)
            {
                if(numbers[i][j] == 0)
                {
                    canMove = true;
                    return;
                }
            }
        }
        for(int i = 0;i < scale;i++)
        {
            for(int j = 0; j < scale - 1;j++)
            {
                if(numbers[i][j] == numbers[i][j + 1])
                {
                    canMove = true;
                    return;
                }
            }
        }
        for(int j = 0;j < scale;j++)
        {
            for(int i = 0; i < scale - 1;i++)
            {
                if(numbers[i][j] == numbers[i + 1][j])
                {
                    canMove = true;
                    return;
                }
            }
        }
    }

    static private void clearNumber(){
        for(int i = 0;i < scale;i++)
        {
            for(int j = 0; j < scale;j++)
            {
                numbers[i][j] = 0;
                stacks[i][j].clear();
            }
        }
    }//清空数组与栈

    static protected void initLogic(){
        score = 0;
        turns = 0;
        clearNumber();
        setInitRandomNumber();
        storeNumber();
        canMove = true;
        isOver = false;
    }

    static protected void firstInitLogic(){
        File file = new File(filepath, "data"+scale);
        if(file.exists())//文件存在且上次游戏未结束才载入
        {
            try {
                InputStream inputStream = new FileInputStream(file);
                DataInputStream dataInputStream = new DataInputStream(inputStream);
                    for(int i = 0;i < Logic.scale;i++)
                    {
                        for(int j = 0;j < Logic.scale;j++)
                        {
                            Logic.numbers[i][j] = dataInputStream.readInt();
                        }
                    }
                    score = dataInputStream.readInt();
                    turns = 0;
                    storeNumber();
                    canMove = true;
                    isOver = false;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        else
        {
            initLogic();
        }
    }

    static private void setInitRandomNumber(){
        emptyLength = scale * scale;
        for (int i = 0;i < Logic.emptyLength;i++)
            emptyIndex[i] = i;

        XY tworandplace = new XY();
        int index = (int)(Math.random() * emptyLength);
        int randplace = emptyIndex[index];
        tworandplace = translateToTwoDimension(randplace);
        numbers[tworandplace.y][tworandplace.x] = percentageRandom();

        for(int i = index;i < Logic.emptyLength - 1;i++)
        {
            emptyIndex[i]=emptyIndex[i+1];
        }

        emptyLength -= 1;
        XY tworandplace2 = new XY();
        int index2 = (int)(Math.random() * emptyLength);
        int randplace2 = emptyIndex[index2];
        tworandplace2 = translateToTwoDimension(randplace2);
        numbers[tworandplace2.y][tworandplace2.x] = percentageRandom();
    }

    static private int percentageRandom(){
        double randomNumber;
        randomNumber = Math.random();
        if(randomNumber >= 0 && randomNumber <= 0.8)
        {
            return 1;
        }
        else
            return 2;
    }

    static private void storeNumber(){
        for(int i = 0;i < scale;i++)
        {
            for (int j = 0;j < scale;j++)
            {
                stacks[i][j].push(numbers[i][j]);
            }
        }
        scoreStack.push(score);
        turns++;
    }

    static protected void undo(){
        if(stacks[0][0].size() == 1)
            return;
        scoreStack.pop();
        score = scoreStack.peek();
        for(int i = 0;i < scale;i++)
        {
            for (int j = 0;j < scale;j++)
            {
                stacks[i][j].pop();
                numbers[i][j] = stacks[i][j].peek();
            }
        }
    }
}
