package com.example.demo.todo;

import com.example.demo.model.security.LoginData;
import com.example.demo.model.security.LoginResponse;
import com.example.demo.model.user.UserCreationDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void integrationTest(){
        ResponseEntity<Todo[]> todoArrayResponse;
        ResponseEntity<Todo> todoResponse;

        ResponseEntity<Void> userCreationResponse = restTemplate.postForEntity("/api/user", new UserCreationDTO("user", "pw", "pw"), Void.class);
        assertThat(userCreationResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<LoginResponse> loginResponse = restTemplate.postForEntity("/api/login", new LoginData("user", "pw"), LoginResponse.class);
        String jwt = loginResponse.getBody().getToken();

        todoArrayResponse = restTemplate.exchange(
                "/api/kanban",
                HttpMethod.GET,
                new HttpEntity<>(createHeaders(jwt)),
                Todo[].class
        );

        // initial get todos via api should be empty
        assertThat(todoArrayResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(todoArrayResponse.getBody()).isEmpty();

//        // add 3 todos via api
//        Todo todo1 = new Todo("1", "test todo 1", "todo 1", TodoStatus.OPEN);
//        Todo todo2 = new Todo("2", "test todo 2", "todo 2", TodoStatus.IN_PROGRESS);
//        Todo todo3 = new Todo("3", "test todo 3", "todo 3", TodoStatus.DONE);
//        var todos = new Todo[]{todo1, todo2, todo3};
//        for(Todo t: todos){
//            todoResponse = restTemplate.postForEntity("/api/kanban", t, Todo.class);
//            assertThat(todoResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//            assertThat(todoResponse.getBody()).isEqualTo(t);
//        }
//
//        // add bad todo
//        Todo todoForBadRequest = new Todo(null, null, null, null);
//        todoResponse = restTemplate.postForEntity("/api/kanban", todoForBadRequest, Todo.class);
//        assertThat(todoResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
//        assertThat(todoResponse.getBody()).isEqualTo(todoForBadRequest);
//
//        todoForBadRequest = new Todo("1", "", "", null);
//        todoResponse = restTemplate.postForEntity("/api/kanban", todoForBadRequest, Todo.class);
//        assertThat(todoResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
//        assertThat(todoResponse.getBody()).isEqualTo(todoForBadRequest);
//
//
//        // find those todos again via api
//        todoArrayResponse = restTemplate.getForEntity("/api/kanban", Todo[].class);
//        assertThat(todoArrayResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(todoArrayResponse.getBody()).containsExactlyInAnyOrderElementsOf(Arrays.asList(todos));
//
//        // delete one todo
//        restTemplate.delete("/api/kanban/" + todo3.getId());
//
//        // check if deleted
//        todoArrayResponse = restTemplate.getForEntity("/api/kanban", Todo[].class);
//        assertThat(todoArrayResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(todoArrayResponse.getBody()).containsExactlyInAnyOrderElementsOf(List.of(todo1, todo2));
//
//        // delete todo2 with restTemplate.exchange for statusCode AND return etc
//        todoResponse = restTemplate.exchange("/api/kanban/{id}", HttpMethod.DELETE, new HttpEntity<>(todo2), Todo.class, todo2.getId());
//        assertThat(todoResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
//
//        // check if deleted
//        todoArrayResponse = restTemplate.getForEntity("/api/kanban", Todo[].class);
//        assertThat(todoArrayResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(todoArrayResponse.getBody()).containsExactlyInAnyOrderElementsOf(List.of(todo1));
//
//        // try to delete not existent todo
//        todoResponse = restTemplate.exchange("/api/kanban/{id}", HttpMethod.DELETE, new HttpEntity<>(todo2), Todo.class, todo2.getId());
//        assertThat(todoResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//        assertThat(todoResponse.getBody()).isNull();
//
//        // move task to next status
//        todoResponse = restTemplate.exchange("/api/kanban/next", HttpMethod.PUT, new HttpEntity<>(todo1), Todo.class);
//        assertThat(todoResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
//        // using getTodoById api here
//        todoResponse = restTemplate.getForEntity("/api/kanban/" + todo1.getId(), Todo.class);
//        assertThat(todoResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(todoResponse.getBody().getStatus()).isEqualTo(TodoStatus.IN_PROGRESS);
//
//        // move task to prev status
//        todoResponse = restTemplate.exchange("/api/kanban/prev", HttpMethod.PUT, new HttpEntity<>(todo1), Todo.class);
//        assertThat(todoResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
//        // using getTodoById api here
//        todoResponse = restTemplate.getForEntity("/api/kanban/" + todo1.getId(), Todo.class);
//        assertThat(todoResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(todoResponse.getBody().getStatus()).isEqualTo(TodoStatus.OPEN);
//
//        // same but for todo not in db
//        todoResponse = restTemplate.exchange("/api/kanban/next", HttpMethod.PUT, new HttpEntity<>(todo2), Todo.class);
//        assertThat(todoResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//        assertThat(todoResponse.getBody()).isNull();
//
//        // getTodoById for not existent
//        todoResponse = restTemplate.getForEntity("/api/kanban/" + todo2.getId(), Todo.class);
//        assertThat(todoResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//        assertThat(todoResponse.getBody()).isNull();
//
//        // change todo1 and save to db
//        todo1.setTask("the new task");
//        todoResponse = restTemplate.exchange("/api/kanban", HttpMethod.PUT, new HttpEntity<>(todo1), Todo.class);
//        assertThat(todoResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(todoResponse.getBody().getId()).isEqualTo(todo1.getId());
//        todoResponse = restTemplate.getForEntity("/api/kanban/" + todo1.getId(), Todo.class);
//        assertThat(todoResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(todoResponse.getBody().getTask()).isEqualTo("the new task");
//
//        // try to save task changes not in db
//        todoResponse = restTemplate.exchange("/api/kanban", HttpMethod.PUT, new HttpEntity<>(todo2), Todo.class);
//        assertThat(todoResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//        assertThat(todoResponse.getBody()).isNull();
    }

    private HttpHeaders createHeaders(String jwt) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);
        return headers;
    }
}
