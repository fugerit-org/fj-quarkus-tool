package test.org.fugerit.java.tool.etp;

import io.quarkus.test.junit.QuarkusTest;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class TestExcelToProps {

    private byte[] getInput( String path ) {
        return SafeFunction.get( () ->  {
            String json = StreamIO.readString( ClassHelper.loadFromDefaultClassLoader( "etp/"+path ) );
            return json.getBytes();
        } );
    }

    @Test
    void testConvert200() {
        given()
                .when()
                .multiPart( "file", getInput( "test.xlsx" ) )
                .post( "/fj-quarkus-tool/api/excel_to_props/convert/0/0/0/1" )
                .then()
                .statusCode(200);
    }

}
