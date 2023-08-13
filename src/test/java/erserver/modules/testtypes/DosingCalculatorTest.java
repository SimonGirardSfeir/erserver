package erserver.modules.testtypes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatRuntimeException;

class DosingCalculatorTest {

    private DosingCalculator dosingCalculator;
    private Patient patient;

    @BeforeEach
    public void setUp() {
        dosingCalculator = new DosingCalculator();
        patient = new Patient();
    }

    @Test
    void returnsCorrectDosesForNeonate() {
        patient.setBirthDate(LocalDate.now().minusDays(28));
        String singleDose = dosingCalculator.getRecommendedSingleDose(patient, "Tylenol Oral Suspension");
        assertThat(singleDose).isEqualTo("0");
    }

    @Test
    void returnsCorrectDosesForInfant() {
        patient.setBirthDate(LocalDate.now().minusDays(40));
        String singleDose = dosingCalculator.getRecommendedSingleDose(patient, "Tylenol Oral Suspension");
        assertThat(singleDose).isEqualTo("2.5 ml");
    }

    @Test
    void returnsCorrectDosesForChild() {
        patient.setBirthDate(LocalDate.now().minusYears(3));
        String singleDose = dosingCalculator.getRecommendedSingleDose(patient, "Tylenol Oral Suspension");
        assertThat(singleDose).isEqualTo("5 ml");
    }

    @Test
    void returnsCorrectDosesForNeonateAmox() {
        patient.setBirthDate(LocalDate.now().minusDays(20));
        String singleDose = dosingCalculator.getRecommendedSingleDose(patient, "Amoxicillin Oral Suspension");
        assertThat(singleDose).isEqualTo("15 mg/kg");
    }

    @Test
    void returnsExceptionForAdults() {
        patient.setBirthDate(LocalDate.now().minusYears(16));
        assertThatRuntimeException().isThrownBy(
                () -> dosingCalculator.getRecommendedSingleDose(patient, "Amoxicillin Oral Suspension")
        );
    }

    @Test
    void returnsNullForUnrecognizedMedication() {
        patient.setBirthDate(LocalDate.now().minusYears(16));
        assertThatRuntimeException().isThrownBy(
                () -> dosingCalculator.getRecommendedSingleDose(patient, "No Such Med")
        );
    }
}