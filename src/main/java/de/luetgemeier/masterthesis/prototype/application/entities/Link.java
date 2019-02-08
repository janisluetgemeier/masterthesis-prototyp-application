package de.luetgemeier.masterthesis.prototype.application.entities;

import lombok.*;
import org.checkerframework.checker.units.qual.A;

import javax.persistence.*;

@Entity
@Table(name = "t_links")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Link {

    @Id
    @GeneratedValue
    private Integer id;
    private String hash;
    @Column(name = "userid", nullable = false)
    private Integer userid;
    @Column(name = "fileid", nullable = false)
    private Integer fileid;
}
