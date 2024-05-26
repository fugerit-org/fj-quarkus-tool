package org.fugerit.java.tool.ptj;

import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class PTJInput {

    @Getter @Setter
    @Schema( description = "Input format, PROPERTIES or JSON", example = "PROPERTIES")
    private String inputFormat;

    @Getter @Setter
    @Schema( description = "Input format, PROPERTIES or JSON", example = "JSON")
    private String outputFormat;

    @Getter @Setter
    @Schema( description = "If true will check for duplicated properties", example = "true")
    private Boolean checkDuplication;

    @Getter @Setter
    @Schema( description = "The content to be converted", example = "key.1= value 1\nkey.2= value 2")
    private String docContent;

}
