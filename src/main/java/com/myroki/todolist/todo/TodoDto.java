package com.myroki.todolist.todo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class TodoDto {
    @Getter
    public static class Post {
        private String title;

        @JsonProperty("todo_order")
        private Integer todoOrder;
    }

    @Getter
    public static class Patch {
        private String title;

        @JsonProperty("todo_order")
        private Integer todoOrder;
        private boolean completed;
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private long id;
        private String title;

        @JsonProperty("todo_order")
        private Integer todoOrder;
        private boolean completed;
    }
}