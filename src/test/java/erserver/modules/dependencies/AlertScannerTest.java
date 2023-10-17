package erserver.modules.dependencies;

import erserver.modules.testtypes.Patient;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AlertScannerTest {
    @Test
    void scanAlertsForPriorityRedPatients() {
        // Arrange
        InboundPatientTestDouble inboundPatientTestDouble = new InboundPatientTestDouble();
        Patient patient1 = new Patient(1, "John Doe",
                LocalDate.of(1970, 10, 16), Priority.RED, "stroke");
        Patient patient2 = new Patient(2, "Sam Doe",
                LocalDate.of(1980, 10, 16), Priority.YELLOW, "mild stroke");
        inboundPatientTestDouble.simulateNewInboundPatient(patient1);
        inboundPatientTestDouble.simulateNewInboundPatient(patient2);
        AlertTransmitterTestDouble alertTransmitterTestDouble = new AlertTransmitterTestDouble();
        AlertScanner alertScanner = new AlertScanner(new StaffAssignmentManager(), inboundPatientTestDouble, alertTransmitterTestDouble);
        // Act
        alertScanner.scan();

        //Assert
        assertThat(alertTransmitterTestDouble.getAlertsReceivedRequiringAck()).isEqualTo(List.of("111-111-1111: New inbound critical patient: 1"));
    }
    @Test
    void scanAlertsForPriorityYellowConditionHeartArrhythmiaPatients() {
        // Arrange
        InboundPatientTestDouble inboundPatientTestDouble = new InboundPatientTestDouble();
        Patient patient1 = new Patient(1, "John Doe",
                LocalDate.of(1975, 10, 16), Priority.GREEN, "shortness of breath");
        Patient patient2 = new Patient(2, "Sam Doe",
                LocalDate.of(1985, 10, 16), Priority.YELLOW, "heart arrhythmia");
        inboundPatientTestDouble.simulateNewInboundPatient(patient1);
        inboundPatientTestDouble.simulateNewInboundPatient(patient2);
        AlertTransmitterTestDouble alertTransmitterTestDouble = new AlertTransmitterTestDouble();
        AlertScanner alertScanner = new AlertScanner(new StaffAssignmentManager(), inboundPatientTestDouble, alertTransmitterTestDouble);

        // Act
        alertScanner.scan();

        //Assert
        assertThat(alertTransmitterTestDouble.getAlertsReceived()).isEqualTo(List.of("111-111-1111: New inbound critical patient: 2"));
    }

    @Test
    void onlyTransmitOnceForGivenInboundPatient() {
        // Arrange
        InboundPatientTestDouble inboundPatientTestDouble = new InboundPatientTestDouble();
        Patient patient1 = new Patient(1, "John Doe",
                LocalDate.of(1975, 10, 16), Priority.GREEN, "shortness of breath");
        Patient patient2 = new Patient(2, "Sam Doe",
                LocalDate.of(1985, 10, 16), Priority.YELLOW, "heart arrhythmia");
        inboundPatientTestDouble.simulateNewInboundPatient(patient1);
        inboundPatientTestDouble.simulateNewInboundPatient(patient2);
        AlertTransmitterTestDouble alertTransmitterTestDouble = new AlertTransmitterTestDouble();
        AlertScanner alertScanner = new AlertScanner(new StaffAssignmentManager(), inboundPatientTestDouble, alertTransmitterTestDouble);

        // Act
        alertScanner.scan();
        alertScanner.scan();

        //Assert
        assertThat(alertTransmitterTestDouble.getAlertsReceived()).isEqualTo(List.of("111-111-1111: New inbound critical patient: 2"));
    }

    private static class AlertTransmitterTestDouble implements AlertTransmitter {
        private final List<String> alertsReceived;
        private final List<String> alertsReceivedRequiringAck;

        public AlertTransmitterTestDouble() {
            this.alertsReceived = new ArrayList<>();
            this.alertsReceivedRequiringAck = new ArrayList<>();
        }
        @Override
        public void transmit(String targetDevice, String pageText) {
            alertsReceived.add(targetDevice + ": " + pageText);

        }
        @Override
        public void transmitRequiringAcknowledgement(String targetDevice, String pageText) {
            alertsReceivedRequiringAck.add(targetDevice + ": " + pageText);
        }
        public List<String> getAlertsReceivedRequiringAck() {
            return alertsReceivedRequiringAck;
        }
        public List<String> getAlertsReceived() {
            return alertsReceived;
        }
    }
    private static class InboundPatientTestDouble implements InboundPatientSource {
        private final List<Patient> inbounds;

        public InboundPatientTestDouble() {
            this.inbounds = new ArrayList<>();
        }
        public void simulateNewInboundPatient(Patient patient) {
            inbounds.add(patient);
        }
        @Override
        public List<Patient> currentInboundPatients() {
            return inbounds;
        }
        @Override
        public void informOfPatientArrival(int transportId) {

        }
    }

}