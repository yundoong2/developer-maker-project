package com.project.dmaker.repository;

import com.project.dmaker.code.StatusCode;
import com.project.dmaker.entity.Developer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 개발자 정보 저장을 위한 Jpa Repository
 * @author cyh68
 * @since 2023-05-08
 **/
@Repository
public interface DeveloperRepository
        extends JpaRepository<Developer, Long> {
    Optional<Developer> findByMemberId(String memberId);

    List<Developer> findDevelopersByStatusCodeEquals(StatusCode statusCode);
}
