package org.fugerit.java.tool.etp;

import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

@ToString
public class ETPInput {

    @RestForm
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    @Schema( description = "File to upload")
    @Getter @Setter
    private FileUpload file;

    @Schema( description = "Sheet index, first sheet is 0", example = "0" )
    @Getter @Setter
    private int sheetIndex;

    @Schema( description = "Key column index, first column is 0", example = "0" )
    @Getter @Setter
    private int keyColumnIndex;

    @Schema( description = "Value column index, first column is 0", example = "0" )
    @Getter @Setter
    private int valueColumnIndex;

    @Schema( description = "Number of header lines to skip", example = "1" )
    @Getter @Setter
    private int skipHeaderLines;

}
