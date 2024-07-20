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
            e.printStackTrace();
        }
        model = tempModel;
    }
    @Test
    public void testModal() throws Exception {
        String inputText = "i want to kill my self";
        float prediction = model.predict(inputText);
        System.out.println("the model prediction is: " + prediction);
        
        String inputText2 = "i am happy";
        float prediction2 = model.predict(inputText2);
        System.out.println("the model prediction is: " + prediction2);
        
        String inputText3 = "i am sad";
        float prediction3 = model.predict(inputText3);
        System.out.println("the model prediction is: " + prediction3);
    }

}
