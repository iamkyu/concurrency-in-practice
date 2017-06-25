package io.iamkyu.chapter2;

import io.iamkyu.annotation.NotThreadSafe;

/**
 * 경쟁조건 예제
 *
 * @author Kj Nam
 * @since 2017-06-25
 */
@NotThreadSafe
public class LazyInitRace {
    private Object instance = null;

    public Object getInstance() {

        /*
         * FIXME
         * 경쟁 조건 때문에 제대로 동작하지 않을 가능성이 있다.
         * 스레드 A와 스레드 B가 각각 동시에 instance 를 살펴봤을 때 null 이면
         * 두 스레드가 서로 다른 인스턴스를 가져갈 수 있다.
         */
        if (instance == null)
            instance = new Object();

        return instance;
    }
}
