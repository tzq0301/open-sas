package cn.tzq0301.opensasopenmind.repository;

import cn.tzq0301.opensasopenmind.entity.token.OpenSasToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpenSasTokenRepository extends JpaRepository<OpenSasToken, Long> {
    boolean existsByToken(String token);
}
