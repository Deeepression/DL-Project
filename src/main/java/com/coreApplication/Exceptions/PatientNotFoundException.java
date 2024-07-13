package com.coreApplication.Exceptions;

public class PatientNotFoundException extends RuntimeException {
    public PatientNotFoundException(Long id) {
        super("Patient with ID " + id + " not found");
    }
}
