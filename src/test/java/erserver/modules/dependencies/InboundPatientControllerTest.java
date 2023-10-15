package erserver.modules.dependencies;

import erserver.modules.testtypes.Patient;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class InboundPatientControllerTest {

    @Test
    void testInboundXmlConversion() {
        //Given
        String xmlForScenario = """
                <Inbound>
                \t<Patient>
                \t\t<TransportId>1</TransportId>
                \t\t<Name>John Doe</Name>
                \t\t<Condition>heart arrhythmia</Condition>
                \t\t<Priority>YELLOW</Priority>
                \t\t<Birthdate></Birthdate>
                \t</Patient>
                </Inbound>""";
        //When
        List<Patient> patients =  InboundPatientController.buildPatientsFromXml(xmlForScenario);
        //Then
        Patient expectedPatient = new Patient();
        expectedPatient.setName("John Doe");
        expectedPatient.setPriority(Priority.YELLOW);
        expectedPatient.setCondition("heart arrhythmia");
        expectedPatient.setTransportId(1);
        List<Patient> expectedPatients = List.of(expectedPatient);
        assertThat(patients).isEqualTo(expectedPatients);
    }

}