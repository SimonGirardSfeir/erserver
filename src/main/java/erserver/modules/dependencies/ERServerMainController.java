package erserver.modules.dependencies;

import java.util.Timer;
import java.util.TimerTask;

public class ERServerMainController {

   private static StaffAssignmentManager staffAssignmentManager;
   private static InboundPatientSource inboundPatientController;
   private static AlertScanner alertScanner;

   static {
      staffAssignmentManager = new StaffAssignmentManager();
      EmergencyResponseService emergencyTransportService = new EmergencyResponseService("http://localhost", 4567, 1000);
      inboundPatientController = new InboundPatientController(emergencyTransportService);
      alertScanner = new AlertScanner(staffAssignmentManager, inboundPatientController);
      TimerTask alertTask = new TimerTask() {
         @Override
         public void run() {
            alertScanner.scan();
         }
      };
      Timer timer = new Timer();
      timer.schedule(alertTask, 1000, 30000);
   }

   public InboundPatientSource getInboundPatientController() {
      return inboundPatientController;
   }

   public StaffAssignmentManager getStaffAssignmentManager() {
      return staffAssignmentManager;
   }



}
