package com.thanhhuong.rookbooks.controller.admin;

import com.thanhhuong.rookbooks.controller.common.BaseController;
import com.thanhhuong.rookbooks.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@Controller
@RequestMapping("/admin/roles_management")
public class AdminRoleController extends BaseController {
    private final RoleService roleService;
}

