package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    private String name;
    private String description;
    private StatusEnum status;
    private LocalDate deadLine;
}