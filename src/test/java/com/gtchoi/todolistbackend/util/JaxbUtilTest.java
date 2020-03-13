package com.gtchoi.todolistbackend.util;

import com.gtchoi.todolistbackend.model.*;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class JaxbUtilTest {


    @Test
    public void marshal() throws JAXBException {

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken("access token");
        loginResponse.setLogin(true);
        loginResponse.setMessage("message");
        loginResponse.setUserId("gtchoi");

        boolean canMarshal = JaxbUtil.canMarshal(loginResponse, LoginResponse.class);
        String marshal = JaxbUtil.marshal(loginResponse, LoginResponse.class);

        System.out.println("expected:");
        System.out.println(marshal);

        assertThat(canMarshal, is(true));
    }

    @Test
    public void unmarshal() throws JAXBException {
        String xmlString = "<loginResponse>\n" +
                "    <accessToken>access token</accessToken>\n" +
                "    <login>true</login>\n" +
                "    <message>message</message>\n" +
                "    <userId>gtchoi</userId>\n" +
                "</loginResponse>";

        boolean canUnmarshal = JaxbUtil.canUnmarshal(xmlString, LoginResponse.class);
        boolean cannotUnmarshal = JaxbUtil.canUnmarshal(xmlString, ErrorResponse.class);
        LoginResponse loginResponse = (LoginResponse) JaxbUtil.unmarshal(xmlString, LoginResponse.class);

        assertThat(cannotUnmarshal, is(false));
        assertThat(canUnmarshal, is(true));
        assertThat(loginResponse.getAccessToken(), is("access token"));
        assertThat(loginResponse.isLogin(), is(true));
        assertThat(loginResponse.getMessage(), is("message"));
        assertThat(loginResponse.getUserId(), is("gtchoi"));
    }

    @Test
    public void marshalListWrapper() throws JAXBException {
        // ca n you
        ProjectDTOList dtoList = new ProjectDTOList();
        ProjectDTO dto = new ProjectDTO();
        ProjectDTO dto2 = new ProjectDTO();
        dto.setProjectName("project 1");
        dto2.setProjectName("project 2");


        TodoDTO todo = new TodoDTO();
        todo.setText("todo 1");
        TodoDTO todo2 = new TodoDTO();
        todo2.setText("todo 2");
        dto.setTodos(Arrays.asList(todo, todo2));

        dtoList.setProjects(Arrays.asList(dto, dto2));

        String marshal = JaxbUtil.marshal(dtoList, ProjectDTOList.class);

        System.out.println("expected xml:");
        System.out.println(marshal);

    }

}
