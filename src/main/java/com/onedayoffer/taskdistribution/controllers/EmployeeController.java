package com.onedayoffer.taskdistribution.controllers;

import com.onedayoffer.taskdistribution.DTO.EmployeeDTO;
import com.onedayoffer.taskdistribution.DTO.TaskDTO;
import com.onedayoffer.taskdistribution.DTO.TaskStatus;
import com.onedayoffer.taskdistribution.repositories.entities.Task;
import com.onedayoffer.taskdistribution.services.EmployeeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "employees")
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeDTO> getEmployees(@RequestParam(required = false) String sort) {
        return employeeService.getEmployees(sort);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeDTO getOneEmployee(@PathVariable @NonNull Integer id) {
        return employeeService.getOneEmployee(id);
    }

    @GetMapping("{id}/tasks")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskDTO> getTasksByEmployeeId(@PathVariable Integer id) {
        return employeeService.getTasksByEmployeeId(id);
    }

    @PatchMapping("{id}/tasks/{taskId}/status")
    @ResponseStatus(HttpStatus.OK)
    public void changeTaskStatus(@PathVariable Integer id, @PathVariable String newStatus
                                          /* other PathVariable and RequestParam */ ) {

        TaskStatus status = TaskStatus.valueOf(newStatus);
        employeeService.changeTaskStatus(id, status);


        //employeeService.changeTaskStatus ...
    }

    @PostMapping("/{id}/create/newTasks/{task}")
    @ResponseStatus(HttpStatus.CREATED)
    public void postNewTask(@PathVariable Integer id, @Valid @RequestBody TaskDTO taskDto) {

        employeeService.postNewTask(id, taskDto);

        //employeeService.postNewTask ...
    }
}