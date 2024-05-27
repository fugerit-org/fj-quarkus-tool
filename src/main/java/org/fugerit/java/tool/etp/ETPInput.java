package org.fugerit.java.tool.etp;

import jakarta.ws.rs.core.MediaType;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

public class ETPInput {

    @RestForm
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    @Schema( description = "File to upload")
    @Getter @Setter
    private FileUpload file;
    
}
