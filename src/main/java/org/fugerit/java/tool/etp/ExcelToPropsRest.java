package org.fugerit.java.tool.etp;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.tool.RestHelper;
import org.fugerit.java.tool.util.HelperSortedProperties;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringWriter;
import java.util.Iterator;

@Slf4j
@ApplicationScoped
@Path(  "/excel_to_props")
public class ExcelToPropsRest {

    private ETPOutput convertWorker( ETPInput input ) {
        ETPOutput output = new ETPOutput();
        SafeFunction.apply( () -> {
            FileUpload file = input.getFile();
            File tempFile = file.uploadedFile().toFile();
            HelperSortedProperties props = new HelperSortedProperties();
            try ( FileInputStream fis = new FileInputStream( tempFile);
                  Workbook workbook = new XSSFWorkbook( fis ) ) {
                Sheet sheet = workbook.getSheetAt( input.getSheetIndex() );
                Iterator<Row> rows = sheet.rowIterator();
                int count = 0;
                while ( rows.hasNext() ) {
                    Row row = rows.next();
                    if ( count >= input.getSkipHeaderLines() ) {
                        Cell cellKey = row.getCell( input.getKeyColumnIndex() );
                        Cell cellValue = row.getCell( input.getValueColumnIndex() );
                        props.put( cellKey.getStringCellValue(), cellValue.getStringCellValue() );
                    }
                    count++;
                }
            }
            try (StringWriter writer = new StringWriter() ) {
                props.store( writer, "Converted from excel" );
                log.info( "props : {}", props );
                output.setDocOutput( writer.toString() );
            }
            output.setOutputMessage( "Conversion completed" );
        }, e -> output.setOutputMessage( String.format( "Error : %s", e.getMessage() ) ) );
        return output;
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/convert/{sheetIndex}/{keyColumnIndex}/{valueColumnIndex}/{skipHeaderLines}")
    public Response convert(ETPInput input,
                            @PathParam( "sheetIndex" ) int sheetIndex,
                            @PathParam( "keyColumnIndex" ) int keyColumnIndex,
                            @PathParam( "valueColumnIndex" ) int valueColumnIndex,
                            @PathParam( "skipHeaderLines" ) int skipHeaderLines ) {
        return RestHelper.defaultHandle( () -> {
            input.setSheetIndex( sheetIndex );
            input.setKeyColumnIndex( keyColumnIndex );
            input.setValueColumnIndex( valueColumnIndex );
            input.setSkipHeaderLines( skipHeaderLines );
            log.info( "input : {}", input );
            ETPOutput output = this.convertWorker( input );
            return Response.ok().entity( output ).build();
        } );
    }

}
