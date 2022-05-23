package com.example.guki;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class GukView extends View {
    private Bitmap background; // задний фон
    private Paint score; // краска
    private Button exit;

    GukLogic gukController; // контроллер жука

    //
    //конструктор
    //

    public GukView(Context context) {
        super(context);
        this.gukController = new GukLogic(5, this); // задаем контроллер

        background = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_background); // задаем фон картинкой
        score = new Paint(); // краска для очков
        score.setColor(Color.BLACK); // цвет очков
        score.setTextAlign(Paint.Align.CENTER); // расположение текста
        score.setTextSize(75); // размер текста
        score.setTypeface(Typeface.DEFAULT_BOLD); // тип текста жирный
        score.setAntiAlias(true);
    }

    //
    //отрисовка жука
    //

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        gukController.update(); // обновление
        int sc = gukController.points; // очки
        canvas.drawBitmap(background, 0, 0, null); // отрисовка фона
        canvas.drawText("Очки: " + sc, getWidth() / (float) 2, 100, score); // отрисовка очков

        gukController.drawBugs(canvas); // отрисовка жуков
    }

    //
    //обработка нажатия
    //

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float eventX = event.getX();
            float eventY = event.getY();
            gukController.touchEvent(eventX, eventY); // передеам координаты нажатия и там сравниваем с координатами жуков


        }
        return true;
    }

}
