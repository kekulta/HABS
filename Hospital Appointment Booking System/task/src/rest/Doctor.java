package rest;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "doctors")
class Doctor {
    @Id
    private long id;
    @Column
    @NotBlank
    @NotNull
    private String doctorName;

    public Doctor() {

    }

    public Doctor(long id, String doctorName) {
        this.id = id;
        this.doctorName = doctorName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
}
