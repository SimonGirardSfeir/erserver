package erserver.modules.dependencies;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StaffAssignmentManagerTest {

    @Test
    void physiciansAndResidentsReturnForPhysiciansOnDuty() {
        // Arrange
        StaffProviderDouble staffProviderDouble = new StaffProviderDouble();
        staffProviderDouble.addStaffMemberToReturn(new Staff(1, "John Doctor", StaffRole.DOCTOR));
        staffProviderDouble.addStaffMemberToReturn(new Staff(2, "Frank Resident", StaffRole.RESIDENT));
        staffProviderDouble.addStaffMemberToReturn(new Staff(3, "Mary Nurse", StaffRole.NURSE));
        StaffAssignmentManager manager = new StaffAssignmentManager(staffProviderDouble, new BedProviderDouble());
        // Act
        List<Staff> physiciansOnDuty = manager.getPhysiciansOnDuty();
        // Assert
        Staff expectedStaff1 = new Staff(1, "John Doctor", StaffRole.DOCTOR);
        Staff expectedStaff2 = new Staff(2, "Frank Resident", StaffRole.RESIDENT);
        assertThat(physiciansOnDuty).isEqualTo(List.of(expectedStaff1, expectedStaff2));
    }

    private static class StaffProviderDouble implements StaffProvider {
        private final List<Staff> staffToReturn;
        public StaffProviderDouble() {
            this.staffToReturn = new ArrayList<>();
        }
        public void addStaffMemberToReturn(Staff staff) {
            staffToReturn.add(staff);
        }
        @Override
        public List<Staff> getShiftStaff() {
            return staffToReturn;
        }
    }

    private static class BedProviderDouble implements BedProvider {
        @Override
        public List<Bed> getAllBeds() {
            return new ArrayList<>();
        }
    }
}