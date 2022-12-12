package com.myroki.todolist.todo;

import com.myroki.todolist.audit.Auditable;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Todo extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer todoOrder;

    @Column(nullable = false)
    private Boolean completed = false;
}