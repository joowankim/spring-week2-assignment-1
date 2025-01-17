package com.codesoom.assignment.controllers;

import com.codesoom.assignment.dto.Task;
import com.codesoom.assignment.generators.IdGenerator;
import com.codesoom.assignment.repositories.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 할 일에 대한 HTTP request를 처리합니다.
 */
@RestController
@RequestMapping(path = "/tasks")
@CrossOrigin
public class TaskController {

    private final TaskRepository taskRepository;
    private final IdGenerator taskIdGenerator;

    public TaskController(TaskRepository taskRepository, IdGenerator unsignedLongGenerator) {
        this.taskRepository = taskRepository;
        this.taskIdGenerator = unsignedLongGenerator;
    }

    /**
     * 모든 할 일의 리스트를 반환한다.
     *
     * @return 할 일 목록
     */
    @GetMapping
    public List<Task> getAllTasks() {
        return this.taskRepository.findAll();
    }

    /**
     * 새로운 할 일을 생성해 리턴합니다.
     *
     * @param task 생성할 할 일 정보
     * @return 새로 생성된 할 일
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(@RequestBody Task task) {
        task.setId(taskIdGenerator.generateId());
        return this.taskRepository.addTask(task);
    }

    /**
     * 요청된 id를 가진 할 일 인스턴스를 반환한다.
     *
     * @param id 조회 대상 할 일의 id
     * @return 조회된 할 일 인스턴스
     */
    @GetMapping(path = "/{id}")
    public Task getTask(@PathVariable Long id) {
        return this.taskRepository.getOneTask(id);
    }

    /**
     * PathVariable로 요청된 id를 가진 할 일 인스턴스의 속성을 재설정한다.
     *
     * @param id 유저가 속성을 재설정하기 원하는 할 일의 id이다.
     * @param task RequestBody의 content로 구성된 할 일 인스턴스다.
     * @return 재설정이 끝난 할 일 인스턴스를 반환한다.
     */
    @PutMapping(path = "/{id}")
    public Task setTask(@PathVariable Long id, @RequestBody Task task) {
        task.setId(id);
        return this.taskRepository.setOneTask(task);
    }

    /**
     * PathVariable로 요청된 id를 가진 할 일 인스턴스의 정보를 부분적으로 변경한다.
     *
     * @param id 유저가 정보 변경을 원하는 할 일의 id이다.
     * @param task RequestBody의 content로 구성된 할 일 인스턴스다.
     * @return 정보가 변경된 할 일 인스턴스를 반환한다.
     */
    @PatchMapping(path = "/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task task) {
        task.setId(id);
        return this.taskRepository.updateOneTask(task);
    }

    /**
     * PathVariable로 요청된 id를 가진 할 일 인스턴스를 삭제한다.
     *
     * @param id 유저가 삭제를 원하는 할 일의 id이다.
     */
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeTask(@PathVariable Long id) {
        this.taskRepository.removeOneTask(id);
    }
}
