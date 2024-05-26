package test.org.fugerit.java.tool.etp;

import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class TestExcelToProps {

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
                .multiPart( "sheetIndex", "0" )
                .multiPart( "keyColumnIndex", "0" )
                .multiPart( "valueColumnIndex", "0" )
                .post( "/fj-quarkus-tool/api/excel_to_props/convert" )
                .then()
                .statusCode(200);
    }

}
