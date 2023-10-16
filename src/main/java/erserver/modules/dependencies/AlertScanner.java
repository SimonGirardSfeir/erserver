package erserver.modules.dependencies;

import erserver.modules.dependencies.vendorpagersystem.PagerTransport;
import erserver.modules.testtypes.Patient;
import erserver.modules.dependencies.vendorpagersystem.PagerSystem;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AlertScanner {

   private static final String ADMIN_ON_CALL_DEVICE = "111-111-1111";

   private final StaffAssignmentManager staffAssignmentManager;
   private final InboundPatientSource inboundPatientController;
   private final Set<Integer> criticalPatientNotificationsSent;

   public AlertScanner(StaffAssignmentManager staffAssignmentManager,
                       InboundPatientSource inboundPatientController) {
      this.staffAssignmentManager = staffAssignmentManager;
      this.inboundPatientController = inboundPatientController;
      criticalPatientNotificationsSent = new HashSet<>();
   }

   public void scan() {
      System.out.println("Scanning for situations requiring alerting...");
      List<Patient> inbound = inboundPatientController.currentInboundPatients();
      for (Patient patient : inbound) {
         if (shouldAlertForRedPriority(patient) || shouldAlertForYellowPriorityHeartArrhythmia(patient)) {
            alertForNewCriticalPatient(patient);
         }
      }
   }

   private boolean shouldAlertForRedPriority(Patient patient) {
      return Priority.RED == patient.getPriority() &&
              !criticalPatientNotificationsSent.contains(patient.getTransportId());
   }

   private boolean shouldAlertForYellowPriorityHeartArrhythmia(Patient patient) {
      return Priority.YELLOW == patient.getPriority() &&
              "heart arrhythmia".equalsIgnoreCase(patient.getCondition()) &&
              !criticalPatientNotificationsSent.contains(patient.getTransportId());
   }

   protected void alertForNewCriticalPatient(Patient patient) {
      try {
         PagerTransport transport = PagerSystem.getTransport();
         transport.initialize();
         transport.transmitRequiringAcknowledgement(ADMIN_ON_CALL_DEVICE, "New inbound critical patient: " +
            patient.getTransportId());
         criticalPatientNotificationsSent.add(patient.getTransportId());
      } catch (RuntimeException e) {
         System.out.println("Failed attempt to use pager system to device " + ADMIN_ON_CALL_DEVICE);
      }
   }

}
