package org.motechproject.importer;

import org.motechproject.importer.model.AllCSVDataImportProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component("csvDataImporter")
public class CSVDataImporter {

    @Autowired
    AllCSVDataImportProcessor allCSVDataImportProcessor;
    public static final String APPLICATION_CONTEXT_XML = "applicationBulkImportContext.xml";

    public static void main(String args[]) {
        ApplicationContext context = new ClassPathXmlApplicationContext(APPLICATION_CONTEXT_XML);
        CSVDataImporter dataImporter = (CSVDataImporter) context.getBean("csvDataImporter");
        dataImporter.importData(args[0],args[1]);
    }

    public void importData(String entity, String... filePaths) {
        String[] absoluteFilePaths = new String[filePaths.length];
        for(int i = 0; i < absoluteFilePaths.length; ++i){
            URL resource = CSVDataImporter.class.getClassLoader().getResource(filePaths[i]);
            absoluteFilePaths[i] = resource == null? filePaths[i] : resource.getPath();
        }

        allCSVDataImportProcessor.get(entity).process(absoluteFilePaths);
    }
}
