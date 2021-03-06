package de.home.mayumi.practice.controller;

import de.home.mayumi.practice.common.ResponseMessage;
import de.home.mayumi.practice.domain.CreateResponseMessage;
import de.home.mayumi.practice.domain.EmployeeData;
import de.home.mayumi.practice.domain.SearchCriteria;
import de.home.mayumi.practice.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static de.home.mayumi.practice.common.ResultState.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@Validated
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/employee")
    public ResponseEntity<ResponseMessage<EmployeeData>> createEmployee(@RequestBody @Valid EmployeeData employeeData) {

        CreateResponseMessage result = employeeService.createNewEmployee(employeeData);

        EmployeeData employee = result.getEmployee();

        ResponseMessage<EmployeeData> message = ResponseMessage.<EmployeeData>builder()
                .message("Created")
                .data(employee)
                .build();
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PatchMapping("/employee/{employeeId}")
    public ResponseEntity<ResponseMessage<EmployeeData>> updateEmployee(@PathVariable String employeeId, @RequestBody @Valid EmployeeData employeeData) {

        CreateResponseMessage result = employeeService.updateEmployee(employeeId, employeeData);

        if (NOT_FOUND.equals(result.getResultState())) {

            ResponseMessage<EmployeeData> message = ResponseMessage.<EmployeeData>builder()
                    .message("No matching employee has been found")
                    .build();

            return ResponseEntity.notFound().build();
        }

        ResponseMessage<EmployeeData> message = ResponseMessage.<EmployeeData>builder()
                .message("Created")
                .data(result.getEmployee())
                .build();
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeData>> getEmployees(SearchCriteria searchCriteria) {

        List<EmployeeData> employees = employeeService.getEmployees(searchCriteria);

        return ResponseEntity.ok(employees);
    }

}
