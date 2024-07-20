package com.coreApplication.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    private Long id;
    private String patientName;
    private int age;
    private String notes;
    private String socialMediaLink;
    private String generalStatus = "GOOD";
    private List<Post> posts = new ArrayList<>();
}
