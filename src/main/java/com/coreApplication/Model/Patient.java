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
    private Long id;
    private String patientName;
    private int age;
    private String notes;
    private String socialMediaLink;
    private String generalStatus = "GOOD";
    private List<Post> posts = new ArrayList<>();
}
