package io.iamkyu.chapter4;

import io.iamkyu.annotation.GuardedBy;
import io.iamkyu.annotation.ThreadSafe;

/**
 * 값 변경이 가능하고 스레드 안정성도 확보한 클래스
 */
@ThreadSafe
public class SafePoint {
    @GuardedBy("this") private int x;
    @GuardedBy("this") private int y;

    private SafePoint(int[] a) {
        this(a[0], a[1]);
    }

    public SafePoint(SafePoint point) {
        this(point.get());
    }

    public SafePoint(int x, int y) {
        this.set(x, y);
    }

    public synchronized int[] get() {
        return new int[]{x, y};
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
