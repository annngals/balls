package com.example.balls;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class TestSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    DrawThread thread;

    float x = 300, y = 300;
    Paint p = new Paint();
    float x1 = 400, y1 = 300;
    Paint p1 = new Paint();
    RectF myRect = new RectF();
    Paint pr = new Paint();
    int rad = 30;

    float left, top, right, bottom;
    boolean action = false;

    ArrayList<Integer> colors = new ArrayList<Integer>(Arrays.asList(
            R.color.ball1, R.color.ball2, R.color.ball3,
            R.color.ball4, R.color.ball5, R.color.ball6,
            R.color.ball7, R.color.ball8
    ));

    Random r = new Random();

    class DrawThread extends Thread {
        boolean flag = true;

        public DrawThread(SurfaceHolder holder) {
            this.holder = holder;
        }

        SurfaceHolder holder;

        @Override
        public void run() {
            super.run();

            left = 500;
            top = 500;
            right = 600;
            bottom = 600;

            p.setColor(getResources().getColor(R.color.ball1));
            p1.setColor(getResources().getColor(R.color.ball2));
            pr.setColor(getResources().getColor(R.color.white));

            int dist = 100;

            x+= r.nextFloat() * dist - 5;
            y+= r.nextFloat() * dist - 5;

            x1 += r.nextFloat() * dist - 5;
            y1 += r.nextFloat() * dist - 5;

            float dx = 50;
            float dy = 40;
            float dx2 = -50;
            float dy2 = -60;

            while (flag == true){
                Canvas c = holder.lockCanvas();
                myRect.set(left, top, right, bottom);

                if (c != null) {
                    c.drawColor(getResources().getColor(R.color.black));

                    x += dx;
                    y += dy;

                    x1 += dx2;
                    y1 += dy2;

                    if (x >= c.getWidth() - rad  || x1 >= c.getWidth() - rad ) {
                        dx *= -1;
                        dx2 *= -1;
                        dist -=100;

                    } else if (y >= c.getHeight() - rad || y1 >= c.getHeight() - rad ) {
                        dy *= -1;
                        dy2 *= -1;
                        dist +=100;

                    } else if (x < rad || x1 < rad){
                        dx *= -1;
                        dx2 *= -1;
                        dist -=100;

                    } else if (y < rad || y1 < rad) {
                        dy *= -1;
                        dy2 *= -1;
                        dist -=100;
                    }
                    else if (x1 == x || y1 == y) {
                        dx *= -1;
                        dx2 *= -1;
                        dy *= -1;
                        dy2 *= -1;
                        dist +=100;
                    }
                    else if ((x >= myRect.left && x <= myRect.right) && (y >= myRect.top && y <= myRect.bottom) ) {
                        dx *= -1;
                        dy *= -1;
                        dist +=100;
                        int random = (int) (r.nextFloat() * colors.size());
                        int color = colors.get(random);
                        p.setColor(getResources().getColor(color));
                    }
                    else if ((x1 >= myRect.left && x1 <= myRect.right) && (y1 >= myRect.top && y1 <= myRect.bottom)) {
                        dx2 *= -1;
                        dy *= -1;
                        dist +=100;
                        int random = (int) (r.nextFloat() * colors.size());
                        int color = colors.get(random);
                        p1.setColor(getResources().getColor(color));
                    }

                    c.drawCircle(x, y, rad, p);
                    c.drawCircle(x1, y1, rad, p1);
                    c.drawRect(myRect, pr);

                    if (p.getColor() == p1.getColor()){
                        Paint pw = new Paint();
                        pw.setColor(getResources().getColor(R.color.white));
                        pw.setTextSize(70);
                        c.drawText("You Won!", 400, 200, pw);
                        flag=false;
                    }
                }

                holder.unlockCanvasAndPost(c);

                try {
                    Thread.sleep(100); }
                catch (InterruptedException e) {}
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch( event.getAction() ) {

            case MotionEvent.ACTION_DOWN:
                action = true;
                return true;

            case MotionEvent.ACTION_MOVE:
                if (action) {
                    final int x_new = (int) event.getX();
                    final int y_new = (int) event.getY();

                    left = x_new - 50;
                    right = x_new + 50;
                    top = y_new - 50;
                    bottom = y_new + 50;
                }
                return true;

            case MotionEvent.ACTION_UP:
                action = false;
                return true;
        }
        return true;
    }

    public TestSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        thread = new DrawThread(surfaceHolder);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        thread.flag = false;
        thread = new DrawThread(surfaceHolder);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        thread.flag = false;
    }
}