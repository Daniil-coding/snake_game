package com.snake.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import static com.snake.game.MainSnakeGame.CAMHEIGHT;
import static com.snake.game.MainSnakeGame.CAMWIDTH;
import static java.lang.Math.abs;

public class Snake
{
    private class TailPart
    {
        private Texture img;
        public int x, y;
        private SpriteBatch batch;
        private String rotateDirection = "null";

        TailPart(int x, int y) {
            this.x = x;
            this.y = y;
            img = new Texture("horizontalTailPart.png");
            batch = new SpriteBatch();
        }

        public String getRotateDirection() {
            return rotateDirection;
        }

        public void setRotateDirection(String rotateDirection) {
            switch (rotateDirection) {
                case "null":
                    this.rotateDirection = "null";
                    img = new Texture("tailPart.png");
                    break;
                case "RU":
                    this.rotateDirection = "RU";
                    img = new Texture("RUtailPart.png");
                    break;
                case "RD":
                    this.rotateDirection = "RD";
                    img = new Texture("RDtailPart.png");
                    break;
                case "LD":
                    this.rotateDirection = "LD";
                    img = new Texture("LDtailPart.png");
                    break;
                case "LU":
                    this.rotateDirection = "LU";
                    img = new Texture("LUtailPart.png");
                    break;
            }
        }

        public void draw() {
            batch.setProjectionMatrix(camera.combined);
            camera.update();
            batch.begin();
                batch.draw(img, x, y);
            batch.end();
        }

        public void setVertical() {
            img = new Texture("verticalTailPart.png");
        }

        public void setHorizontal() {
            img = new Texture("horizontalTailPart.png");
        }

        public void dispose() {
            this.img.dispose();
            this.batch.dispose();
        }
    }

    private class Head
    {
        private Texture img;
        public int x, y;
        private SpriteBatch batch;

        Head(int x, int y) {
            this.x = x;
            this.y = y;
            img = new Texture("RIGHThead.png");
            batch = new SpriteBatch();
        }

        public void setRight() {
            img = new Texture("RIGHThead.png");
            direction = RIGHT;
        }

        public void setUp() {
            img = new Texture("UPhead.png");
            direction = UP;
        }

        public void setDown() {
            img = new Texture("DOWNhead.png");
            direction = DOWN;
        }

        public void setLeft() {
            img = new Texture("LEFThead.png");
            direction = LEFT;
        }

        public void draw() {
            batch.setProjectionMatrix(camera.combined);
            camera.update();
            batch.begin();
                batch.draw(img, x, y);
            batch.end();
        }

        public void move() {
            switch (direction) {
                case RIGHT:
                    x += SEGSIZE;
                    break;
                case LEFT:
                    x -= SEGSIZE;
                    break;
                case DOWN:
                    y -= SEGSIZE;
                    break;
                case UP:
                    y += SEGSIZE;
                    break;
            }
        }

        public void dispose() {
            this.batch.dispose();
            this.img.dispose();
        }
    }

    private class Tail
    {
        private class TailEnd
        {
            private Texture img;
            public int x, y;
            private SpriteBatch batch;
            private int direction = LEFT;

            TailEnd(int x, int y) {
                this.x = x;
                this.y = y;
                img = new Texture("LEFTtail.png");
                batch = new SpriteBatch();
            }

            public void setRight() {
                img = new Texture("RIGHTtail.png");
                direction = RIGHT;
            }

            public void setUp() {
                img = new Texture("UPtail.png");
                direction = UP;
            }

            public void setDown() {
                img = new Texture("DOWNtail.png");
                direction = DOWN;
            }

            public void setLeft() {
                img = new Texture("LEFTtail.png");
                direction = LEFT;
            }

            public void draw() {
                batch.setProjectionMatrix(camera.combined);
                camera.update();
                batch.begin();
                    batch.draw(img, x, y);
                batch.end();
            }

