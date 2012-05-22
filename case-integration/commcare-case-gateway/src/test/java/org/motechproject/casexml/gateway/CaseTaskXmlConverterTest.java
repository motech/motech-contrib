package org.motechproject.casexml.gateway;

import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.motechproject.casexml.domain.CaseTask;
import org.motechproject.util.DateUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CaseTaskXmlConverterTest {

    private final String ownerId = "1b23abcaa82aff8215a831";
    private final String caseType = "task";
    private final String pregnancyCaseType = "cc_bihar_pregnancy";
    private final String childCaseType = "cc_bihar_newborn";

    @Test
    public void shouldConvertToCaseXmlWithEnvelopeCorrectlyForAMotherVaccination() throws ParserConfigurationException, IOException, SAXException {
        String motherCaseId = "3F2504E04F8911D39A0C0305E82C3301";
        String randomGeneratedCaseId = "3F2504E04F8911D39A0C0305E82C3300";
        String taskId = "tt2";
        String dateExpires = DateUtil.today().plusDays(5).toString();
        String dateEligible = DateUtil.today().toString();
        String currentTime = DateUtil.now().toDateTime(DateTimeZone.UTC).toString();
        String motechUserId = "motechId";
        CaseTask task = createCaseTask(randomGeneratedCaseId, motherCaseId, taskId, dateEligible,  dateExpires, currentTime, motechUserId,  pregnancyCaseType);

        String caseXmlWithEnvelope = new CaseTaskXmlConverter().convertToCaseXml(task);
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(caseXmlWithEnvelope)));


        assertEquals("data", (doc).getDocumentElement().getTagName());
        assertEquals("http://bihar.commcarehq.org/pregnancy/task", (doc).getDocumentElement().getAttribute("xmlns"));

        Element meta = (Element)doc.getElementsByTagName("meta").item(0);
        assertEquals("http://openrosa.org/jr/xforms", meta.getAttribute("xmlns"));
        assertEquals(currentTime , meta.getElementsByTagName("timeStart").item(0).getTextContent());
        assertEquals(currentTime, meta.getElementsByTagName("timeEnd").item(0).getTextContent());
        assertEquals(motechUserId, meta.getElementsByTagName("userID").item(0).getTextContent());
        assertNotNull(meta.getElementsByTagName("instanceID").item(0).getTextContent());

        Element caseEle = (Element)doc.getElementsByTagName("case").item(0);
        assertEquals(randomGeneratedCaseId, caseEle.getAttribute("case_id"));
        assertEquals(currentTime, caseEle.getAttribute("date_modified"));
        assertEquals(motechUserId, caseEle.getAttribute("user_id"));
        assertEquals("http://commcarehq.org/case/transaction/v2", caseEle.getAttribute("xmlns"));

        Element createEle = (Element)caseEle.getElementsByTagName("create").item(0);
        assertEquals(caseType, createEle.getElementsByTagName("case_type").item(0).getTextContent());
        assertEquals("caseName", createEle.getElementsByTagName("case_name").item(0).getTextContent());
        assertEquals(ownerId, createEle.getElementsByTagName("owner_id").item(0).getTextContent());

        Element updateEle = (Element)caseEle.getElementsByTagName("update").item(0);
        assertEquals(taskId, updateEle.getElementsByTagName("task_id").item(0).getTextContent());
        assertEquals(dateEligible, updateEle.getElementsByTagName("date_eligible").item(0).getTextContent());
        assertEquals(dateExpires, updateEle.getElementsByTagName("date_expires").item(0).getTextContent());

        Element indexEle = (Element)caseEle.getElementsByTagName("index").item(0);
        Element motherEle = (Element)indexEle.getElementsByTagName("person_id").item(0);
        assertEquals(motherCaseId, motherEle.getTextContent());
        assertEquals(pregnancyCaseType, motherEle.getAttribute("case_type"));
    }

    @Test
    public void shouldConvertToCaseXmlWithEnvelopeCorrectlyForAChildVaccination() throws ParserConfigurationException, IOException, SAXException {
        String childCaseId = "3F2504E04F8911D39A0C0305E82C3301";
        String randomGeneratedCaseId = "3F2504E04F8911D39A0C0305E82C3300";
        String taskId = "measles";
        String dateExpires = DateUtil.today().plusDays(5).toString();
        String dateEligible = DateUtil.today().toString();
        String currentTime = DateUtil.now().toDateTime(DateTimeZone.UTC).toString();
        String motechUserId = "motechId";
        CaseTask task = createCaseTask(randomGeneratedCaseId, childCaseId, taskId, dateEligible,  dateExpires, currentTime, motechUserId,  childCaseType);

        String caseXmlWithEnvelope = new CaseTaskXmlConverter().convertToCaseXml(task);

        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(caseXmlWithEnvelope)));


        assertEquals("data", (doc).getDocumentElement().getTagName());
        assertEquals("http://bihar.commcarehq.org/pregnancy/task", (doc).getDocumentElement().getAttribute("xmlns"));

        Element meta = (Element)doc.getElementsByTagName("meta").item(0);
        assertEquals("http://openrosa.org/jr/xforms", meta.getAttribute("xmlns"));
        assertEquals(currentTime , meta.getElementsByTagName("timeStart").item(0).getTextContent());
        assertEquals(currentTime, meta.getElementsByTagName("timeEnd").item(0).getTextContent());
        assertEquals(motechUserId, meta.getElementsByTagName("userID").item(0).getTextContent());
        assertNotNull(meta.getElementsByTagName("instanceID").item(0).getTextContent());

        Element caseEle = (Element)doc.getElementsByTagName("case").item(0);
        assertEquals(randomGeneratedCaseId, caseEle.getAttribute("case_id"));
        assertEquals(currentTime, caseEle.getAttribute("date_modified"));
        assertEquals(motechUserId, caseEle.getAttribute("user_id"));
        assertEquals("http://commcarehq.org/case/transaction/v2", caseEle.getAttribute("xmlns"));

        Element createEle = (Element)caseEle.getElementsByTagName("create").item(0);
        assertEquals(caseType, createEle.getElementsByTagName("case_type").item(0).getTextContent());
        assertEquals("caseName", createEle.getElementsByTagName("case_name").item(0).getTextContent());
        assertEquals(ownerId, createEle.getElementsByTagName("owner_id").item(0).getTextContent());

        Element updateEle = (Element)caseEle.getElementsByTagName("update").item(0);
        assertEquals(taskId, updateEle.getElementsByTagName("task_id").item(0).getTextContent());
        assertEquals(dateEligible, updateEle.getElementsByTagName("date_eligible").item(0).getTextContent());
        assertEquals(dateExpires, updateEle.getElementsByTagName("date_expires").item(0).getTextContent());

        Element indexEle = (Element)caseEle.getElementsByTagName("index").item(0);
        Element childEle = (Element)indexEle.getElementsByTagName("person_id").item(0);
        assertEquals(childCaseId, childEle.getTextContent());
        assertEquals(childCaseType, childEle.getAttribute("case_type"));
    }

    private CaseTask createCaseTask(String caseId, String motherCaseId, String taskId, String dateEligible, String dateExpires, String currentTime, String motechUserId, String clientCaseType) {
        CaseTask caseTask = new CaseTask();
        caseTask.setCaseId(caseId);
        caseTask.setCaseType("task");
        caseTask.setCaseName("caseName");
        caseTask.setOwnerId(ownerId);
        caseTask.setCurrentTime(currentTime);
        caseTask.setMotechUserId(motechUserId);
        caseTask.setTaskId(taskId);
        caseTask.setClientCaseId(motherCaseId);
        caseTask.setClientCaseType(clientCaseType);
        caseTask.setDateEligible(dateEligible);
        caseTask.setDateExpires(dateExpires);
        return caseTask;
    }

    @Test
    public void shouldConvertToCloseCaseXmlWithEnvelopeCorrectlyForAVaccination() throws ParserConfigurationException, IOException, SAXException {
        String childCaseId = "3F2504E04F8911D39A0C0305E82C3301";
        String randomGeneratedCaseId = "3F2504E04F8911D39A0C0305E82C3300";
        String taskId = "measles";
        String dateExpires = DateUtil.today().plusDays(5).toString();
        String dateEligible = DateUtil.today().toString();
        String currentTime = DateUtil.now().toDateTime(DateTimeZone.UTC).toString();
        String motechUserId = "motechId";
        CaseTask task = createCaseTask(randomGeneratedCaseId, childCaseId, taskId, dateEligible,  dateExpires, currentTime, motechUserId, childCaseType);
        String caseXmlWithEnvelope = new CaseTaskXmlConverter().convertToCloseCaseXml(task);

        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(caseXmlWithEnvelope)));


        assertEquals("data", (doc).getDocumentElement().getTagName());
        assertEquals("http://bihar.commcarehq.org/pregnancy/task", (doc).getDocumentElement().getAttribute("xmlns"));

        Element meta = (Element)doc.getElementsByTagName("meta").item(0);
        assertEquals("http://openrosa.org/jr/xforms", meta.getAttribute("xmlns"));
        assertEquals(currentTime , meta.getElementsByTagName("timeStart").item(0).getTextContent());
        assertEquals(currentTime, meta.getElementsByTagName("timeEnd").item(0).getTextContent());
        assertEquals(motechUserId, meta.getElementsByTagName("userID").item(0).getTextContent());
        assertNotNull(meta.getElementsByTagName("instanceID").item(0).getTextContent());

        Element caseEle = (Element)doc.getElementsByTagName("case").item(0);
        assertEquals(randomGeneratedCaseId, caseEle.getAttribute("case_id"));
        assertEquals(currentTime, caseEle.getAttribute("date_modified"));
        assertEquals(motechUserId, caseEle.getAttribute("user_id"));
        assertEquals("http://commcarehq.org/case/transaction/v2", caseEle.getAttribute("xmlns"));

        Element closeEle = (Element)caseEle.getElementsByTagName("close").item(0);
        assertNotNull(closeEle);
    }
}
