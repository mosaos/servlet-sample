package mosaos.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Note implements Serializable {
    private Long id;
    private String uuid;
    private Long userId;
    private String title;
    private String content;
    private String tags;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}