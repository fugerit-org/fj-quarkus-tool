package test.org.fugerit.java.tool;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import org.fugerit.java.tool.RestHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

@QuarkusTest
class TestRestHelper {

	@Test
	void test500() {
		Assertions.assertEquals( Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
				RestHelper.defaultHandle( () -> { throw new IOException( "scenario exception" ); } ).getStatus() );
	}
	
	@Test
	void test400() {
		Assertions.assertEquals( Response.Status.BAD_REQUEST.getStatusCode(),
				RestHelper.defaultHandle( () -> null ).getStatus() );
	}
	
}
