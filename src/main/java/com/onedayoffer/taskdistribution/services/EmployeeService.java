package com.onedayoffer.taskdistribution.services;

import com.onedayoffer.taskdistribution.DTO.EmployeeDTO;
import com.onedayoffer.taskdistribution.DTO.TaskDTO;
import com.onedayoffer.taskdistribution.DTO.TaskStatus;
import com.onedayoffer.taskdistribution.repositories.EmployeeRepository;
import com.onedayoffer.taskdistribution.repositories.TaskRepository;
import com.onedayoffer.taskdistribution.repositories.entities.Employee;
import com.onedayoffer.taskdistribution.repositories.entities.Task;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;

    public List<EmployeeDTO> getEmployees(@Nullable String sortDirection) {

        List<Employee> employeeList = employeeRepository.findAllAndSort(Sort.by(sortDirection));

        if (employeeList != null) {
            return employeeList.stream().map(this::getEmployeeDto).collect(Collectors.toList());
        }

        throw new java.lang.UnsupportedOperationException("implement getEmployees");

        // if sortDirection.isPresent() ..
        // Sort.Direction direction = ...
        // employees = employeeRepository.findAllAndSort(Sort.by(direction, "fio"))
        // employees = employeeRepository.findAll()
        // Type listType = new TypeToken<List<EmployeeDTO>>() {}.getType()
        // List<EmployeeDTO> employeeDTOS = modelMapper.map(employees, listType)
    }

    @Transactional
    public EmployeeDTO getOneEmployee(Integer id) {
        return employeeRepository.findById(id).map(this::getEmployeeDto)
                .orElseThrow(() -> new java.lang.UnsupportedOperationException("implement getOneEmployee"));
    }

    public List<TaskDTO> getTasksByEmployeeId(Integer id) {
        return employeeRepository.findById(id).map(empl -> getTaskDto(empl.getTasks()))
                .orElseThrow(() -> new java.lang.UnsupportedOperationException("implement getOneEmployee"));
    }

    @Transactional
    public void changeTaskStatus(Integer taskId, TaskStatus status) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);

        if(taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setStatus(status);
            taskRepository.saveAndFlush(task);
        } else {
            throw new java.lang.UnsupportedOperationException("implement changeTaskStatus");
        }
    }

    @Transactional
    public void postNewTask(Integer id, TaskDTO newTask) {

        Optional<Employee> emp = employeeRepository.findById(id);

        if (emp.isPresent()) {
            Employee employee = emp.get();

        } else {
            throw new java.lang.UnsupportedOperationException("implement postNewTask");
        }
    }

    //маппер в дто
    //TODO: переделать на Mapper

    private EmployeeDTO getEmployeeDto(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setFio(employee.getFio());
        dto.setJobTitle(employee.getJobTitle());
        dto.setTasks( getTaskDto(employee.getTasks()));

        return dto;
    }

    private Task createTask(TaskDTO taskDto, Employee employee) {
        Task task = new Task();
        task.setId(taskDto.getId());
        task.setName(taskDto.getName());
        task.setEmployee(employee);
        task.setTaskType(taskDto.getTaskType());
        task.setStatus(taskDto.getStatus());
        task.setPriority(taskDto.getPriority());
        task.setLeadTime(taskDto.getLeadTime());
        return task;
    }

    private List<TaskDTO> getTaskDto(List<Task> tasks) {
        List<TaskDTO> taskDTOList = new ArrayList<>();
        if (tasks.isEmpty()) {
            return taskDTOList;
        }

        for(Task task : tasks) {
            TaskDTO dto = new TaskDTO();
            dto.setId(task.getId());
            dto.setName(task.getName());
            dto.setTaskType(task.getTaskType());
            dto.setStatus(task.getStatus());
            dto.setPriority(task.getPriority());
            dto.setLeadTime(task.getLeadTime());
            taskDTOList.add(dto);
        }
        return taskDTOList;
    }
}
