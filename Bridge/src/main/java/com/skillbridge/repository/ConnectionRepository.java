package com.skillbridge.repository;

import com.skillbridge.entity.Connection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Long> {

    @Query("""
            SELECT c FROM Connection c
            WHERE c.studentOne.id = :studentId OR c.studentTwo.id = :studentId
            ORDER BY c.connectedAt DESC
            """)
    List<Connection> findAllByStudentId(@Param("studentId") Long studentId);

    @Query("""
            SELECT c FROM Connection c
            WHERE c.studentOne.id = :studentId OR c.studentTwo.id = :studentId
            """)
    Page<Connection> findAllByStudentId(@Param("studentId") Long studentId, Pageable pageable);

    @Query("""
            SELECT COUNT(c) > 0 FROM Connection c
            WHERE (c.studentOne.id = :studentAId AND c.studentTwo.id = :studentBId)
               OR (c.studentOne.id = :studentBId AND c.studentTwo.id = :studentAId)
            """)
    boolean existsBetweenStudents(@Param("studentAId") Long studentAId, @Param("studentBId") Long studentBId);

    @Query("""
            SELECT c FROM Connection c
            WHERE (c.studentOne.id = :studentAId AND c.studentTwo.id = :studentBId)
               OR (c.studentOne.id = :studentBId AND c.studentTwo.id = :studentAId)
            """)
    Optional<Connection> findBetweenStudents(@Param("studentAId") Long studentAId, @Param("studentBId") Long studentBId);
}
