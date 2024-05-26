package test.org.fugerit.java.tool.util;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.tool.util.HelperSortedProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class TestHelperSortedProperties {

    @Test
    void test() {
        HelperSortedProperties props = new HelperSortedProperties();
        props.put( "key1", "value1" );
        props.entrySet().forEach( e -> log.info( "key : {}", e.getKey() ) );
        log.info( "sorted keys : {]", props.getSortedKeys() );
        Assertions.assertEquals( "value1", props.get( "key1" ) );
    }

}
