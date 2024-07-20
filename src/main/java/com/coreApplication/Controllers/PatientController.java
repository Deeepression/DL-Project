package com.coreApplication.Controllers;

import com.coreApplication.Exceptions.PatientNotFoundException;
import com.coreApplication.Model.Patient;
import com.coreApplication.Model.Post;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "http://localhost:3000")
public class PatientController {

    private final List<Patient> patients = new ArrayList<>();
    private final List<Post> posts = new ArrayList<>(); // Assuming a global posts list for simplicity
    private Long nextPatientId = 1L;
    private Long nextPostId = 1L;

    @GetMapping
    public List<Patient> getAllPatients() {
        return patients;
    }

    @PostMapping
    public Patient addPatient(@RequestBody Patient patient) {
        patient.setId(nextPatientId++);
        patients.add(patient);
        return patient;
    }

    @GetMapping("/{id}")
    public Patient getPatient(@PathVariable Long id) {
        return patients.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new PatientNotFoundException(id));
    }

    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable Long id) {
        patients.removeIf(patient -> patient.getId().equals(id));
    }

    @PostMapping("/{patientId}/posts")
    public Patient addPostToPatient(@PathVariable Long patientId, @RequestBody Post post) {
        Patient patient = getPatient(patientId);
        post.setId(nextPostId++);
        patient.getPosts().add(post);
        posts.add(post);
        return patient;
    }

    @DeleteMapping("/{patientId}/posts/{postId}")
    public Patient deletePostFromPatient(@PathVariable Long patientId, @PathVariable Long postId) {
        Patient patient = getPatient(patientId);
        patient.getPosts().removeIf(post -> post.getId().equals(postId));
        posts.removeIf(post -> post.getId().equals(postId));
        return patient;
    }

    @PostMapping("/{patientId}/posts/{postId}")
    public Patient addPostByIdToPatient(@PathVariable Long patientId, @PathVariable Long postId) {
        Patient patient = getPatient(patientId);
        Post post = posts.stream()
                .filter(p -> p.getId().equals(postId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));
        patient.getPosts().add(post);
        return patient;
    }
}
