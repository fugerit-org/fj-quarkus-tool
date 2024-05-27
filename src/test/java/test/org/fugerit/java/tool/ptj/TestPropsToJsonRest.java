package test.org.fugerit.java.tool.ptj;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.tool.ptj.PTJInput;
import org.fugerit.java.tool.ptj.PTJOutput;
import org.fugerit.java.tool.ptj.PropsToJsonRest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.given;

@Slf4j
@QuarkusTest
class TestPropsToJsonRest {

    private static final ObjectMapper MAPPER = new ObjectMapper().configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private byte[] getInput( String path ) {
        return SafeFunction.get( () ->  {
            String json = StreamIO.readString( ClassHelper.loadFromDefaultClassLoader( "ptj/"+path ) );
            log.info( "payload : {}", json );
            return json.getBytes();
        } );
    }

    private void testWorker( String apiPath, String jsonPayloadPath ) {
        given()
                .when()
                .accept( MediaType.APPLICATION_JSON )
                .contentType( MediaType.APPLICATION_JSON )
                .body( getInput( jsonPayloadPath ) )
                .post( apiPath )
                .then()
                .statusCode(200);
    }

    @Test
    void testConvert() {
        int[] testId = { 1 };
        for ( int k=0; k<testId.length; k++ ) {
            this.testWorker( "/fj-quarkus-tool/api/props_to_json/convert", "200_"+testId[0]+".json" );
        }
    }

    @Test
    void testConvertFacade() {
        SafeFunction.apply( () -> {
            int[] testId = { 1 };
            for ( int k=0; k<testId.length; k++ ) {
                byte[] data = getInput( "200_"+testId[0]+".json" );
                PTJInput input = MAPPER.readValue( data, PTJInput.class );
                PTJOutput output = PropsToJsonRest.convert( input );
                Assertions.assertNotNull( output );
            }
        } );
    }

}
