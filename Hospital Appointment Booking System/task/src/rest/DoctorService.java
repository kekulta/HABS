package rest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
class DoctorService {

    AtomicInteger counter = new AtomicInteger(0);

    private final DoctorRepository doctorRepository;

    DoctorService(@Autowired DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    List<Doctor> getAllDoctors() {
        List<Doctor> lst = new ArrayList<Doctor>();
        doctorRepository.findAll().forEach(lst::add);
        return lst;
    }

    void deleteAll() {
        doctorRepository.deleteAll();
        System.out.println("Doctor repository cleaned");
    }

    void delete(Doctor doc) {
        doctorRepository.delete(doc);
    }

    @Transactional
    void deleteById(Long id) {
        doctorRepository.deleteById(id);
    }

    Doctor findDoctorById(Long id) {
        return doctorRepository.findDoctorById(id);
    }

    Doctor save(Doctor toSave) {
        return doctorRepository.save(toSave);
    }
}

@Repository
interface DoctorRepository extends CrudRepository<Doctor, Long> {
    Doctor findDoctorById(Long id);


}