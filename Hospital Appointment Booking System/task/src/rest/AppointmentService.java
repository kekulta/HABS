package rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class AppointmentService {

    AtomicInteger counter = new AtomicInteger(0);

    private final AppointmentRepository appointmentRepository;

    public AppointmentService(@Autowired AppointmentRepository rep) {
        this.appointmentRepository = rep;
    }

    List<Appointment> getAllAppointments() {
        List<Appointment> result = new ArrayList<>();
        appointmentRepository.findAll().forEach(result::add);
        return result;
    }

    Appointment save(Appointment toSave) {
        return appointmentRepository.save(toSave);
    }

}

@Repository
interface AppointmentRepository extends CrudRepository<Appointment, Long> {
    Appointment findAppointmentByIdApp(Long idApp);
}