            public void move() {
                switch (direction) {
                    case RIGHT:
                        x -= SEGSIZE;
                        break;
                    case LEFT:
                        x += SEGSIZE;
                        break;
                    case DOWN:
                        y += SEGSIZE;
                        break;
                    case UP:
                        y -= SEGSIZE;
                        break;
                }
            }

            public void dispose() {
                this.batch.dispose();
                this.img.dispose();
            }
        }

        private TailPart[] tailParts;
        private TailEnd tailEnd;
        private int x, y;

        Tail(int x, int y) {
            x -= SEGSIZE;
            this.x = x;
            this.y = y;
            tailParts = new TailPart[length];
            tailEnd = new TailEnd(this.x - length * SEGSIZE, this.y);
            for (int i = 0; i < length; ++i)
                tailParts[i] = new TailPart(x - i * SEGSIZE, y);
        }

        public void move() {
            TailPart tp = new TailPart(0, 0);
            switch (direction) {
                case RIGHT:
                    tp.x = tailParts[0].x + SEGSIZE;
                    tp.y = tailParts[0].y;
                    break;
                case LEFT:
                    tp.x = tailParts[0].x - SEGSIZE;
                    tp.y = tailParts[0].y;
                    break;
                case UP:
                    tp.x = tailParts[0].x;
                    tp.y = tailParts[0].y + SEGSIZE;
                    break;
                case DOWN:
                    tp.x = tailParts[0].x;
                    tp.y = tailParts[0].y - SEGSIZE;
                    break;
            }
            if (direction == UP || direction == DOWN)
                tp.setVertical();
            tail.x = tp.x;
            tail.y = tp.y;
            switch (tailParts[length - 1].getRotateDirection()) {
                case "null":
                    break;
                case "RU":
                    if (tailEnd.direction == DOWN) {
                        tailEnd.setRight();
                        tailEnd.x += SEGSIZE;
                        tailEnd.y += SEGSIZE;
                    } else {
                        tailEnd.setUp();
                        tailEnd.y += SEGSIZE;
                        tailEnd.x += SEGSIZE;
                    }
                    break;
                case "RD":
                    if (tailEnd.direction == LEFT) {
                        tailEnd.setDown();
                        tailEnd.y -= SEGSIZE;
                        tailEnd.x += SEGSIZE;
                    } else {
                        tailEnd.setRight();
                        tailEnd.x += SEGSIZE;
                        tailEnd.y -= SEGSIZE;
                    }
                    break;
                case "LU":
                    if (tailEnd.direction == RIGHT) {
                        tailEnd.setUp();
                        tailEnd.y += SEGSIZE;
                        tailEnd.x -= SEGSIZE;
                    } else {
                        tailEnd.setLeft();
                        tailEnd.x -= SEGSIZE;
                        tailEnd.y += SEGSIZE;
                    }
                    break;
                case "LD":
                    if (tailEnd.direction == UP) {
                        tailEnd.setLeft();
                        tailEnd.x -= SEGSIZE;
                        tailEnd.y -= SEGSIZE;
                    } else {
                        tailEnd.setDown();
                        tailEnd.y -= SEGSIZE;
                        tailEnd.x -= SEGSIZE;
                    }
                    break;
            }
            tailEnd.move();
            for (int i = length - 1; i > 0; --i)
                tailParts[i] = tailParts[i - 1];
            tailParts[0] = tp;
        }

        public void draw() {
            for (int i = 0; i < length; ++i)
                tailParts[i].draw();
            tailEnd.draw();
        }

        public void rotate(String direction) {
            tailParts[0].setRotateDirection(direction);
        }

        public void dispose() {
            for (int i = 0; i < length; ++i)
                tailParts[i].dispose();
            tailEnd.dispose();
        }
    }

    public static final int RIGHT = 39, LEFT = 37, UP = 38, DOWN = 40, SEGSIZE = 28;
    public int x, y;
    private int length = 1, direction = RIGHT;
    private float time = 0f, movePeriod = 0.2f;
    private Head head;
    private Tail tail;
    private Sound eatSound, hitSound, tailBiteSound;
    private OrthographicCamera camera;
    private boolean alive, pause;
    private ArrayList<Sound> sounds;
    private int soundIndex;

