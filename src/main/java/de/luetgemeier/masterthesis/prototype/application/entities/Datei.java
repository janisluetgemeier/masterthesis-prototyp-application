package de.luetgemeier.masterthesis.prototype.application.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_dateien")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Datei {

    @Id
    @GeneratedValue
    private Integer id;

    private Integer userid;

    private String originalfilename;

    private String storedfilename;
}
