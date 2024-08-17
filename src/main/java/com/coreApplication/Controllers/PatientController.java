package com.coreApplication.Controllers;

import com.coreApplication.Exceptions.PatientNotFoundException;
import com.coreApplication.Model.Patient;
import com.coreApplication.Model.Post;
import com.coreApplication.Repositories.PatientRepository;
import com.coreApplication.Repositories.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public Patient getPatient(@PathVariable String id) {
        return patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException(id));
    }

    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable String id) {
        patientRepository.deleteById(id);
    }

    @PostMapping("/{patientId}/posts")
    public Patient addPostToPatient(@PathVariable String patientId, @RequestBody Post post) {
        Patient patient = getPatient(patientId);
        postRepository.save(post);
        patient.getPosts().add(post);
        return patientRepository.save(patient);
    }

    @DeleteMapping("/{patientId}/posts/{postId}")
    public Patient deletePostFromPatient(@PathVariable String patientId, @PathVariable String postId) {
        Patient patient = getPatient(patientId);
        patient.getPosts().removeIf(post -> post.getId().equals(postId));
        postRepository.deleteById(postId);
        return patientRepository.save(patient);
    }

    @PostMapping("/{patientId}/posts/{postId}")
    public Patient addOrUpdatePostByIdToPatient(@PathVariable String patientId, @PathVariable String postId) {
        // Fetch the Patient
        Patient patient = getPatient(patientId);

        // Fetch the Post
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));

        // Replace the existing post with the same ID or add the new post
        List<Post> updatedPosts = new ArrayList<>(patient.getPosts());
        updatedPosts.removeIf(existingPost -> existingPost.getId().equals(postId)); // Remove the existing post if present
        updatedPosts.add(post); // Add the new post

        // Set the new list on the patient
        patient.setPosts(updatedPosts);

        // Save the updated patient
        return patientRepository.save(patient);
    }


    public Patient getPatientById(String id) {
        return getPatient(id);
    }
}
