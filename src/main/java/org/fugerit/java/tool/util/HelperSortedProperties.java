package org.fugerit.java.tool.util;

import lombok.Getter;
import org.fugerit.java.core.util.CheckDuplicationProperties;

import java.util.*;

public class HelperSortedProperties extends CheckDuplicationProperties {

    @Override
    public Set<Map.Entry<Object, Object>> entrySet() {
        LinkedHashSet<Map.Entry<Object, Object>> entries = new LinkedHashSet<>();
        this.getSortedKeys().forEach( k -> entries.add( new AbstractMap.SimpleEntry<>( k, this.get( k ) ) ) );
        return entries;
    }

    @Override
    public synchronized Object put(Object key, Object value) {
        this.sortedKeys.add( (String) key );
        return super.put(key, value);
    }

    public HelperSortedProperties() {
        this.sortedKeys = new LinkedHashSet<>();
    }

    @Getter
    private Collection<String> sortedKeys;

}