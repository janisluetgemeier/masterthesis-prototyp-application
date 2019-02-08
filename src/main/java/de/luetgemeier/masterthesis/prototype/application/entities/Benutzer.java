package de.luetgemeier.masterthesis.prototype.application.entities;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "t_benutzer")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Benutzer {

    @Id
    @GeneratedValue
    private Integer id;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String password;
    @Column(name = "isadmin", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean isadmin = false;

    @Column(name = "token", nullable = true)
    private String token;


}
