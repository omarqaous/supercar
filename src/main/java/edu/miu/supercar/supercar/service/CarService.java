package edu.miu.supercar.supercar.service;

import edu.miu.supercar.supercar.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import edu.miu.supercar.supercar.model.Car;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CarService {
    @Autowired
    private CarRepository carRepository;
    public Car createCar(Car car) {
        return carRepository.save(car);
    }
    public Optional<Car> getCartByID(Long id){
        return carRepository.findById(id);
    }

    public List<Car> getAllCars(){
        return carRepository.findAll();
    }
    public List<Car> getAvailableCars(LocalDate startDate, LocalDate endDate) {
        // JPQL query to find available cars between given dates
        return carRepository.findAvailableCarsByDateRange(startDate, endDate);
    }
    public Car updateCar(Long id, Car updatedCar){
        Optional<Car> existingCarOptional= carRepository.findById(id);
        if (existingCarOptional.isPresent()) {
            Car existingCar = existingCarOptional.get();
            existingCar.setMake(updatedCar.getMake());
            existingCar.setModel(updatedCar.getModel());
            existingCar.setYear(updatedCar.getYear());
            existingCar.setRentalPrice(updatedCar.getRentalPrice());
            existingCar.setAvailable(updatedCar.isAvailable());
            return carRepository.save(existingCar);
        } else {
            throw new RuntimeException("Car not found with id: " + id);
        }
    }

    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

}
