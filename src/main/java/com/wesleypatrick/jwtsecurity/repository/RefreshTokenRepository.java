package com.wesleypatrick.jwtsecurity.repository;

import com.wesleypatrick.jwtsecurity.model.RefreshToken;
import com.wesleypatrick.jwtsecurity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByLookupHash(String lookupHash);

    Optional<RefreshToken> findByUser(User user);

    void deleteByUser_Email(String email);
}
