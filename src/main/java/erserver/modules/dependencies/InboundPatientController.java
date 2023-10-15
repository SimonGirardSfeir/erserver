package erserver.modules.dependencies;

import erserver.modules.testtypes.Patient;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class InboundPatientController {

   private EmergencyResponseService transportService;

   public InboundPatientController(EmergencyResponseService transportService) {
      this.transportService = transportService;
   }

   public List<Patient> currentInboundPatients() {
      String xmlForInbound = transportService.fetchInboundPatients();
      System.out.println("Recieved XML from transport service: \n" + xmlForInbound);
      List<Patient> patients = buildPatientsFromXml(xmlForInbound);
      System.out.println("Returning inbound patients: " + patients.size());
      return patients;
   }

   static List<Patient> buildPatientsFromXml(String xmlForInbound) {
      ArrayList<Patient> patients = new ArrayList<>();
      SAXBuilder builder = new SAXBuilder();
      try {
         InputStream stream = new ByteArrayInputStream(xmlForInbound.getBytes(StandardCharsets.UTF_8));
         Document document = builder.build(stream);
         Element rootNode = document.getRootElement();
         List<Element> list = rootNode.getChildren("Patient");
          for (Element node : list) {
              Patient patient = new Patient();
              patient.setTransportId(Integer.parseInt(node.getChildText("TransportId")));
              patient.setName(node.getChildText("Name"));
              patient.setCondition(node.getChildText("Condition"));
              patient.setPriority(Priority.getByString(node.getChildText("Priority")));
              patients.add(patient);
          }
      } catch (IOException | JDOMException io) {
         System.out.println(io.getMessage());
      }
       return patients;
   }

   public void informOfPatientArrival(int transportId) {
      transportService.informOfArrival(transportId);
   }

}
