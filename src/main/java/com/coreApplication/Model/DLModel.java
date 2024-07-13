package com.coreApplication.Model;

import ai.djl.Model;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.translate.Batchifier;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;

import java.io.IOException;
import java.nio.file.Paths;

public class DLModel {

    private Model model;
    private HuggingFaceTokenizer tokenizer;

    public DLModel() throws IOException, ModelException {
        this.model = Model.newInstance("bert-base-uncased");
        this.model.load(Paths.get("/Users/user/Documents/GitHub/DL-Project/src/main/resources/scripted_model.pt"));

        this.tokenizer = HuggingFaceTokenizer.builder()
                .optTokenizerPath(Paths.get("/Users/user/Documents/GitHub/DL-Project/src/main/resources/tokenizer/tokenizer.json"))
                .build();
    }

    public float predict(String text) throws TranslateException {
        try (Predictor<String, Float> predictor = model.newPredictor(new CustomTranslator(tokenizer))) {
            return predictor.predict(text);
        }
    }

    private static class CustomTranslator implements Translator<String, Float> {
        private HuggingFaceTokenizer tokenizer;

        public CustomTranslator(HuggingFaceTokenizer tokenizer) {
            this.tokenizer = tokenizer;
        }
        
        @Override
        public NDList processInput(TranslatorContext ctx, String input) {
            ai.djl.huggingface.tokenizers.Encoding encoding = tokenizer.encode(input);
            NDManager manager = ctx.getNDManager();
            // Assuming getIds() and getAttentionMask() return long[] based on the toArray() usage
            long[] inputIds = encoding.getIds();
            long[] attentionMask = encoding.getAttentionMask();
            NDArray inputIdsArray = manager.create(inputIds);
            NDArray attentionMaskArray = manager.create(attentionMask);
            return new NDList(inputIdsArray, attentionMaskArray);
        }

        @Override
        public Float processOutput(TranslatorContext ctx, NDList list) {
            NDArray logits = list.singletonOrThrow();
            NDArray probabilities = logits.signi();
            return probabilities.getFloat();
        }

        @Override
        public Batchifier getBatchifier() {
            return null;
        }
    }
}
