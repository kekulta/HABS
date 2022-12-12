package rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.persistence.*;
import javax.print.Doc;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}

@RestController
class Controller {

    @Autowired
    AppointmentService appServ;

    @Autowired
    DoctorService docServ;


    @PostMapping("/setAppointment")
    ResponseEntity<Appointment> setAppointment(@Valid @RequestBody Appointment appointment) {
        System.out.println(appointment);
        boolean valid = false;
        for (Doctor doc : docServ.getAllDoctors()) {
            System.out.println(doc.getDoctorName() + " " + appointment.getDoctor());
            if (doc.getDoctorName().equals(appointment.getDoctor().toLowerCase())) {
                for (AppointInfo info : generate(4, doc.getDoctorName())) {
                    System.out.println(info.avalabletime + " " + appointment.getDate() + " " + info.booked);
                    if (Objects.equals(info.avalabletime, appointment.getDate()) && !info.booked) {
                        valid = true;
                        break;
                    }
                }
                break;
            }
        }
        if (!valid) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Appointment app = new Appointment(appServ.counter.getAndIncrement(), appointment.getDoctor().toLowerCase(), appointment.getPatient().toLowerCase(), appointment.getDate());

        appServ.save(app);
        return new ResponseEntity(app, HttpStatus.OK);
    }

    @GetMapping("/appointments")
    ResponseEntity<Appointment> appointments() {
        if (appServ.getAllAppointments().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(appServ.getAllAppointments(), HttpStatus.OK);
    }

    @DeleteMapping("/deleteAppointment")
    ResponseEntity delete(@RequestParam int id) {
        for (int i = 0; i < appServ.getAllAppointments().size(); i++) {
            if (appServ.getAllAppointments().get(i).getIdApp() == id) {
                Appointment app = new Appointment(appServ.getAllAppointments().get(i).getIdApp(), appServ.getAllAppointments().get(i).getDoctor(), appServ.getAllAppointments().get(i).getPatient(), appServ.getAllAppointments().get(i).getDate());
                appServ.getAllAppointments().remove(i);
                return new ResponseEntity(app, HttpStatus.OK);
            }
        }
        return new ResponseEntity(new StringResponse("The appointment does not exist or was already cancelled"), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/newDoctor")
    ResponseEntity newDoctor(@RequestBody @Valid Doctor doc) {

        docServ.getAllDoctors().forEach(doctor -> {
            if (Objects.equals(doctor.getDoctorName(), doc.getDoctorName().toLowerCase())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        });

        return new ResponseEntity(docServ.save(new Doctor(docServ.counter.getAndIncrement(), doc.getDoctorName().toLowerCase())), HttpStatus.OK);
    }

    @GetMapping("/allDoctorslist")
    ResponseEntity allDoctors() {
        var lst = docServ.getAllDoctors();
        var code = HttpStatus.OK;
        if (lst.isEmpty()) {
            code = HttpStatus.NO_CONTENT;
        }
        return new ResponseEntity(lst, code);
    }

    @GetMapping("/availableDatesByDoctor")
    ResponseEntity getAvailablesDate(@RequestParam String doc) {
        for (Doctor doctor : docServ.getAllDoctors()) {
            if (Objects.equals(doctor.getDoctorName(), doc.toLowerCase())) {
                return new ResponseEntity(generate(4, doc), HttpStatus.OK);
            }
        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/deleteDoctor")
    ResponseEntity deleteDoctor(@RequestParam String doc) {
        for (Doctor doctor : docServ.getAllDoctors()) {
            if (Objects.equals(doc.toLowerCase(), doctor.getDoctorName())) {
                for (int i = 0; i < appServ.getAllAppointments().size(); i++) {
                    if (Objects.equals(appServ.getAllAppointments().get(i).getDoctor(), doctor.getDoctorName())) {
                        appServ.getAllAppointments().get(i).setDoctor("director");
                    }
                }
                docServ.getAllDoctors().forEach(doct -> System.out.println(doct.getDoctorName()));
                System.out.println(doctor.getId() + " " + doctor.getDoctorName());
                docServ.deleteAll();
                docServ.getAllDoctors().forEach(doct -> System.out.println(doct.getId()));
                return new ResponseEntity(doctor, HttpStatus.OK);
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    List<AppointInfo> generate(int days, String doctorName) {
        List<AppointInfo> result = new ArrayList<>();
        for (int i = 0; i < days; i++) {
            var date = LocalDate.now().plusDays(i + 1).toString();
            AtomicBoolean booked = new AtomicBoolean(false);
            appServ.getAllAppointments().forEach(appointment -> {
                if (Objects.equals(appointment.getDoctor(), doctorName.toLowerCase()) && Objects.equals(appointment.getDate(), date)) {
                    booked.set(true);

                }
            });
            result.add(new AppointInfo(date, booked.get()));
        }
        return result;
    }
}

class AppointInfo {
    String avalabletime;

    public String getAvalabletime() {
        return avalabletime;
    }

    public Boolean getBooked() {
        return booked;
    }

    public void setAvalabletime(String avalabletime) {
        this.avalabletime = avalabletime;
    }

    public void setBooked(Boolean booked) {
        this.booked = booked;
    }

    Boolean booked;

    public AppointInfo(String date, Boolean booked) {
        this.avalabletime = date;
        this.booked = booked;
    }
}

class StringResponse {

    private String error;

    public StringResponse(String s) {
        this.error = s;
    }


    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}








