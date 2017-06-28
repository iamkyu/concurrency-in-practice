package io.iamkyu.chapter3;

import io.iamkyu.annotation.GuardedBy;
import io.iamkyu.annotation.NotThreadSafe;
import io.iamkyu.annotation.ThreadSafe;

/**
 * @author Kj Nam
 * @since 2017-06-28
 */
@NotThreadSafe
public class MutableInteger {

    /*
     * FIXME
     * 여러 스레드에서 동시에 접근하면 문제가 발생할 소지가 많다.
     * ex) 스테일(stale) 현상
     */
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

@ThreadSafe
class SynchronizedInteger {
    @GuardedBy("this") private int value;

    public synchronized int getValue() {
        return value;
    }

    public synchronized void setValue(int value) {
        this.value = value;
    }
}
