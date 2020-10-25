package com.matejko.wownetwork.persistance.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
@Data
@EqualsAndHashCode
public class Post {
    /**
     * Identifier in database
     */
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;

    /**
     * Message of the post
     */
    private String message;

    /**
     * Date when the post has been added
     */
    private Date postDate;

    /**
     * The user that created the post
     */
    @ManyToOne
    private User user;
}
