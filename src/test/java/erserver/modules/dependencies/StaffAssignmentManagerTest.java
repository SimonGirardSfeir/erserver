package erserver.modules.dependencies;

import org.junit.jupiter.api.Test;

import java.util.List;
class StaffAssignmentManagerTest {

    @Test
    void physiciansAndResidentsReturnForPhysiciansOnDuty() {
        StaffAssignmentManager manager = new StaffAssignmentManager(null, null);
        List<Staff> physiciansOnDuty = manager.getPhysiciansOnDuty();
    }

}