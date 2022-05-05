package com.tkt.financial.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "sector")
    private String sector;

    @Column(name = "siren")
    private int siren;

    @OneToMany(mappedBy = "company", cascade = CascadeType.REMOVE)
    private List<Result> results;

    public static int compareTo(Company c1, Company c2, String attributeName, Boolean desc) {
        int result = 0;
        switch (attributeName) {
            case "id":
                result = Integer.compare(c1.id, c2.id);
                break;
            case "name":
                result = c1.name.compareTo(c2.name);
                break;
            case "sector":
                result = c1.sector.compareTo(c2.sector);
                break;
            case "siren":
                result = Integer.compare(c1.siren, c2.siren);
                break;
            default:
                return 0;
        }
        if (desc != null && desc) {
            return (-1) * result;
        } else {
            return result;
        }
    }

}
