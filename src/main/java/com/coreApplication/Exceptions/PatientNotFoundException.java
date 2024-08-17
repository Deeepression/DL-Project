package com.coreApplication.Exceptions;

public class PatientNotFoundException extends RuntimeException {
    public PatientNotFoundException(String id) {
        super("Patient with ID " + id + " not found");
    }
}
