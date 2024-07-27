package com.coreApplication.Controllers;

import com.coreApplication.Exceptions.PatientNotFoundException;
import com.coreApplication.Model.Patient;
import com.coreApplication.Model.Post;
import com.coreApplication.Repositories.PatientRepository;
import com.coreApplication.Repositories.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "http://localhost:3000")
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PostRepository postRepository;

    @GetMapping
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @PostMapping
    public Patient addPatient(@RequestBody Patient patient) {
        return patientRepository.save(patient);
    }

    @GetMapping("/{id}")
    public Patient getPatient(@PathVariable Long id) {
        return patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException(id));
    }

    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable Long id) {
        patientRepository.deleteById(id);
    }

    @PostMapping("/{patientId}/posts")
    public Patient addPostToPatient(@PathVariable Long patientId, @RequestBody Post post) {
        Patient patient = getPatient(patientId);
        postRepository.save(post);
        patient.getPosts().add(post);
        return patientRepository.save(patient);
    }

    @DeleteMapping("/{patientId}/posts/{postId}")
    public Patient deletePostFromPatient(@PathVariable Long patientId, @PathVariable Long postId) {
        Patient patient = getPatient(patientId);
        patient.getPosts().removeIf(post -> post.getId().equals(postId));
        postRepository.deleteById(postId);
        return patientRepository.save(patient);
    }

    @PostMapping("/{patientId}/posts/{postId}")
    public Patient addPostByIdToPatient(@PathVariable Long patientId, @PathVariable Long postId) {
        Patient patient = getPatient(patientId);
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));
        patient.getPosts().add(post);
        return patientRepository.save(patient);
    }

    public Patient getPatientById(Long id) {
        return getPatient(id);
    }
}
