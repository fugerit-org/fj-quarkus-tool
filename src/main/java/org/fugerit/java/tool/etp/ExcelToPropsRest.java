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
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Iterator;

@Slf4j
@ApplicationScoped
@Path(  "/excel_to_props")
public class ExcelToPropsRest {

    public static ETPOutput convertWorker(InputStream excelStream, int sheetIndex, int keyColumnIndex, int valueColumnIndex, int skipHeaderLines) {
        ETPOutput output = new ETPOutput();
        log.info( "sheetIndex : {}, keyColumnIndex : {}, valueColumnIndex : {}, skipHeaderLines : {}", sheetIndex, keyColumnIndex, valueColumnIndex, skipHeaderLines );
        SafeFunction.apply( () -> {
            HelperSortedProperties props = new HelperSortedProperties();
            try ( Workbook workbook = new XSSFWorkbook( excelStream ) ) {
                Sheet sheet = workbook.getSheetAt( sheetIndex);
                Iterator<Row> rows = sheet.rowIterator();
                int count = 0;
                while ( rows.hasNext() ) {
                    Row row = rows.next();
                    if ( count >= skipHeaderLines ) {
                        Cell cellKey = row.getCell( keyColumnIndex );
                        Cell cellValue = row.getCell( valueColumnIndex );
                        props.put( cellKey.getStringCellValue(), cellValue.getStringCellValue() );
                    }
                    count++;
                }
            }
            try (StringWriter writer = new StringWriter() ) {
                props.store( writer,  "Converted from excel (https://docs.fugerit.org/fj-quarkus-tool/home/index.html)" );
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
            FileUpload file = input.getFile();
            File tempFile = file.uploadedFile().toFile();
            try ( FileInputStream fis = new FileInputStream( tempFile ) ) {
                ETPOutput output = convertWorker( fis, sheetIndex, keyColumnIndex, valueColumnIndex, skipHeaderLines );
                return Response.ok().entity( output ).build();
            }
        } );
    }

}
