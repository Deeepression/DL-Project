package com.test;
import com.coreApplication.Model.DLModel;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

public class DLModelTest {
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
    @Test
    public void testModal() throws Exception {
        String inputText = "i want to kill my self";
        float prediction = model.predict(inputText);
        System.out.println("the model prediction is: " + prediction);
    }

}
