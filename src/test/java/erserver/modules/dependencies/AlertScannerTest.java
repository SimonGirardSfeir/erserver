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
        AlertScannerTestingSubclass scanner = new AlertScannerTestingSubclass(new StaffAssignmentManager(), inboundPatientTestDouble);

        // Act
        scanner.scan();

        //Assert
        assertThat(scanner.patientsAlertedFor).isEqualTo(List.of(patient1))
                .doesNotContain(patient2);
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
        AlertScannerTestingSubclass scanner = new AlertScannerTestingSubclass(new StaffAssignmentManager(), inboundPatientTestDouble);

        // Act
        scanner.scan();

        //Assert
        assertThat(scanner.patientsAlertedFor).isEqualTo(List.of(patient2))
                .doesNotContain(patient1);
    }

    private static class AlertScannerTestingSubclass extends AlertScanner {
        public final List<Patient> patientsAlertedFor;
        public AlertScannerTestingSubclass(StaffAssignmentManager staffAssignmentManager, InboundPatientSource inboundPatientController) {
            super(staffAssignmentManager, inboundPatientController);
            patientsAlertedFor = new ArrayList<>();
        }

        @Override
        protected void alertForNewCriticalPatient(Patient patient) {
            patientsAlertedFor.add(patient);
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