package com.myroki.todolist.restdocs;

import com.google.gson.Gson;
import com.myroki.todolist.todo.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class TodoControllerRestDocs {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    @MockBean
    private TodoMapper mapper;

    @Autowired
    private Gson gson;


    @Test
    void postList() throws Exception {
        // given
        TodoDto.Post post = new TodoDto.Post("자바 공부하기", 1);
        TodoDto.Response response = new TodoDto.Response(1, "자바 공부하기", 1, false);
        String content = gson.toJson(post).replace("todoOrder", "order"); // request body가 @JsonProperty 어노테이션의 적용을 받지 않O

        given(mapper.todoPostDtoToTodo(Mockito.any(TodoDto.Post.class))).willReturn(new Todo());
        given(todoService.createTodo(Mockito.any(Todo.class))).willReturn(new Todo());
        given(mapper.todoToTodoResponseDto(Mockito.any(Todo.class))).willReturn(response);

        // when
        ResultActions actions = mockMvc.perform(post("/")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));

        // then
        actions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(post.getTitle()))
                .andExpect(jsonPath("$.order").value(post.getTodoOrder()))
                .andExpect(jsonPath("$.completed").value(false))
                .andDo(document("post-todo",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                List.of(fieldWithPath("title").type(JsonFieldType.STRING).description("할 일 제목").attributes(key("constraints").value("Not Empty")),
                                        fieldWithPath("order").type(JsonFieldType.NUMBER).description("우선 순위").attributes(key("constraints").value("Not Null, min: 1, max: 4"))
                                )
                        ),
                        responseFields(
                                List.of(fieldWithPath("id").type(JsonFieldType.NUMBER).description("고유 식별자"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("할 일 제목"),
                                        fieldWithPath("order").type(JsonFieldType.NUMBER).description("우선 순위"),
                                        fieldWithPath("completed").type(JsonFieldType.BOOLEAN).description("완료 여부")
                                )
                        )
                ));
    }

    @Test
    void getAllList() throws Exception {
        // given
        List<TodoDto.Response> responses = List.of(
                new TodoDto.Response(1, "자바 공부하기", 1, false),
                new TodoDto.Response(2, "파이썬 공부하기", 2, false)
        );

        given(todoService.getAllTodoList()).willReturn(new ArrayList<>());
        given(mapper.todoToTodoResponseDto(Mockito.anyList())).willReturn(responses);

        // when
        ResultActions actions = mockMvc.perform(get("/")
                .accept(MediaType.APPLICATION_JSON));

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andDo(document("get-all-todo",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                List.of(fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("고유 식별자"),
                                        fieldWithPath("[].title").type(JsonFieldType.STRING).description("할 일 제목"),
                                        fieldWithPath("[].order").type(JsonFieldType.NUMBER).description("우선 순위"),
                                        fieldWithPath("[].completed").type(JsonFieldType.BOOLEAN).description("완료 여부")
                                ))
                ));
    }

    @Test
    void getTodoList() throws Exception {
        // given
        long todolistId = 1L;
        TodoDto.Response response = new TodoDto.Response(todolistId, "자바 공부하기", 1, false);

        given(todoService.findTodoById(Mockito.anyLong())).willReturn(new Todo());
        given(mapper.todoToTodoResponseDto(Mockito.any(Todo.class))).willReturn(response);

        // when
        ResultActions actions = mockMvc.perform(get("/{id}", todolistId)
                .accept(MediaType.APPLICATION_JSON));

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.title").value(response.getTitle()))
                .andExpect(jsonPath("$.order").value(response.getTodoOrder()))
                .andExpect(jsonPath("$.completed").value(response.isCompleted()))
                .andDo(document("get-todo",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(List.of(
                                parameterWithName("id").description("고유 식별자"))),
                        responseFields(
                                List.of(fieldWithPath("id").type(JsonFieldType.NUMBER).description("고유 식별자"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("할 일 제목"),
                                        fieldWithPath("order").type(JsonFieldType.NUMBER).description("우선 순위"),
                                        fieldWithPath("completed").type(JsonFieldType.BOOLEAN).description("완료 여부")
                                )
                        )
                ));
    }

    @Test
    void patchTodoList() throws Exception {
        // given
        long todolistId = 1L;
        TodoDto.Patch patch = new TodoDto.Patch("자바 공부하기", 1, true);
        TodoDto.Response response = new TodoDto.Response(todolistId, "자바 공부하기", 1, true);
        String content = gson.toJson(patch).replace("todoOrder", "order");

        given(mapper.todoPatchDtoToTodo(Mockito.any(TodoDto.Patch.class))).willReturn(new Todo());
        given(todoService.updateTodo(Mockito.any(Todo.class))).willReturn(new Todo());
        given(mapper.todoToTodoResponseDto(Mockito.any(Todo.class))).willReturn(response);

        // when
        ResultActions actions = mockMvc.perform(patch("/{id}", todolistId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));

        //then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.title").value(response.getTitle()))
                .andExpect(jsonPath("$.order").value(response.getTodoOrder()))
                .andExpect(jsonPath("$.completed").value(response.isCompleted()))
                .andDo(document("patch-todo",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(List.of(
                                parameterWithName("id").description("고유 식별자"))),
                        requestFields(
                                List.of(fieldWithPath("title").type(JsonFieldType.STRING).description("할 일 제목").optional(),
                                        fieldWithPath("order").type(JsonFieldType.NUMBER).description("우선 순위").optional().attributes(key("constraints").value("min: 1, max: 4")),
                                        fieldWithPath("completed").type(JsonFieldType.BOOLEAN).description("완료 여부").optional()
                                )
                        ),
                        responseFields(
                                List.of(fieldWithPath("id").type(JsonFieldType.NUMBER).description("고유 식별자"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("할 일 제목"),
                                        fieldWithPath("order").type(JsonFieldType.NUMBER).description("우선 순위"),
                                        fieldWithPath("completed").type(JsonFieldType.BOOLEAN).description("완료 여부")
                                )
                        )
                ));
    }

    @Test
    void deleteAll() throws Exception {
        // given
        doNothing().when(todoService).deleteAllTodoList();

        // when
        ResultActions actions = mockMvc.perform(delete("/"));

        // then
        actions.andExpect(status().isAccepted())
                .andDo(document("delete-all-todo",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    void deleteTodoList() throws Exception {
        // given
        long todolistId = 1L;
        doNothing().when(todoService).deleteTodoById(Mockito.anyLong());

        // when
        ResultActions actions = mockMvc.perform(delete("/{id}", todolistId));

        // then
        actions.andExpect(status().isNoContent())
                .andDo(document("delete-todo",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(List.of(parameterWithName("id").description("고유 식별자")))
                ));
    }
}