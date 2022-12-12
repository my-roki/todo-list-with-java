package com.myroki.todolist.todo;

import com.myroki.todolist.exception.BusinessLogicException;
import com.myroki.todolist.exception.ExceptionCode;
import com.myroki.todolist.utils.BeanUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class TodoService {
    private final TodoRepository todoRepository;
    private final BeanUtils<Todo> beanUtils;

    public Todo createTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    @Transactional(readOnly = true)
    public List<Todo> getAllTodoList() {
        return todoRepository.findAll();
    }

    public Todo findTodoById(long id) {
        Optional<Todo> optionalTodo = todoRepository.findById(id);
        Todo todo = optionalTodo.orElseThrow(() -> new BusinessLogicException(ExceptionCode.TODO_NOT_FOUND));

        return todo;
    }

    public Todo updateTodo(Todo todo) {
        Todo findTodo = findTodoById(todo.getId());
        beanUtils.copyNonNullProperties(todo, findTodo);

        return todoRepository.save(findTodo);
    }

    public void deleteAllTodoList() {
        todoRepository.deleteAll();
    }

    public void deleteTodoById(long id) {
        verifyTodoExist(id);
        todoRepository.deleteById(id);
    }

    private void verifyTodoExist(long id) {
        boolean exists = todoRepository.existsById(id);
        if (!exists) throw new BusinessLogicException(ExceptionCode.TODO_NOT_FOUND);
    }

}