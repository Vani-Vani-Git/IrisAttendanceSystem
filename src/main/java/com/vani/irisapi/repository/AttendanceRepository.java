package com.vani.irisapi.repository;
import com.vani.irisapi.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.Optional;
import java.util.List;
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByStudentIdAndDate(String studentId, LocalDate date);
    List<Attendance> findByStudentId(String studentId);
    long countByDate(LocalDate date);
    long countByDateAndStatus(LocalDate date, String status);

    List<Attendance> findByDate(LocalDate now);
}
