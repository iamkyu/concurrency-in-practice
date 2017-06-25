package io.iamkyu.chapter2;

import io.iamkyu.annotation.GuardedBy;
import io.iamkyu.annotation.NotThreadSafe;
import io.iamkyu.annotation.ThreadSafe;
import org.omg.CORBA.ServerRequest;

import javax.servlet.ServletResponse;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 경쟁조건 예제
 *
 * @author Kj Nam
 * @since 2017-06-25
 */
@NotThreadSafe
public abstract class UnsafeCachingFactorizer {
    private final AtomicReference<BigInteger> lastNumber = new AtomicReference<>();
    private final AtomicReference<BigInteger[]> lastFactors = new AtomicReference<>();

    public void service(ServerRequest req, ServletResponse resp) {
        BigInteger i = extractFromRequest(req);

        if (!(i.equals(lastNumber.get()))) {
            BigInteger[] factors = factor(i);

            /*
             * FIXME
             * 타이밍이 좋지 않다면 두 값을 모두 갱신하지 못하고 하나는 수정됐고
             * 하나는 수정되지 않은 상황이 발생할 수 있다
             */
            lastNumber.set(i);
            lastFactors.set(factors);
        }

        encodeIntoResponse(resp, lastFactors.get());
    }

    protected abstract BigInteger[] factor(BigInteger i);
    protected abstract void encodeIntoResponse(ServletResponse resp, BigInteger[] bigIntegers);
    protected abstract BigInteger extractFromRequest(ServerRequest req);

}

@ThreadSafe
abstract class SynchronizedFactorizer {
    @GuardedBy("this") private final AtomicReference<BigInteger> lastNumber = new AtomicReference<>();
    @GuardedBy("this") private final AtomicReference<BigInteger[]> lastFactors = new AtomicReference<>();

    /*
     * FIXME
     * synchronized 키워드를 통해 락을 걸 수 있지만
     * 이 경우 서블릿을 여러 클라이언트가 동시 사용할 수 없고,
     * 이 때문에 응답성이 떨어질 수 있다.
     */
    public synchronized void service(ServerRequest req, ServletResponse resp) {
        BigInteger i = extractFromRequest(req);

        if (!(i.equals(lastNumber.get()))) {
            BigInteger[] factors = factor(i);

            /*
             * FIXME
             * 타이밍이 좋지 않다면 두 값을 모두 갱신하지 못하고 하나는 수정됐고
             * 하나는 수정되지 않은 상황이 발생할 수 있다
             */
            lastNumber.set(i);
            lastFactors.set(factors);
        }

        encodeIntoResponse(resp, lastFactors.get());
    }

    protected abstract BigInteger[] factor(BigInteger i);
    protected abstract void encodeIntoResponse(ServletResponse resp, BigInteger[] bigIntegers);
    protected abstract BigInteger extractFromRequest(ServerRequest req);
}

@ThreadSafe
abstract class CachedFactorizer {
    @GuardedBy("this") private BigInteger lastNumber;
    @GuardedBy("this") private BigInteger[] lastFactors;
    @GuardedBy("this") private long hits;
    @GuardedBy("this") private long cacheHits;

    public synchronized long getHits() { return  hits; }
    public synchronized double getCacheHitRatio() {
        return (double) cacheHits / (double) hits;
    }

    public void service(ServerRequest req, ServletResponse resp) {
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = null;

        synchronized (this) {
            if (i.equals(lastNumber)) {
                ++cacheHits;
                factors = lastFactors.clone();
            }
        }

        if (factors == null) {
            factors = factor(i);
            synchronized (this) {
                lastNumber = i;
                lastFactors = factors.clone();
            }
        }

        encodeIntoResponse(resp, factors.clone());
    }

    protected abstract BigInteger[] factor(BigInteger i);
    protected abstract void encodeIntoResponse(ServletResponse resp, BigInteger[] bigIntegers);
    protected abstract BigInteger extractFromRequest(ServerRequest req);
}
