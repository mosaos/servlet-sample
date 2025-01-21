package mosaos.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "password")
public class User implements Serializable {
    private Long id;
    private String uuid;
    private String userId;
    private String password;
    private String userName;
    private String email;
    private Boolean isAdmin;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}