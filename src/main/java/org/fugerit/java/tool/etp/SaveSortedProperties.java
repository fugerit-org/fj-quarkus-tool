package org.fugerit.java.tool.etp;

import org.fugerit.java.tool.ptj.LoadSortedProperties;

import java.util.*;

public class SaveSortedProperties extends LoadSortedProperties {

    @Override
    public Set<Map.Entry<Object, Object>> entrySet() {
        LinkedHashSet<Map.Entry<Object, Object>> entries = new LinkedHashSet<>();
        this.getSortedKeys().forEach( k -> entries.add( new AbstractMap.SimpleEntry<>( k, this.get( k ) ) ) );
        return entries;
    }

}
