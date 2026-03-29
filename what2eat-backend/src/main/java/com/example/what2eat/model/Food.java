package com.example.what2eat.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "food") // 强制改回 food，避免产生两个表
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // 手动保留 Getter 和 Setter，解决空值存入的“最后一公里”问题
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}