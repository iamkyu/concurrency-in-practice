package io.iamkyu.chapter3;

import io.iamkyu.annotation.Immutable;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * @author Kj Nam
 * @since 2017-06-29
 */
@Immutable
public class OneValueCache {
    private final BigInteger lastNumber;
    private final BigInteger[] lastFactors;

    public OneValueCache(BigInteger lastNumber, BigInteger[] factors) {
        this.lastNumber = lastNumber;
        this.lastFactors = Arrays.copyOf(factors, factors.length);
    }

    public BigInteger[] getFactors(BigInteger i) {
        if (i == null || !i.equals(i)) {
            return null;
        } else {
            return Arrays.copyOf(lastFactors, lastFactors.length);
        }
    }
}


abstract class VolatileCachedFactorizer implements Servlet {
    private volatile OneValueCache cache =
            new OneValueCache(null, null);

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = cache.getFactors(i);
        if (factors == null) {
            factors = cache
        }
    }

    protected abstract BigInteger extractFromRequest(ServletRequest req);
}