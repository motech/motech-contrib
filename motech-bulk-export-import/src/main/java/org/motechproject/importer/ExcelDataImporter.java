package org.motechproject.importer;

import org.motechproject.importer.model.AllExcelDataImportProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component
public class ExcelDataImporter {

    @Autowired
    AllExcelDataImportProcessor allDataImportProcessor;
    public static final String APPLICATION_CONTEXT_XML = "applicationBulkExportContext.xml";

    public static void main(String args[]) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext(APPLICATION_CONTEXT_XML);
        ExcelDataImporter dataImporter = (ExcelDataImporter) context.getBean("excelDataImporter");
        dataImporter.importData(args[0],args[1]);
    }

    public void importData(String entity, String file) throws Exception {
        URL resource = ExcelDataImporter.class.getClassLoader().getResource(file);
        allDataImportProcessor.get(entity).process(resource == null? file : resource.getPath());
    }
}
