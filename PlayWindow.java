package com.snake.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import java.io.File;
import java.util.LinkedList;
import static com.badlogic.gdx.math.MathUtils.random;

public class PlayWindow extends Window
{

    private static class DirectionRegulator
    {
        private Texture right, left, up, down;
        public static float SCALE = 4f;
        private int size = (int) (Snake.SEGSIZE * SCALE);
        private int x, y;

        DirectionRegulator(int x, int y)
        {
            this.x = x; this.y = y;
            left = new Texture("left.png");
            right = new Texture("right.png");
            up = new Texture("up.png");
            down = new Texture("down.png");

        }

        public void draw(SpriteBatch batch)
        {
            batch.draw(left, x, y + size, size, size);
            batch.draw(right, x + size * 2, y + size, size, size);
            batch.draw(up, x + size, y + size * 2, size, size);
            batch.draw(down, x + size, y, size, size);
        }

        public void setPosition(int n, int m)
        {
            this.x = n * size;
            this.y = m * size;
        }

        public int pressProcessing(int x, int y)
        {
            x /= size;
            y /= size;
            if (x == 0 && y == 1)
                return Snake.LEFT;
            if (x == 1 && y == 0)
                return Snake.DOWN;
            if (x == 1 && y == 2)
                return Snake.UP;
            if (x == 2 && y == 1)
                return Snake.RIGHT;
            return 0;
        }
    }

    private Snake snake;
    private Apple apple;
    private LinkedList<Stone> stones;
    private DirectionRegulator directionRegulator;
    private long score;
    private BitmapFont text;
    private CharSequence message;
    private Texture gameOverImage;

    PlayWindow(WindowManager windowManager) {
        super(windowManager);
        text = new BitmapFont();
        text.getData().setScale(1.5f);
        text.setColor(Color.BLACK);
        score = 0;
        apple = new Apple(15, 0);
        snake = new Snake(1, 0);
        stones = new LinkedList<Stone>();
        directionRegulator = new DirectionRegulator(0, 0);
        directionRegulator.setPosition(0, 0);
        gameOverImage = new Texture("gameover.png");
    }

    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glClearColor(100, 100, 100,0.5f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        message = "Score: " + score + ".";
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
            text.draw(batch, message, 10, camera.viewportHeight - 10);
            directionRegulator.draw(batch);
        batch.end();
        if (snake.ableToEat(apple)){
            snake.eat(apple);
            if (stones.size() < 50) {
                Stone stone = new Stone(random(0, 40) * Snake.SEGSIZE, random(0, 40) * Snake.SEGSIZE);
                while (snake.hitsStone(stone) || !snake.stoneIsInRightPos(stone))
                    stone.changePosition();
                stones.add(stone);
                score++;
                if (score % 10 == 0)
                    snake.accelerate(0.005f);
            }
        }
        for (Stone stone : stones)
            stone.draw();
        snake.draw();
        apple.draw();
        snake.update();
        int width = (int) camera.viewportWidth;
        int height = (int) camera.viewportHeight;

        if (!snake.isAlive()) {
            batch.begin();
            int x = (width - gameOverImage.getWidth()) / 2;
            int y = (height - gameOverImage.getHeight()) / 2;
            batch.draw(gameOverImage, x, y);
            batch.end();
            if (Gdx.input.isTouched())
                wm.push(new PlayWindow(wm));
            return;
        }
        catchPress();
        for (Stone stone : stones) {
            if (snake.hitsStone(stone)) {
                snake.playHitSound();
                snake.die();
                return;
            }
        }
        while (!rightLocation())
            apple.changePosition();
        if (snake.outOfScreen(width, height)) {
            snake.playHitSound();
            snake.die();
        }
    }

    @Override
    public void update(float deltaTime) {}

    @Override
    public void catchPress() {
        if (Gdx.input.isKeyPressed(Input.Keys.TAB))
            snake.pause();
        if (!Gdx.input.isTouched())
            return;
        if (!snake.inMotion())
            return;
        snake.keyPresProcessing();
        if (!Gdx.input.isTouched())
            return;
        int x = Gdx.input.getX();
        int y = Gdx.input.getY();
        Vector3 v = new Vector3(x, y, 0);
        camera.unproject(v);
        x = (int)v.x;
        y = (int)v.y;
        int direction = directionRegulator.pressProcessing(x, y);
        if (direction == Snake.LEFT)
            snake.rotate(Snake.LEFT);
        if (direction == Snake.RIGHT)
            snake.rotate(Snake.RIGHT);
        if (direction == Snake.UP)
            snake.rotate(Snake.UP);
        if (direction == Snake.DOWN)
            snake.rotate(Snake.DOWN);
    }

    @Override
    public void dispose() {
        snake.dispose();
        apple.dispose();
        batch.dispose();
        for (Stone stone : stones)
            stone.dispose();
    }

    public boolean rightLocation()
    {
        for (Stone stone : stones)
        {
            Rectangle a = new Rectangle();
            Rectangle b = new Rectangle();
            a.setSize(Snake.SEGSIZE); b.setSize(Snake.SEGSIZE);
            a.setPosition(stone.x, stone.y);
            b.setPosition(apple.x, apple.y);
            if (a.overlaps(b))
                return false;
        }
        return true;
    }
}
