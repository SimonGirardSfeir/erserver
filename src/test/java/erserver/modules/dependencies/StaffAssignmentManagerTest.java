package erserver.modules.dependencies;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StaffAssignmentManagerTest {
    @Mock
    StaffProvider staffProvider;
    @Mock
    BedProvider bedProvider;

    StaffAssignmentManager staffAssignmentManager;

    @Test
    void physiciansAndResidentsReturnForPhysiciansOnDuty() {
        // Arrange
        Staff staff1 = new Staff(1, "John Doctor", StaffRole.DOCTOR);
        Staff staff2 = new Staff(2, "Frank Resident", StaffRole.RESIDENT);
        Staff staff3 = new Staff(3, "Mary Nurse", StaffRole.NURSE);
        when(staffProvider.getShiftStaff()).thenReturn(List.of(staff1, staff2, staff3));
        when(bedProvider.getAllBeds()).thenReturn(new ArrayList<>());
        staffAssignmentManager = new StaffAssignmentManager(staffProvider, bedProvider);
        // Act
        List<Staff> physiciansOnDuty = staffAssignmentManager.getPhysiciansOnDuty();
        // Assert
        Staff expectedStaff1 = new Staff(1, "John Doctor", StaffRole.DOCTOR);
        Staff expectedStaff2 = new Staff(2, "Frank Resident", StaffRole.RESIDENT);
        assertThat(physiciansOnDuty).isEqualTo(List.of(expectedStaff1, expectedStaff2));
    }
}