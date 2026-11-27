package com.skillbridge.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(
        name = "connections",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_connections_pair",
                        columnNames = {"student_one_id", "student_two_id"}
                )
        },
        indexes = {
                @Index(name = "idx_connections_student_one", columnList = "student_one_id"),
                @Index(name = "idx_connections_student_two", columnList = "student_two_id")
        }
)
public class Connection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_one_id", nullable = false, foreignKey = @ForeignKey(name = "fk_connections_student_one"))
    private Student studentOne;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_two_id", nullable = false, foreignKey = @ForeignKey(name = "fk_connections_student_two"))
    private Student studentTwo;

    @CreationTimestamp
    @Column(name = "connected_at", nullable = false, updatable = false)
    private LocalDateTime connectedAt;

    public Connection() {
    }

    public Connection(Student studentOne, Student studentTwo) {
        this.studentOne = studentOne;
        this.studentTwo = studentTwo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Student getStudentOne() { return studentOne; }
    public void setStudentOne(Student studentOne) { this.studentOne = studentOne; }

    public Student getStudentTwo() { return studentTwo; }
    public void setStudentTwo(Student studentTwo) { this.studentTwo = studentTwo; }

    public LocalDateTime getConnectedAt() { return connectedAt; }
    public void setConnectedAt(LocalDateTime connectedAt) { this.connectedAt = connectedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Connection that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
