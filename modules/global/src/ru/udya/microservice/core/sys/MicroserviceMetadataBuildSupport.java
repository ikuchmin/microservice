package ru.udya.microservice.core.sys;

import com.haulmont.cuba.core.sys.MetadataBuildSupport;
import org.apache.commons.text.StringTokenizer;
import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MicroserviceMetadataBuildSupport extends MetadataBuildSupport {

    private static final Logger log = LoggerFactory.getLogger(MetadataBuildSupport.class);

    @Override
    public List<XmlFile> init() {
        List<XmlFile> metadataXmlList = new ArrayList<>();
        StringTokenizer metadataFilesTokenizer = new StringTokenizer(getMetadataConfig());
        for (String fileName : metadataFilesTokenizer.getTokenArray()) {
            try {
                metadataXmlList.add(new XmlFile(fileName, readXml(fileName)));

            } catch (IllegalStateException e) {
                // Skip errors that metadata.xml not found
                if (e.getMessage().equals("Unable to read resource: " + fileName)) {
                    log.warn("Metadata resource not found: " + fileName);
                }
            }
        }
        return metadataXmlList;
    }
}
