package io.iamkyu.chapter4;

import io.iamkyu.annotation.ThreadSafe;

import java.util.Collections;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 스레드 안정성을 ConcurrentMap 에 위임
 */
@ThreadSafe
public class DelegatingVehicleTracker {
    private final ConcurrentMap<String, String> locations;
    private final Map<String, String> unmodifiableMap;

    public DelegatingVehicleTracker(Map<String, String> points) {
        locations = new ConcurrentHashMap<>();
        unmodifiableMap = Collections.unmodifiableMap(locations);
    }

    public Map<String, String> getLocations() {
        return unmodifiableMap;
    }

    public String getLocation(String id) {
        return locations.get(id);
    }

    public void setLocations(String id, String point) {
        if (locations.replace(id, point) == null)
            throw new IllegalArgumentException();
    }


    Vector vec = new Vector();
    // ConcurrentHashMap
    CopyOnWriteArrayList
}
