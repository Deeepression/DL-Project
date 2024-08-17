package com.coreApplication.Controllers;

import com.coreApplication.Exceptions.PostNotFoundException;
import com.coreApplication.Model.DLModel;
import com.coreApplication.Model.Patient;
import com.coreApplication.Model.Post;
import com.coreApplication.Repositories.PostRepository;
import com.coreApplication.Utils.PredictionRequest;
import com.coreApplication.Utils.PredictionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/predictions")
@CrossOrigin(origins = "http://localhost:3000")
class PredictionController {

    @Autowired
    private PatientController patientController;

    @Autowired
    private PostRepository postRepository;

    private final DLModel model = new DLModel();

    @PostMapping("/predict")
    public PredictionResponse predict(@RequestBody PredictionRequest request) {
        String inputText = request.getText();
        float prediction = model.predict(inputText);
        return new PredictionResponse(prediction);
    }

    @GetMapping("/post/{patientId}/{postId}")
    public Post predictPostById(@PathVariable String patientId, @PathVariable String postId) {
        Post post =  postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
        String postContent = post.getText();
        double predictionScore = predict(new PredictionRequest(postContent)).getScore();
        post.setPrediction((float) predictionScore);
        postRepository.save(post);
        patientController.addOrUpdatePostByIdToPatient(patientId, postId);
        return post;
    }
}
