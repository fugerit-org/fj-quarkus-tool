package org.fugerit.java.tool.ptj;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.util.MapEntry;
import org.fugerit.java.core.util.checkpoint.CheckpointUtils;
import org.fugerit.java.tool.RestHelper;
import org.fugerit.java.tool.util.HelperSortedProperties;

import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
@Path("/props_to_json")
public class PropsToJsonRest {

    public static final String FORMAT_PROPERTIES = "PROPERTIES";

    public static final String FORMAT_JSON1 = "JSON1";

    public static final String FORMAT_JSON2 = "JSON2";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private List<MapEntry<String,String>> convert1(final HelperSortedProperties current ) {
        return current.getSortedKeys().stream().map(
                k -> new MapEntry<>( k, current.getProperty( k ) )
        ).collect( Collectors.toList() );
    }

    private PTJOutput convert( PTJInput input ) {
        PTJOutput output = new PTJOutput();
        SafeFunction.apply( () -> {
            long time = System.currentTimeMillis();
            HelperSortedProperties props = new HelperSortedProperties();
            try (StringReader reader = new StringReader( input.getDocContent() )) {
                props.load( reader );
            }
            List<MapEntry<String,String>> entries = this.convert1( props );
            output.setDocOutput( MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString( entries ) );
            StringBuilder info = new StringBuilder();
            if ( !props.getDuplications().isEmpty() ) {
                info.append( "Duplicated entries found:\n" );
                props.getDuplications().forEach( e -> info.append( "key : "+e.getKey()+", value : "+e.getValue()+"\n" ) );
            }
            output.setOutputMessage( "Conversion completed!" );
            output.setInfo( info.toString() );
            output.setGenerationTime( CheckpointUtils.formatTimeDiffMillis( time , System.currentTimeMillis() ) );
        }, e -> output.setOutputMessage( String.format( "Conversion error : %s", e.toString() ) ) );
        return output;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/convert")
    public Response document(PTJInput input) {
        return RestHelper.defaultHandle( () -> Response.ok().entity( this.convert( input ) ).build() );
    }

}
