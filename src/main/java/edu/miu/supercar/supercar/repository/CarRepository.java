package edu.miu.supercar.supercar.repository;

import edu.miu.supercar.supercar.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    @Query("SELECT c FROM Car c WHERE c.available = TRUE")
    List<Car> findAvailableCars();

    @Query("SELECT c FROM Car c WHERE c.available = TRUE AND c.id NOT IN ( "
            + "SELECT b.car.id FROM Booking b WHERE "
            + "(b.startDate <= :endDate AND b.endDate >= :startDate) OR "
            + "(b.startDate BETWEEN :startDate AND :endDate) OR "
            + "(b.endDate BETWEEN :startDate AND :endDate)"
            + ")"
    )
    List<Car> findAvailableCarsByDateRange(LocalDate startDate, LocalDate endDate);

}
