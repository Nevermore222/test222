package com.gm.wj.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@Table(name = "review")
@ToString
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /**
     * Title of the question.
     */
    private String question;

    /**
     * answer of the question.
     */
    private String answer;

    /**
     * type of the question.
     */
    private String type;

    /**
     * showType
     */
    @Transient
    private String showType;

    /**
     * isOK
     */
    @Transient
    private Boolean isOK;

}
