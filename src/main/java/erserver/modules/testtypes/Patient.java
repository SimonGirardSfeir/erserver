package erserver.modules.testtypes;

import erserver.modules.dependencies.Priority;

import java.time.LocalDate;
import java.util.Objects;

public class Patient {

   private int transportId;
   private String name;
   private LocalDate birthDate;
   private Priority priority;
   private String condition;

   public Patient() {
   }
   public Patient(int transportId, String name, LocalDate birthDate, Priority priority, String condition) {
      this.transportId = transportId;
      this.name = name;
      this.birthDate = birthDate;
      this.priority = priority;
      this.condition = condition;
   }

   public int getTransportId() {
      return transportId;
   }

   public void setTransportId(int transportId) {
      this.transportId = transportId;
   }

   public LocalDate getBirthDate() {
      return birthDate;
   }

   public void setBirthDate(LocalDate birthDate) {
      this.birthDate = birthDate;
   }

   public ChildClassification getChildClassification() {
      return ChildClassification.calculate(birthDate, LocalDate.now());
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public Priority getPriority() {
      return priority;
   }

   public void setPriority(Priority priority) {
      this.priority = priority;
   }

   public String getCondition() {
      return condition;
   }

   public void setCondition(String condition) {
      this.condition = condition;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o)
         return true;
      if (o == null || getClass() != o.getClass())
         return false;
      Patient patient = (Patient) o;
      return transportId == patient.transportId && Objects.equals(name, patient.name) &&
              Objects.equals(birthDate, patient.birthDate) && priority == patient.priority &&
              Objects.equals(condition, patient.condition);
   }

   @Override
   public int hashCode() {
      return Objects.hash(transportId, name, birthDate, priority, condition);
   }
}
