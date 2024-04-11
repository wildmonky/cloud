package org.lizhao.cloud.gateway.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Description 页面控制器 目前 webflux 不支持 forward
 *
 * @author lizhao
 * @version 0.0.1-SNAPSHOT
 * @date 2024-03-28 16:16
 * @since 0.0.1-SNAPSHOT
 */
@RequestMapping("/")
@Controller
public class PageController {

    @Operation(summary = "首页")
    @GetMapping("/")
    public String index() {
        return "/public/index";
    }

    @Operation(summary = "注册页")
    @GetMapping("/register")
    public String register() {
        return "/public/register";
    }

    @Operation(summary = "登录页")
    @GetMapping("/login")
    public String login(String status) {
        return "/public/login" + (status != null ? "?" + status : "");
    }

    @Operation(summary = "错误页")
    @GetMapping("/error")
    public String error() {
        return "/public/error";
    }

    @Operation(summary = "主页")
    @GetMapping("/root")
    public String root() {
        return "/root";
    }

    @Operation(summary = "权限")
    @GetMapping("/authority")
    public String authority() {
        return "/management/authority";
    }

    @Operation(summary = "角色")
    @GetMapping("/role")
    public String role() {
        return "/management/role";
    }

    @Operation(summary = "用户")
    @GetMapping("/user")
    public String user() {
        return "/management/user";
    }

    @Operation(summary = "用户组")
    @GetMapping("/group")
    public String group() {
        return "/management/group";
    }

}
