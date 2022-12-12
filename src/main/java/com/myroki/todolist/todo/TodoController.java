package com.myroki.todolist.todo;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class TodoController {
    private final TodoService todoService;
    private final TodoMapper mapper;

    @PostMapping
    public ResponseEntity postList(@RequestBody TodoDto.Post requestBody) {
        Todo todo = mapper.todoPostDtoToTodo(requestBody);
        Todo serviceTodo = todoService.createTodo(todo);
        TodoDto.Response response = mapper.todoToTodoResponseDto(serviceTodo);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity getAllList() {
        List<Todo> todoList = todoService.getAllTodoList();
        List<TodoDto.Response> response = mapper.todoToTodoResponseDto(todoList);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getTodoList(@PathVariable("id") long todolistId) {
        Todo todo = todoService.findTodoById(todolistId);
        TodoDto.Response response = mapper.todoToTodoResponseDto(todo);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity patchTodoList(@PathVariable("id") long todolistId,
                                        @RequestBody TodoDto.Patch requestBody) {
        Todo todo = mapper.todoPatchDtoToTodo(requestBody);
        todo.setId(todolistId);
        Todo updateTodo = todoService.updateTodo(todo);
        TodoDto.Response response = mapper.todoToTodoResponseDto(updateTodo);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity deleteAll() {
        todoService.deleteAllTodoList();

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteTodoList(@PathVariable("id") long todolistId) {
        todoService.deleteTodoById(todolistId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}