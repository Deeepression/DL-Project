package com.coreApplication.Model;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtSession;
import edu.stanford.nlp.simple.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DLModel {

    private final OrtEnvironment env;
    private final OrtSession session;

    public DLModel() throws Exception {
        env = OrtEnvironment.getEnvironment();
        session = env.createSession(Files.readAllBytes(Paths.get("/Users/user/Downloads/model.onnx")));
    }

    public float predict(String text) throws Exception {
        Map<String, OnnxTensor> inputs = preprocess(text);
        OrtSession.Result results = session.run(inputs);
        float[][] output = (float[][]) results.get(0).getValue();
        return convertToProbability(output);
    }

    private Map<String, OnnxTensor> preprocess(String text) throws Exception {
        Document doc = new Document(text);
        List<String> tokens = doc.sentences().stream()
                .flatMap(sentence -> sentence.words().stream())
                .collect(Collectors.toList());

        // Example tokenization logic; replace with actual token IDs and attention mask generation
        long[] inputIds = tokens.stream().mapToLong(token -> token.hashCode() & 0xfffffff).toArray();
        long[] attentionMask = new long[inputIds.length];
        java.util.Arrays.fill(attentionMask, 1);

        Map<String, OnnxTensor> inputs = new HashMap<>();
        inputs.put("input_ids", OnnxTensor.createTensor(env, new long[][]{inputIds}));
        inputs.put("attention_mask", OnnxTensor.createTensor(env, new long[][]{attentionMask}));

        return inputs;
    }

    private float convertToProbability(float[][] output) {
        return output[0][0];
    }
}