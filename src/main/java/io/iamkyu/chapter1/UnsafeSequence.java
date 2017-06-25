package io.iamkyu.chapter1;

import io.iamkyu.annotation.GuardedBy;
import io.iamkyu.annotation.NotThreadSafe;
import io.iamkyu.annotation.ThreadSafe;

/**
 * 스레드 안전성 예제
 *
 * @author Kj Nam
 * @since 2017-06-22
 */
@NotThreadSafe
public class UnsafeSequence {
    private int value;

    public int getNext() {

        /*
         * FIXME
         * 1) 값을 읽고
         * 2) 읽은 값에 1을 더하고
         * 3) 결과를 저장
         *
         * 타이밍이 좋지 않으면 이 세개의 연산 사이에
         * 두 개의 쓰레드가 무작위로 끼어들 수 있다.
         */
        return value++;
    }
}

@ThreadSafe
class Sequece {
    @GuardedBy("this") private int value;

    public synchronized int getNext() {
        return value++;
    }
}
