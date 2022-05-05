package com.tkt.financial.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "result")
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "ca")
    private int ca;

    @Column(name = "margin")
    private int margin;

    @Column(name = "ebitda")
    private int ebitda;

    @Column(name = "loss")
    private int loss;

    @Column(name = "year")
    private int year;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    @JsonIgnore
    private Company company;
}
