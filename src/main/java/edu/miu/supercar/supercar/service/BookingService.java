package edu.miu.supercar.supercar.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import edu.miu.supercar.supercar.DTO.BookingCreateDto;
import edu.miu.supercar.supercar.model.Car;
import edu.miu.supercar.supercar.model.User;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.miu.supercar.supercar.model.Booking;
import edu.miu.supercar.supercar.model.BookingStatus
        ;
import edu.miu.supercar.supercar.repository.*;

@Service
@Transactional
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CarService carService;

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    public Booking createBooking(BookingCreateDto bookingDto) {
        Booking booking = new Booking();
        // Set other booking fields based on bookingDto
        User user = userService.getUserById(bookingDto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        booking.setUser(user);
        Car car = carService.getCartByID(bookingDto.getCarId()).orElseThrow(() -> new RuntimeException("Car not found"));
        if (car.isAvailable()) {
            booking.setCar(car);
            booking.setStartDate(bookingDto.getStartDate());
            booking.setEndDate(bookingDto.getEndDate());
            double dailyRate = carService.getCartByID(bookingDto.getCarId()).orElseThrow(() -> new RuntimeException("Car not found")).getRentalPrice();
            long days = ChronoUnit.DAYS.between(booking.getStartDate(), booking.getEndDate());
            booking.setTotalCost(days * dailyRate);
            booking.setStatus(BookingStatus.PENDING);
            return bookingRepository.save(booking);
        } else {

            throw new RuntimeException("Car is not available");
        }
    }

    public Booking updateBooking(Booking booking) {
        Optional<Booking> existingBookingOptional = bookingRepository.findById(booking.getId());
        if (existingBookingOptional.isPresent()) {
            Booking existingBooking = existingBookingOptional.get();
            // Update relevant fields (e.g., user, car, dates, status, total cost)
            existingBooking.setUser(booking.getUser());
            existingBooking.setCar(booking.getCar());
            existingBooking.setStartDate(booking.getStartDate());
            existingBooking.setEndDate(booking.getEndDate());
            existingBooking.setTotalCost(booking.getTotalCost());
            existingBooking.setStatus(booking.getStatus());
            return bookingRepository.save(existingBooking);
        } else {
            throw new RuntimeException("Booking not found with id: " + booking.getId());
        }
    }

    public Booking extendBooking(Long bookingId, LocalDate newEndDate) {
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        if (bookingOptional.isPresent()) {
            Booking booking = bookingOptional.get();

            // Validate new end date
            validateNewEndDate(booking, newEndDate);

            booking.setEndDate(newEndDate);
            // Recalculate total cost based on the extended duration
            double dailyRate = bookingRepository.findById(bookingId).get().getCar().getRentalPrice();
            long days = ChronoUnit.DAYS.between(booking.getStartDate(), booking.getEndDate());
            booking.setTotalCost(days * dailyRate);

            return bookingRepository.save(booking);
        } else {
            throw new RuntimeException("Booking not found with id: " + bookingId);
        }
    }

    private void validateNewEndDate(Booking booking, LocalDate newEndDate) {
        // Check if new end date is in the future and after the current end date
        if (!newEndDate.isAfter(LocalDate.now())) {
            throw new RuntimeException("New end date must be in the future");
        }
        if (!newEndDate.isAfter(booking.getEndDate())) {
            throw new RuntimeException("New end date must be after the current end date");
        }
    }


    public List<Car> getAvailableCars(LocalDate startDate, LocalDate endDate) {
        return carService.getAvailableCars(startDate, endDate);
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
}