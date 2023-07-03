package com.auto.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.auto.entity.HouseUser;
import com.auto.service.HouseUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 房东模块
 */
@Controller
@RequestMapping("/houseUser")
public class HouseUserController {

    private static final String PAGE_CREATE = "houseUser/create";
    public static final String PAGE_SUCCESS = "common/successPage";
    private static final String PAGE_EDIT = "houseUser/edit";
    private static final String SHOW_ACTION = "redirect:/house/";
    @Reference
    private HouseUserService houseUserService;

    /**
     * 到新新增房东页面
     */
    @GetMapping("/create")
    public String create(HouseUser houseUser, Model model){
        model.addAttribute("houseUser",houseUser);
        return PAGE_CREATE;
    }

    /**
     * 保存新增房东
     * @param houseUser
     * @param model
     * @return
     */
    @PostMapping("/save")
    public String save(HouseUser houseUser, Model model){
        houseUserService.insert(houseUser);
        model.addAttribute("messagePage", "新增房东信息成功");
        return PAGE_SUCCESS;
    }

    /**
     * 修改房东回显页面
     */

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model){
        HouseUser houseUser = houseUserService.getById(id);
        model.addAttribute("houseUser",houseUser);
        return PAGE_EDIT;
    }

    /**
     * 保存修改
     */
    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, HouseUser houseUser,Model model){
        houseUser.setId(id);
        houseUserService.update(houseUser);
        model.addAttribute("messagePage", "修改房东信息成功");
        return PAGE_SUCCESS;
    }

    /**
     * 删除房东信息
     */
    @GetMapping("/delete/{houseId}/{id}")
    public String delete(@PathVariable("houseId") Long houseId,@PathVariable("id") Long id){
        houseUserService.delete(id);
        return SHOW_ACTION + houseId;
    }
}
