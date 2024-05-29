package org.fugerit.java.tool.etp;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.docx4j.openpackaging.contenttype.CTTypes;
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage;
import org.docx4j.openpackaging.parts.SpreadsheetML.WorkbookPart;
import org.docx4j.openpackaging.parts.SpreadsheetML.WorksheetPart;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.function.SimpleValue;
import org.fugerit.java.tool.RestHelper;
import org.fugerit.java.tool.util.HelperSortedProperties;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.xlsx4j.org.apache.poi.ss.usermodel.DataFormatter;
import org.xlsx4j.sml.Cell;
import org.xlsx4j.sml.SheetData;
import org.xlsx4j.sml.Worksheet;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

@Slf4j
@ApplicationScoped
@Path(  "/excel_to_props")
public class ExcelToPropsRest {

    public static ETPOutput convertWorker(InputStream excelStream, int sheetIndex, int keyColumnIndex, int valueColumnIndex, int skipHeaderLines) {
        ETPOutput output = new ETPOutput();
        log.info( "sheetIndex : {}, keyColumnIndex : {}, valueColumnIndex : {}, skipHeaderLines : {}", sheetIndex, keyColumnIndex, valueColumnIndex, skipHeaderLines );
        SafeFunction.apply( () -> {
            new CTTypes();
            HelperSortedProperties props = new HelperSortedProperties();
            SpreadsheetMLPackage spreadsheetMLPackage = SpreadsheetMLPackage.load( excelStream );
            WorkbookPart workbookPart = spreadsheetMLPackage.getWorkbookPart();
            WorksheetPart sheet = workbookPart.getWorksheet( sheetIndex );
            Worksheet worksheet = sheet.getJaxbElement();
            SheetData sheetData = worksheet.getSheetData();
            SimpleValue<Integer> count = new SimpleValue<>(0);
            DataFormatter formatter = new DataFormatter();
            sheetData.getRow().forEach( r -> {
                count.setValue( count.getValue()+1 );
                if ( count.getValue() > skipHeaderLines ) {
                    List<Cell> cells = r.getC();
                    Cell cellKey = cells.get( keyColumnIndex );
                    Cell cellValue = cells.get( valueColumnIndex );
                    props.put( formatter.formatCellValue( cellKey ), formatter.formatCellValue( cellValue ) );
                }
            } );
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
            FileUpload file = input.getFile();
            File tempFile = file.uploadedFile().toFile();
            try ( FileInputStream fis = new FileInputStream( tempFile ) ) {
                ETPOutput output = convertWorker( fis, sheetIndex, keyColumnIndex, valueColumnIndex, skipHeaderLines );
                return Response.ok().entity( output ).build();
            }
        } );
    }

}
