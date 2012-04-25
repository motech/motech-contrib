package org.motechproject.casexml.service.response.converter;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.motechproject.casexml.service.exception.CaseException;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ExceptionConverter implements Converter{

    @Override
    public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext marshallingContext) {
        CaseException exception = (CaseException) o;
        Map<String,String> errorMessages = exception.getErrorMessages();
        if(errorMessages != null){
            Set<String> errorKeys = errorMessages.keySet();

            Iterator<String> iterator = errorKeys.iterator();
            while(iterator.hasNext()){
                String nextKey = iterator.next();
                writeErrorNode(writer, nextKey, errorMessages.get(nextKey),marshallingContext);
            }
        }
    }

    private void writeErrorNode(HierarchicalStreamWriter writer, String key, String value, MarshallingContext marshallingContext) {
       writer.startNode("error");
        writeNode(writer, "code", key);
        writeNode(writer, "message", value);
        writer.endNode();
    }

    private void writeNode(HierarchicalStreamWriter writer,String nodeName, String value) {
        writer.startNode(nodeName);
        writer.setValue(value);
        writer.endNode();
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader hierarchicalStreamReader, UnmarshallingContext unmarshallingContext) {
        return null;
    }

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(CaseException.class) || aClass.getSuperclass().equals(CaseException.class);
    }
}