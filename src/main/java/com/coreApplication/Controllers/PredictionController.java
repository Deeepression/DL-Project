package com.coreApplication.Controllers;

import com.coreApplication.Model.DLModel;
import com.coreApplication.Model.Patient;
import com.coreApplication.Model.Post;
import com.coreApplication.Utils.PredictionRequest;
import com.coreApplication.Utils.PredictionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
class PredictionController {

    @Autowired
    private PatientController patientController;

    private final DLModel model = new DLModel();

    @PostMapping("/predict")
    public PredictionResponse predict(@RequestBody PredictionRequest request) {
        String inputText = request.getText();
        float prediction = model.predict(inputText);
        return new PredictionResponse(prediction);
    }
}