    public Snake(int x, int y)
    {
        alive = true;
        pause = false;
        x *= SEGSIZE;
        y *= SEGSIZE;
        this.x = x;
        this.y = y;
        head = new Head(x, y);
        tail = new Tail(x, y);
        eatSound = Gdx.audio.newSound(Gdx.files.internal("eatSound.wav"));
        hitSound = Gdx.audio.newSound(Gdx.files.internal("hit.wav"));
        tailBiteSound = Gdx.audio.newSound(Gdx.files.internal("tailBite.wav"));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, CAMWIDTH,  CAMHEIGHT);
        sounds = new ArrayList<>(0);
        sounds.add(Gdx.audio.newSound(Gdx.files.internal("a.wav")));
        sounds.add(Gdx.audio.newSound(Gdx.files.internal("b.wav")));
        sounds.add(Gdx.audio.newSound(Gdx.files.internal("c.wav")));
        sounds.add(Gdx.audio.newSound(Gdx.files.internal("d.wav")));
        sounds.add(Gdx.audio.newSound(Gdx.files.internal("e.wav")));
        sounds.add(Gdx.audio.newSound(Gdx.files.internal("f.wav")));
        sounds.add(Gdx.audio.newSound(Gdx.files.internal("g.wav")));
        sounds.add(Gdx.audio.newSound(Gdx.files.internal("h.wav")));
        soundIndex = 0;
    }

    public boolean isAlive() { return alive; }

    public void accelerate(float t)
    {
        if (movePeriod - t < 0.05)
            return;
        movePeriod -= t;
    }

    public boolean hitsStone(Stone stone)
    {
        Rectangle a = new Rectangle();
        a.setSize(SEGSIZE);
        a.setPosition(head.x, head.y);
        Rectangle b = new Rectangle();
        b.setSize(SEGSIZE);
        b.setPosition(stone.x, stone.y);
        return a.overlaps(b);
    }

    public void playHitSound() {
        hitSound.play();
    }

    public boolean outOfScreen(int width, int height)
    {
        if (x + SEGSIZE >= width)
            return true;
        if (x < 0)
            return true;
        if (y >= height)
            return true;
        return y + SEGSIZE <= 0;
    }

    public void move()
    {
        head.move();
        tail.move();
        this.x = head.x;
        this.y = head.y;
    }

    public void die() {
        alive = false;
    }

    public void update()
    {
        if (!alive || pause)
            return;
        keyPresProcessing();
        this.x = head.x;
        this.y = head.y;
        time += Gdx.graphics.getDeltaTime();
        if (movePeriod <= time) {
            time = 0;
            move();
        }
        if (tailIsEaten()) {
            tailBiteSound.play();
            alive = false;
        }
    }

    public void keyPresProcessing()
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT))
            this.rotate(Snake.RIGHT);
        else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT))
            this.rotate(Snake.LEFT);
        else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN))
            this.rotate(Snake.DOWN);
        else if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
            this.rotate(Snake.UP);
        if (Gdx.input.isKeyJustPressed(Input.Keys.D))
            this.rotate(Snake.RIGHT);
        else if (Gdx.input.isKeyJustPressed(Input.Keys.A))
            this.rotate(Snake.LEFT);
        else if (Gdx.input.isKeyJustPressed(Input.Keys.S))
            this.rotate(Snake.DOWN);
        else if (Gdx.input.isKeyJustPressed(Input.Keys.W))
            this.rotate(Snake.UP);
    }

    public void draw()
    {
        head.draw();
        tail.draw();
    }

    void pause() {
        pause = !pause;
    }

    private boolean tailIsEaten()
    {
        Rectangle a = new Rectangle();
        a.setSize(SEGSIZE);
        a.setPosition(head.x, head.y);
        for (int i = 0; i < tail.tailParts.length; i++) {
            Rectangle b = new Rectangle();
            b.setSize(SEGSIZE);
            b.setPosition(tail.tailParts[i].x, tail.tailParts[i].y);
            if (a.overlaps(b))
                return true;
        }
        Rectangle b = new Rectangle();
        b.setSize(SEGSIZE);
        b.setPosition(tail.tailEnd.x, tail.tailEnd.y);
        return a.overlaps(b);
    }

    public void rotate(int direction)
    {
        if (abs(this.direction - direction) == 2 || this.direction == direction)
            return;
        sounds.get(soundIndex).play();
        soundIndex = (soundIndex + 1) % sounds.size();
        move();
        switch (direction) {
            case RIGHT:
                if (this.direction == UP)
                    tail.rotate("LU");
                else if (this.direction == DOWN)
                    tail.rotate("LD");
                head.setRight();
                head.x = tail.x + SEGSIZE;
                head.y = tail.y;
                break;
            case LEFT:
                if (this.direction == UP)
                    tail.rotate("RU");
                else if (this.direction == DOWN)
                    tail.rotate("RD");
                head.setLeft();
                head.x = tail.x - SEGSIZE;
                head.y = tail.y;
                break;
            case UP:
                if (this.direction == RIGHT)
                    tail.rotate("RD");
                else if (this.direction == LEFT)
                    tail.rotate("LD");
                head.setUp();
                head.x = tail.x;
                head.y = tail.y + SEGSIZE;
                break;
            case DOWN:
                if (this.direction == RIGHT)
                    tail.rotate("RU");
                else if (this.direction == LEFT)
                    tail.rotate("LU");
                head.setDown();
                head.x = tail.x;
                head.y = tail.y - SEGSIZE;
                break;
        }
        this.direction = direction;
        this.x = head.x;
        this.y = head.y;
    }

    public boolean ableToEat(Apple apple)
    {
        Rectangle rH = new Rectangle();
        rH.setSize(SEGSIZE);
        rH.setPosition(head.x, head.y);
        Rectangle rA = new Rectangle();
        rA.setSize(SEGSIZE);
        rA.setPosition(apple.x, apple.y);
        return rH.overlaps(rA) || !isInRightPos(apple);
    }

    private void grow()
    {
        TailPart[] arr = new TailPart[length + 1];
        for (int i = 0; i < length; i++)
            arr[i] = tail.tailParts[i];
        arr[length] = new TailPart(tail.tailEnd.x, tail.tailEnd.y);
        switch (tail.tailEnd.direction) {
            case RIGHT:
                tail.tailEnd.x += SEGSIZE;
                break;
            case UP:
                tail.tailEnd.y += SEGSIZE;
                arr[length].setVertical();
                break;
            case LEFT:
                tail.tailEnd.x -= SEGSIZE;
                break;
            case DOWN:
                tail.tailEnd.y -= SEGSIZE;
                arr[length].setVertical();
                break;
        }
        length++;
        tail.tailParts = arr;
    }

    public boolean stoneIsInRightPos(Stone stone)
    {
        for (TailPart tp : tail.tailParts)
        {
            Rectangle a = new Rectangle();
            a.setSize(SEGSIZE);
            a.setPosition(stone.x, stone.y);
            Rectangle b = new Rectangle();
            b.setSize(SEGSIZE);
            b.setPosition(tp.x, tp.y);
            if (a.overlaps(b))
                return false;
        }
        return true;
    }

    private boolean isInRightPos(Apple apple)
    {
        Rectangle b = new Rectangle();
        b.setSize(SEGSIZE);
        b.setPosition(apple.x, apple.y);
        for (int i = 0; i < tail.tailParts.length; i++) {
            Rectangle a = new Rectangle();
            a.setSize(SEGSIZE);
            a.setPosition(tail.tailParts[i].x, tail.tailParts[i].y);
            if (a.overlaps(b))
                return false;
        }
        return true;
    }

    public void eat(Apple apple)
    {
        apple.changePosition();
        while (!isInRightPos(apple))
            apple.changePosition();
        eatSound.play();
        grow();
    }

    public boolean inMotion() {
        return !pause;
    }

    public void dispose()
    {
        tail.dispose();
        head.dispose();
    }
}
