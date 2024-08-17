package com.coreApplication.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "patients")
public class Patient {
    @Id
    private String id;
    private String patientName;
    private int age;
    private String notes;
    private String socialMediaLink;
    private String generalStatus = "GOOD";
    private List<Post> posts = new ArrayList<>();

    public void updateGeneralStatus() {
        double averageScore = this.getPosts().stream().mapToDouble(Post::getPrediction).average().orElse(0) *  100;
        String status;
        if (averageScore > 60) {
            status = "BAD";
        } else if (averageScore > 30) {
            status = "MEDIUM";
        } else {
            status = "GOOD";
        }

        this.generalStatus = status;
    }
}
