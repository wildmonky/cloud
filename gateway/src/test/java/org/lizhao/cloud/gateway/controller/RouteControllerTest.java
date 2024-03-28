package org.lizhao.cloud.gateway.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lizhao.cloud.gateway.repository.UserRepository;
import org.lizhao.cloud.gateway.serviceImpl.RouteServiceImpl;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = org.lizhao.cloud.gateway.controller.RouteController.class)
@Import(UserRepository.class)
class RouteControllerTest {

    @MockBean
    private RouteServiceImpl routeServiceImpl;

    @Test
    void routeList() {
    }

    @Test
    void saveRouteList() {
    }

    @Test
    void removeRouteList() {
    }

    @Test
    void listTest() {
    }
}