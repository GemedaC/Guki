package com.example.guki;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.view.View;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

class Guk {

    boolean isRunning ;
    boolean alive ;
    Matrix matrix ;
    Bitmap texture;
    Float x, y, stepX, stepY, destX, destY;
    Integer p ;

    Guk(){
        matrix = new Matrix();
        x = 0f;
        y = 0f;
        p = 0;
        destX = 0f;
        destY = 0f;
        alive = true;

    }

    void die(){

        alive = false;
    }



}

public class GukLogic {
    private View view;
    private ArrayList<Guk> gukList;
    private final int gukCount;
    int points;


    //очищает смерти еслу жук жив додавлем в новый список
    private void removeDeads() {

        ArrayList<Guk> newBugsList = new ArrayList<>(5);
        for (Guk guk : gukList) {
            //bug.die(); // жук уимрает
            if (guk.alive) {
                newBugsList.add(guk);
            }
        }
        gukList = newBugsList;
    }


    //отрисовка жука
    void drawBugs(Canvas canvas) {
        for (Guk bug : gukList) {
            canvas.drawBitmap(bug.texture, bug.matrix, null);
        }
    }


    //создание жука
    private void createBug() {
        Guk bug = new Guk(); // жук
        bug.texture = BitmapFactory.decodeResource(view.getResources(), R.drawable.redguklive); // задаем текстуру

        gukList.add(bug); // добавляем в список
        bug.matrix.setRotate(0, bug.texture.getWidth() / 2, bug.texture.getHeight() / 2); // задаем направление
        bug.matrix.reset(); // обнуляем матрицу
        bug.p = 0;
        bug.isRunning = false;
        float ty, tx;
        int temp = (int) Math.floor(Math.random() * 4); // случайным образом задаем позицию жука
        switch (temp) {
            case 0:
                ty = (float) Math.random() * view.getHeight();
                bug.x = 0f;
                bug.y = ty;
                break;
            case 1:
                ty = (float) Math.random() * view.getHeight();
                bug.x = (float) view.getWidth();
                bug.y = ty;
                break;
            case 2:
                tx = (float) Math.random() * view.getWidth();
                bug.x = tx;
                bug.y = 0f;
                break;
            case 3:
                tx = (float) Math.random() * view.getWidth();
                bug.x = tx;
                bug.y = (float) view.getHeight();
                break;
        }
        bug.matrix.postTranslate(bug.x, bug.y);
    }


    //обработка движений жука
    private void handleBug(Guk guk) {
        if (!guk.isRunning) { // если бежит
            guk.destX = (float) Math.random() * view.getWidth(); // задаем направление по иску
            guk.destY = (float) Math.random() * view.getHeight(); //задаем направление по игреку
            guk.stepX = (guk.destX - guk.x) / 150; // задаем шаг х
            guk.stepY = (guk.destY - guk.y) / 150; // задаем шаг у
            Integer tp;
            //если жук достигает стенки экрана, то разворачиваем его
            if (guk.x <= guk.destX && guk.y >= guk.destY)
                tp = (int) Math.floor(Math.toDegrees(Math.atan(Math.abs(guk.x - guk.destX) / Math.abs(guk.y - guk.destY))));
            else if (guk.x <= guk.destX && guk.y <= guk.destY)
                tp = 90 + (int) Math.floor(Math.toDegrees(Math.atan(Math.abs(guk.y - guk.destY) / Math.abs(guk.x - guk.destX))));
            else if (guk.x >= guk.destX && guk.y <= guk.destY)
                tp = 180 + (int) Math.floor(Math.toDegrees(Math.atan(Math.abs(guk.x - guk.destX) / Math.abs(guk.y - guk.destY))));
            else
                tp = 270 + (int) Math.floor(Math.toDegrees(Math.atan(Math.abs(guk.y - guk.destY) / Math.abs(guk.x - guk.destX))));
            guk.matrix.preRotate(tp - guk.p, guk.texture.getWidth() / 2, guk.texture.getHeight() / 2);
            guk.p = tp;
            guk.isRunning = true;
        } else {
            if (Math.abs(guk.x - guk.destX) < 0.1 &&
                    Math.abs(guk.y - guk.destY) < 0.1)
                guk.isRunning = false;

            guk.matrix.postTranslate(guk.stepX, guk.stepY);
            guk.x += guk.stepX;
            guk.y += guk.stepY;
        }
    }

    //обработка нажатия
    void touchEvent(float x, float y) {
        boolean hit = false;
        for (Guk guk : gukList) {
            if (Math.abs(guk.x - x + 60) < 140 && Math.abs(guk.y - y + 60) < 150) { // проверка на попадание в жука
                guk.texture = BitmapFactory.decodeResource(view.getResources(), R.drawable.redgukdead);
                MediaPlayer.create(view.getContext(), R.raw.hit).start(); // если попали вызывает звк
                guk.stepX = 0f;
                guk.stepY = 0f;
                points += 10; // начисляются очки
                hit = true;
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        guk.die();
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, 500);
                break;
            }
        }
        if (!hit) {
            MediaPlayer.create(view.getContext(), R.raw.miss).start(); // если промах, вызываем другой звук
            points -= 5; // минус очки
        }
    }

    //конструктор
    GukLogic(int bugsCount, View view) {
        points = 0; //очки
        this.view = view; // вьюшка
        this.gukCount = bugsCount; // количество жуков
        gukList = new ArrayList<>(5); // список жуков
    }


    //обновление
    void update() {
        removeDeads(); // удаление сметрей
        while (gukList.size() < gukCount) {
            createBug(); // создание недостающих жуков
        }
        //создается потоки
        for (final Guk guk : gukList) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    handleBug(guk);
                } // в потоке обрабатывем жука
            }).start();
        }
    }
}
