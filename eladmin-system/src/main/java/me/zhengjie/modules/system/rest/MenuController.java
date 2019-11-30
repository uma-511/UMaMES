package me.zhengjie.modules.system.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.aop.log.Log;
import me.zhengjie.modules.system.domain.Menu;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.system.domain.Role;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.repository.RoleRepository;
import me.zhengjie.modules.system.service.MenuService;
import me.zhengjie.modules.system.service.RoleService;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.modules.system.service.dto.MenuDTO;
import me.zhengjie.modules.system.service.dto.MenuQueryCriteria;
import me.zhengjie.modules.system.service.dto.RoleQueryCriteria;
import me.zhengjie.modules.system.service.dto.UserDTO;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author Zheng Jie
 * @date 2018-12-03
 */
@Api(tags = "系统：菜单管理")
@RestController
@RequestMapping("/api/menus")
@SuppressWarnings("unchecked")
public class MenuController {

    private final MenuService menuService;

    private final UserService userService;

    private final RoleService roleService;

    private final RoleRepository roleRepository;

    private static final String ENTITY_NAME = "menu";

    public MenuController(MenuService menuService, UserService userService, RoleService roleService, RoleRepository roleRepository) {
        this.menuService = menuService;
        this.userService = userService;
        this.roleService = roleService;
        this.roleRepository = roleRepository;
    }

    @Log("导出菜单数据")
    @ApiOperation("导出菜单数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('menu:list')")
    public void download(HttpServletResponse response, MenuQueryCriteria criteria) throws IOException {
        menuService.download(menuService.queryAll(criteria), response);
    }

    @ApiOperation("获取前端所需菜单")
    @GetMapping(value = "/build")
    public ResponseEntity buildMenus(){
        UserDTO user = userService.findByName(SecurityUtils.getUsername());
        List<MenuDTO> menuDTOList = menuService.findByRoles(roleService.findByUsers_Id(user.getId()));
        List<MenuDTO> menuDTOS = (List<MenuDTO>) menuService.buildTree(menuDTOList).get("content");
        return new ResponseEntity<>(menuService.buildMenus(menuDTOS),HttpStatus.OK);
    }

    @ApiOperation("返回全部的菜单")
    @GetMapping(value = "/tree")
    @PreAuthorize("@el.check('menu:list','roles:list')")
    public ResponseEntity getMenuTree(){
        if (SecurityUtils.getUsername().equals("um_admin")) {
            return new ResponseEntity<>(menuService.getMenuTree(menuService.findByPid(0L)),HttpStatus.OK);
        }

        UserDTO userDTO = userService.findByName(SecurityUtils.getUsername());
        RoleQueryCriteria criteria = new RoleQueryCriteria();
        List<Role> roles = roleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder));
        List<Menu> menusList = null;
        for (Role role : roles) {
            Map<String, String> map = setToMap(role.getUsers());

            if (map.containsKey(userDTO.getId().toString())) {
                menusList = new ArrayList<>(role.getMenus());
            }
        }
        Map<Long, Long> tempMap = new HashMap<>();
        List<Menu> menusTemp = new ArrayList<>();
        if (menusList != null) {
            for (Menu menu : menusList) {
                tempMap.put(menu.getId(), menu.getId());
                if (menu.getPid() == 0) {
                    menusTemp.add(menu);
                }
            }
        }
        Object menuTree = menuService.getMenuTree(menusTemp, tempMap);
        return new ResponseEntity<>(menuTree,HttpStatus.OK);
    }

    @Log("查询菜单")
    @ApiOperation("查询菜单")
    @GetMapping
    @PreAuthorize("@el.check('menu:list')")
    public ResponseEntity getMenus(MenuQueryCriteria criteria){
        List<MenuDTO> menuDTOList = menuService.queryAll(criteria);
        return new ResponseEntity<>(menuService.buildTree(menuDTOList),HttpStatus.OK);
    }

    @Log("新增菜单")
    @ApiOperation("新增菜单")
    @PostMapping
    @PreAuthorize("@el.check('menu:add')")
    public ResponseEntity create(@Validated @RequestBody Menu resources){
        if (resources.getId() != null) {
            throw new BadRequestException("A new "+ ENTITY_NAME +" cannot already have an ID");
        }
        return new ResponseEntity<>(menuService.create(resources),HttpStatus.CREATED);
    }

    @Log("修改菜单")
    @ApiOperation("修改菜单")
    @PutMapping
    @PreAuthorize("@el.check('menu:edit')")
    public ResponseEntity update(@Validated(Menu.Update.class) @RequestBody Menu resources){
        menuService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除菜单")
    @ApiOperation("删除菜单")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('menu:del')")
    public ResponseEntity delete(@PathVariable Long id){
        List<Menu> menuList = menuService.findByPid(id);
        Set<Menu> menuSet = new HashSet<>();
        menuSet.add(menuService.findOne(id));
        menuSet = menuService.getDeleteMenus(menuList, menuSet);
        menuService.delete(menuSet);
        return new ResponseEntity(HttpStatus.OK);
    }

    private Map<String,String> setToMap (Set<User> set) {
        Map<String,String> map = new HashMap<String,String>();
        if (set == null) {
            return null;
        }
        Iterator<User> iterator = set.iterator();
        while (iterator.hasNext()) {
            User item = iterator.next();
            map.put(item.getId().toString(), item.getId().toString());
        }
        return map;
    }
}
