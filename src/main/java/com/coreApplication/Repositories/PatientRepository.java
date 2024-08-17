package com.coreApplication.Repositories;

import com.coreApplication.Model.Patient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.MongoRepository;
@Configuration
public interface PatientRepository extends MongoRepository<Patient, String> {
}
