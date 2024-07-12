package com.coreApplication.Controllers;

import com.coreApplication.Model.DLModel;
import com.coreApplication.Utils.PredictionRequest;
import com.coreApplication.Utils.PredictionResponse;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
class PredictionController {

    private static final DLModel model = new DLModel();

    @PostMapping("/predict")
    public PredictionResponse predict(@RequestBody PredictionRequest request) {
        // Preprocess the input text (if necessary)
        String inputText = request.getText();

        // Perform prediction using the model
        double predictionScore = model.predict(inputText);

        // Return the prediction score
        return new PredictionResponse(predictionScore);
    }
}
