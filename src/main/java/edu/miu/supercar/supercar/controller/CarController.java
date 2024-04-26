package edu.miu.supercar.supercar.controller;

import edu.miu.supercar.supercar.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import edu.miu.supercar.supercar.model.Car;

import java.util.List;

@RestController
@RequestMapping("/car")
public class CarController {
    @Autowired
    private CarService carService;

    @PostMapping("/create")
    public ResponseEntity<Car> createCar(@RequestBody Car car){
            Car createdCar = carService.createCar(car);
            return new ResponseEntity<>(createdCar, HttpStatus.CREATED);
    }
    @GetMapping("/list")
    public ResponseEntity<List<Car>> getCars(){
        List<Car> cars= carService.getAllCars();
        return new ResponseEntity<>(cars,HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Car> deleteCar(@PathVariable Long id){
        carService.deleteCar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
