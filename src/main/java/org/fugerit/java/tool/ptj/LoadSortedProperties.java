package org.fugerit.java.tool.ptj;

import lombok.Getter;
import org.fugerit.java.core.util.CheckDuplicationProperties;

import java.util.Collection;
import java.util.LinkedHashSet;

public class LoadSortedProperties extends CheckDuplicationProperties {

    @Override
    public synchronized Object put(Object key, Object value) {
        this.sortedKeys.add( (String) key );
        return super.put(key, value);
    }

    public LoadSortedProperties() {
        this.sortedKeys = new LinkedHashSet<>();
    }

    @Getter
    private Collection<String> sortedKeys;

}