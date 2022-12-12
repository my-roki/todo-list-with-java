package com.myroki.todolist.todo;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TodoMapper {
    Todo todoPostDtoToTodo(TodoDto.Post post);

    Todo todoPatchDtoToTodo(TodoDto.Patch patch);

    TodoDto.Response todoToTodoResponseDto(Todo todo);

    List<TodoDto.Response> todoToTodoResponseDto(List<Todo> todo);
}