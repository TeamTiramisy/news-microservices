package com.alibou.microservices.repository;

import com.alibou.microservices.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query(value = """
      select t from Token t
      where t.userId = :userId and (t.expired = false or t.revoked = false)
      """)
    List<Token> findAllValidTokenByUser(Integer userId);

    Optional<Token> findByToken(String token);
}
