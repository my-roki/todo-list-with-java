package com.myroki.todolist.todo;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TodoMapper {
    Todo todoPostDtoToTodo(TodoDto.Post post);

    Todo todoPatchDtoToTodo(TodoDto.Patch patch);

    default TodoDto.Response todoToTodoResponseDto(Todo todo, String domain) {
        TodoDto.Response response = TodoDto.Response.builder()
                .id(todo.getId())
                .title(todo.getTitle())
                .todoOrder(todo.getTodoOrder())
                .completed(todo.getCompleted())
                .url(domain + todo.getId())
                .build();

        return response;
    }

    default List<TodoDto.Response> todoToTodoResponseDto(List<Todo> todo, String domain){
        List<TodoDto.Response> responses = todo.stream()
                .map(t -> todoToTodoResponseDto(t, domain))
                .collect(Collectors.toList());

        return responses;
    }
}