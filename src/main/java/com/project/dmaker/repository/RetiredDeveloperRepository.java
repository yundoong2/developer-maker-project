package com.project.dmaker.repository;

import com.project.dmaker.entity.Developer;
import com.project.dmaker.entity.RetiredDeveloper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 퇴직자 정보 저장을 위한 Jpa Repository
 * @author cyh68
 * @since 2023-05-08
 **/
@Repository
public interface RetiredDeveloperRepository
        extends JpaRepository<RetiredDeveloper, Long> {
}
