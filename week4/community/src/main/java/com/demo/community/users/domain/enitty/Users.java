package com.demo.community.users.domain.enitty;

import com.demo.community.common.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Users extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    // SHA-256 해싱 알고리즘 사용
    @Column(length = 64, nullable = false)
    private String password;

    @Column (length = 20, nullable = false)
    private String nickname = null;

    @Column (nullable = false)
    @Builder.Default
    private String profileImage = "https://i.namu.wiki/i/M0j6sykCciGaZJ8yW0CMumUigNAFS8Z-dJA9h_GKYSmqqYSQyqJq8D8xSg3qAz2htlsPQfyHZZMmAbPV-Ml9UA.webp";
}
