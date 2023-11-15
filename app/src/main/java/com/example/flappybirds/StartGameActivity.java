package com.example.flappybirds;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

class Column {
    public int x, topY, bottomY;
    public Column(int x, int topY, int bottomY) {
        this.x = x;
        this.topY = topY;
        this.bottomY = bottomY;
    }
}

class GameView extends android.view.View {
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
    int distanceColumns = 400;
    int gapColumnY = 500;
    boolean isBirdWingsUp = true;
    int velocityColumn = 8;
    int birdHeight;
    Activity context;
    private int score;

    GameView(Activity context, int score) {
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
            columns[i] = new Column(rectWidth + i * distanceColumns, (int) (Math.random() * (gapColumnY - gapColumnY / 2)), (int) (Math.random() * (gapColumnY - gapColumnY / 2)));
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
        try {Thread.sleep(100);} catch (Exception ignored) {}

        boolean invalidate = true;
        for (int i = 0; i < columnsCount; i++) {
            columns[i].x -= velocityColumn;
            if (columns[i].x < -columnTop.getWidth()) {
                score++;
                columns[i].x += columnsCount * distanceColumns;
                columns[i].topY = (int) (Math.random() * (gapColumnY - gapColumnY / 2));
                columns[i].bottomY = (int) (Math.random() * (gapColumnY - gapColumnY / 2));
            }
            canvas.drawBitmap(columnTop, columns[i].x, columns[i].topY - (float) gapColumnY / 2, null);
            canvas.drawBitmap(columnBottom, columns[i].x, columns[i].bottomY + columnTop.getHeight() + gapColumnY, null);
            if (
                    new Rect(
                            columns[i].x,
                            columns[i].topY - gapColumnY / 2,
                            columns[i].x + columnTop.getWidth(),
                            columns[i].topY - gapColumnY / 2 + columnTop.getHeight()
                    ).intersect(
                            birdsX,
                            birdsY,
                            birdsX + birds[0].getWidth(),
                            birdsY + birdHeight) ||

                            new Rect(
                                    columns[i].x,
                                    columns[i].bottomY + columnTop.getHeight() + gapColumnY,
                                    columns[i].x + columnTop.getWidth(),
                                    columns[i].bottomY + columnTop.getHeight() + gapColumnY + columnTop.getHeight()
                            ).intersect(
                                    birdsX,
                                    birdsY,
                                    birdsX + birds[0].getWidth(),
                                    birdsY + birdHeight)
            ) invalidate = false;
        }

        Paint paint = new Paint();
        paint.setColor(android.graphics.Color.BLACK);
        paint.setTextSize(50);
        canvas.drawText(String.format(getResources().getString(R.string.score), score), 10, 100, paint);
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
        context.getWindowManager().
                getDefaultDisplay().getSize(point);
        rectHeight = point.y;
        rectWidth = point.x;
        rectangle = new Rect(0, 0, rectWidth, rectHeight);
    }
}

public class StartGameActivity extends CustomApplication {
    @Override
    void changeScore(int score) {}

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GameView gameView = new GameView(this, Lib.getScore());
        setContentView(gameView);
    }
}
