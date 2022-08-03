package com.snake.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.Stack;

public class WindowManager
{
    private Stack<Window> windows;

    WindowManager() { windows = new Stack<Window>(); }

    public void push(Window window) { windows.push(window); }
    public void pop() { windows.pop().dispose(); }
    public void render(SpriteBatch batch) { windows.peek().render(batch); }
    public void update(float deltaTime) { windows.peek().update(deltaTime); }
    public void clear() {
        while (!windows.empty())
            pop();
    }
}
