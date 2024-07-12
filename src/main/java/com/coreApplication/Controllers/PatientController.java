package com.coreApplication.Controllers;

import com.coreApplication.Model.Patient;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "http://localhost:3000")
public class PatientController {

    private final List<Patient> patients = new ArrayList<>();
    private Long nextId = 1L;

    @GetMapping
    public List<Patient> getAllPatients() {
        return patients;
    }

    @PostMapping
    public Patient addPatient(@RequestBody Patient patient) {
        patient.setId(nextId++);
        patients.add(patient);
        return patient;
    }

    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable Long id) {
        patients.removeIf(patient -> patient.getId().equals(id));
    }
}
