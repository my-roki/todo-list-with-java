package com.myroki.todolist.todo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Validated
@CrossOrigin(origins = {"https://www.todobackend.com/", "https://todobackend.com/"},
        methods = {RequestMethod.PATCH, RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE, RequestMethod.OPTIONS},
        allowedHeaders = {"x-requested-with", "origin", "content-type", "accept"})
public class TodoController {
    @Value("${server.domain}")
    private String domain;

    private final TodoService todoService;
    private final TodoMapper mapper;

    public TodoController(TodoService todoService, TodoMapper mapper) {
        this.todoService = todoService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity postList(@RequestBody @Valid TodoDto.Post requestBody) {
        Todo todo = mapper.todoPostDtoToTodo(requestBody);
        Todo serviceTodo = todoService.createTodo(todo);
        TodoDto.Response response = mapper.todoToTodoResponseDto(serviceTodo, domain);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity getAllList() {
        List<Todo> todoList = todoService.getAllTodoList();
        List<TodoDto.Response> response = mapper.todoToTodoResponseDto(todoList, domain);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getTodoList(@PathVariable("id") @Positive long todolistId) {
        Todo todo = todoService.findTodoById(todolistId);
        TodoDto.Response response = mapper.todoToTodoResponseDto(todo, domain);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity patchTodoList(@PathVariable("id") @Positive long todolistId,
                                        @RequestBody @Valid TodoDto.Patch requestBody) {
        Todo todo = mapper.todoPatchDtoToTodo(requestBody);
        todo.setId(todolistId);
        Todo updateTodo = todoService.updateTodo(todo);
        TodoDto.Response response = mapper.todoToTodoResponseDto(updateTodo, domain);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity deleteAll() {
        todoService.deleteAllTodoList();

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteTodoList(@PathVariable("id") @Positive long todolistId) {
        todoService.deleteTodoById(todolistId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}