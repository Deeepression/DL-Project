package com.coreApplication.Model;

import ai.onnxruntime.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class DLModel {

    private final OrtEnvironment env;
    private final OrtSession session;

    public DLModel() throws Exception {
        // Initialize the environment and session for ONNX Runtime
        env = OrtEnvironment.getEnvironment();
        session = env.createSession(Files.readAllBytes(Paths.get("p/Users/user/Downloads/model.onnx")));
    }

    public float predict(String text) throws Exception {
        // Preprocess the text to convert it into a format suitable for your model
        Map<String, OnnxTensor> inputs = preprocess(text);

        // Run the model
        OrtSession.Result results = session.run(inputs);

        // Extract and return the prediction from the results
        float[][] output = (float[][]) results.get(0).getValue();
        return output[0][0]; // Adjust based on your model's output
    }

    private Map<String, OnnxTensor> preprocess(String text) throws Exception {
        // Implement preprocessing of the text here
        // This is a placeholder example, replace it with actual preprocessing logic
        long[][] inputIds = {{101, 2054, 2003, 1996, 2160, 102}};
        long[][] attentionMask = {{1, 1, 1, 1, 1, 1}};

        Map<String, OnnxTensor> inputs = new HashMap<>();
        inputs.put("input_ids", OnnxTensor.createTensor(env, inputIds));
        inputs.put("attention_mask", OnnxTensor.createTensor(env, attentionMask));

        return inputs;
    }
}