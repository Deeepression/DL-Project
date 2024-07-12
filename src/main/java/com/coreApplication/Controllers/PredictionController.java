package com.coreApplication.Controllers;

import com.coreApplication.Model.DLModel;
import com.coreApplication.Utils.PredictionRequest;
import com.coreApplication.Utils.PredictionResponse;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
class PredictionController {

    private static final DLModel model;

    static {
        DLModel tempModel = null;
        try {
            tempModel = new DLModel();
        } catch (Exception e) {
            // Log the error or handle it as per your application's error handling policy
            e.printStackTrace();
            // Consider setting tempModel to a default state or using a fallback strategy
        }
        model = tempModel;
    }

    @PostMapping("/predict")
    public PredictionResponse predict(@RequestBody PredictionRequest request) throws Exception {
        if (model == null) {
            // Handle the case where model initialization failed
            throw new IllegalStateException("Model initialization failed. Unable to perform prediction.");
        }
        // Preprocess the input text (if necessary)
        String inputText = request.getText();

        // Perform prediction using the model
        float prediction = model.predict(inputText);
        
        // Create and return the prediction response
        return new PredictionResponse(prediction);
    }
}
