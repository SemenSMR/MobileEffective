package com.example.mobileeffective.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Email(message = "")
    @NotBlank(message = "Адрес электронной почты должен быть действительным")
    @Column(nullable = false, unique = true)
    private String email;
    @NotBlank(message = "Требуется ввести пароль")
    @Size(min = 6, message = "Длина пароля должна составлять не менее 6 символов")
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;
}
