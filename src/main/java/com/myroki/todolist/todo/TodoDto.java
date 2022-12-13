package com.myroki.todolist.todo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class TodoDto {
    @Getter
    @AllArgsConstructor
    public static class Post {
        @NotEmpty
        private String title;

        @JsonProperty("order")
        @NotNull
        @Range(min = 1, max = 4)
        private Integer todoOrder;
    }

    @Getter
    @AllArgsConstructor
    public static class Patch {
        private String title;

        @JsonProperty("order")
        @Range(min = 1, max = 4)
        private Integer todoOrder;

        // TODO true / false 외에 어떠한 문자도 받을 수 없게 검증하는 방법은 없을까?
        private boolean completed;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class Response {
        private long id;
        private String title;

        @JsonProperty("order")
        private Integer todoOrder;
        private boolean completed;
        private String url;
    }
}