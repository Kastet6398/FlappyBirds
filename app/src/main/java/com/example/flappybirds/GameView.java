package com.example.flappybirds;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;


@SuppressLint("ViewConstructor")
public class GameView extends android.view.View {
    Bitmap background;
    Bitmap[] birds = new Bitmap[3];
    Bitmap columnTop;
    Bitmap columnBottom;
    Rect rectangle;
    int rectHeight, rectWidth;
    int birdsX, birdsY;
    int positionWings = 0;
    int velocity = 3;
    int gravity = 1;
    int columnsCount = 4;
    Column[] columns = new Column[columnsCount];
    int distancecolumns = 400;
    int gapcolumnY = 500;
    boolean isBirdWingsUp = true;
    int velocitycolumn = 8;
    int birdHeight;
    Context context;
    private int score;

    GameView(Context context, int score) {
        super(context);
        this.context = context;
        this.score = score;
        createDisplay();

        background = BitmapFactory.decodeResource(getResources(),
                R.drawable.backgroundday);
        birds[0] = BitmapFactory.decodeResource(getResources(),
                R.drawable.birdwingup);
        birds[1] = BitmapFactory.decodeResource(getResources(),
                R.drawable.birdwingmiddle);
        birds[2] = BitmapFactory.decodeResource(getResources(),
                R.drawable.birdwingdown);
        birdHeight = birds[0].getHeight();
        columnTop = BitmapFactory.decodeResource(getResources(),
                R.drawable.columnup);
        columnBottom = BitmapFactory.decodeResource(getResources(),
                R.drawable.columnbottom);
        birdsX = rectWidth / 2;
        birdsY = rectHeight / 2;

        for (int i = 0; i < columnsCount; i++)
            columns[i] = new Column(rectWidth + i * distancecolumns, (int) (Math.random() * (gapcolumnY - gapcolumnY / 2)), (int) (Math.random() * (gapcolumnY - gapcolumnY / 2)));
    }

    @SuppressLint("DrawAllocation")
    public void onDraw(@androidx.annotation.NonNull android.graphics.Canvas canvas) {

        canvas.drawBitmap(background, null, rectangle, null);
        canvas.drawBitmap(birds[positionWings], birdsX, birdsY, null);
        if (birdsY < rectHeight - birdHeight) {
            velocity += gravity;
            birdsY += velocity;
        }
        if (isBirdWingsUp) {
            positionWings++;
            if (positionWings == birds.length - 1) isBirdWingsUp = false;
        } else {
            positionWings--;
            if (positionWings == 0) isBirdWingsUp = true;
        }
        try {
            Thread.sleep(100);
        } catch (Exception ignored) {
        }

        boolean invalidate = true;
        Paint paint = new Paint();
        paint.setColor(android.graphics.Color.BLACK);
        paint.setTextSize(50);
        canvas.drawText("Your score: " + score, 10, 100, paint);
        for (int i = 0; i < columnsCount; i++) {
            columns[i].x -= velocitycolumn;
            if (columns[i].x < -columnTop.getWidth()) {
                score++;
                columns[i].x += columnsCount * distancecolumns;
                columns[i].topY = (int) (Math.random() * (gapcolumnY - gapcolumnY / 2));
                columns[i].bottomY = (int) (Math.random() * (gapcolumnY - gapcolumnY / 2));
            }
            canvas.drawBitmap(columnTop, columns[i].x, columns[i].topY - (float) gapcolumnY / 2, null);
            canvas.drawBitmap(columnBottom, columns[i].x, columns[i].bottomY + columnTop.getHeight() + gapcolumnY, null);
            if (
                    new Rect(
                            columns[i].x,
                            columns[i].topY - gapcolumnY / 2,
                            columns[i].x + columnTop.getWidth(),
                            columns[i].topY - gapcolumnY / 2 + columnTop.getHeight()
                    ).intersect(
                            birdsX,
                            birdsY,
                            birdsX + birds[0].getWidth(),
                            birdsY + birdHeight) ||

                            new Rect(
                                    columns[i].x,
                                    columns[i].bottomY + columnTop.getHeight() + gapcolumnY,
                                    columns[i].x + columnTop.getWidth(),
                                    columns[i].bottomY + columnTop.getHeight() + gapcolumnY + columnTop.getHeight()
                            ).intersect(
                                    birdsX,
                                    birdsY,
                                    birdsX + birds[0].getWidth(),
                                    birdsY + birdHeight)
            ) invalidate = false;
        }


        if (invalidate) invalidate();
        else context.startActivity(new android.content.Intent(context, GameOverActivity.class).putExtra("score", score));
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(@androidx.annotation.NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            birdsY -= rectHeight / 15;
            if (birdsY < 0) birdsY = 0;
            velocity = 3;
        }
        return true;
    }

    public void createDisplay() {
        Point point = new Point();
        ((android.app.Activity) getContext()).getWindowManager().
                getDefaultDisplay().getSize(point);
        rectHeight = point.y;
        rectWidth = point.x;
        rectangle = new Rect(0, 0, rectWidth, rectHeight);
    }
}
