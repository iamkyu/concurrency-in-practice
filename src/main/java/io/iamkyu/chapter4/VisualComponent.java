package io.iamkyu.chapter4;

import io.iamkyu.annotation.ThreadSafe;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 두 개의 독립적인 변수에 스레드 안정성 위임
 */
@ThreadSafe
public class VisualComponent {
    private final List<KeyListener> keyListeners = new CopyOnWriteArrayList<>();
    private final List<MouseListener> mouseListeners = new CopyOnWriteArrayList<>();

    public void addKeyListener(KeyListener listener) {
        keyListeners.add(listener);
    }

    public void addMouseListener(MouseListener listener) {
        mouseListeners.add(listener);
    }

    public void removeKeyListener(KeyListener listener) {
        keyListeners.remove(listener);
    }

    public void removeMouseListener(MouseListener listener) {
        mouseListeners.remove(listener);
    }
}
