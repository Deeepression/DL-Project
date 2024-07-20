package com.coreApplication.Controllers;

import com.coreApplication.Model.DLModel;
import com.coreApplication.Utils.PredictionRequest;
import com.coreApplication.Utils.PredictionResponse;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
class PredictionController {

    private final DLModel model = new DLModel();

    @PostMapping("/predict")
    public PredictionResponse predict(@RequestBody PredictionRequest request) {
        String inputText = request.getText();
        float prediction = model.predict(inputText);
        return new PredictionResponse(prediction);
    }
}
