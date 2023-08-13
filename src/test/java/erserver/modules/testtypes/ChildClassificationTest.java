package erserver.modules.testtypes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class ChildClassificationTest {

   private LocalDate currentDate;


   @BeforeEach
   public void setUp() {
      currentDate = LocalDate.of(2000, 1, 10);
   }

   @Test
   void returnNeonateUpTo30DaysOld() {
      assertThat(ChildClassification.calculate(currentDate, currentDate))
              .isEqualTo(ChildClassification.NEONATE);

      LocalDate birthDate = currentDate.minusDays(29);
      assertThat(ChildClassification.calculate(birthDate, currentDate))
              .isEqualTo(ChildClassification.NEONATE);
   }

   @Test
   void returnInfantFrom30DaysTo2Years() {
      LocalDate birthDate = currentDate.minusDays(30);
      assertThat(ChildClassification.calculate(birthDate, currentDate))
              .isEqualTo(ChildClassification.INFANT);

      birthDate = currentDate.minusYears(2).plusDays(1);
      assertThat(ChildClassification.calculate(birthDate, currentDate))
              .isEqualTo(ChildClassification.INFANT);
   }

   @Test
   void returnChildFrom2YearsTo12years() {
      LocalDate birthDate = currentDate.minusYears(2);
      assertThat(ChildClassification.calculate(birthDate, currentDate))
              .isEqualTo(ChildClassification.CHILD);

      birthDate = currentDate.minusYears(12).plusDays(1);
      assertThat(ChildClassification.calculate(birthDate, currentDate))
              .isEqualTo(ChildClassification.CHILD);
   }

   @Test
   void returnAdolescentFrom12YearsTo16years() {
      LocalDate birthDate = currentDate.minusYears(12);
      assertThat(ChildClassification.calculate(birthDate, currentDate))
              .isEqualTo(ChildClassification.ADOLESCENT);

      birthDate = currentDate.minusYears(16).plusDays(1);
      assertThat(ChildClassification.calculate(birthDate, currentDate))
              .isEqualTo(ChildClassification.ADOLESCENT);
   }

   @Test
   void returnUndefinedAfter16Years() {
      LocalDate birthDate = currentDate.minusYears(16);
      assertThat(ChildClassification.calculate(birthDate, currentDate))
              .isEqualTo(ChildClassification.UNDEFINED);

      birthDate = currentDate.minusYears(80);
      assertThat(ChildClassification.calculate(birthDate, currentDate))
              .isEqualTo(ChildClassification.UNDEFINED);
   }

   @Test
   void returnUndefinedIfBirthdateInFuture() {
      assertThat(ChildClassification.calculate(currentDate.plusDays(1), currentDate))
              .isEqualTo(ChildClassification.UNDEFINED);
   }
}