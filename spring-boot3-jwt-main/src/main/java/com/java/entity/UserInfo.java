package com.java.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @NotBlank(message = "Name cannot be blank")
  private String name;

  @Email(message = "Invalid email format")
  private String email;

  @Size(min = 8, message = "Password must be at least 8 characters long")
  private String password;

  private String roles;

  public boolean validateRoles(String roles) {
    // Your custom logic to validate roles format, allowed values etc.
    // This can involve string manipulation, regular expressions, etc.
    return roles.matches("[A-Za-z,]+"); // Example: Allow comma-separated roles with letters only
  }
}