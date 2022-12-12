package rest;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "appointments")
class Appointment {
    @Id
    private long idApp;
    @NotBlank
    private String doctor;
    @NotBlank
    private String patient;
    @NotBlank
    @Pattern(regexp = "\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])*")
    private String date;

    public Appointment() {

    }

    Appointment(long idApp, String doctor, String patient, String date) {
        this.idApp = idApp;
        this.doctor = doctor;
        this.patient = patient;
        this.date = date;
    }

    public long getIdApp() {
        return idApp;
    }

    public void setIdApp(long idApp) {
        this.idApp = idApp;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}


