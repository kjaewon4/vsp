package com.bu.startup.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class UserProfile extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String nickname;
    private String email;
    private int level;
    private int score;
   
    private boolean banned;
    private boolean isAdmin;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
