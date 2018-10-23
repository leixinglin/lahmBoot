package com.lhh.lahm.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lhh.lahm.controller.async.CarAsync;
import com.lhh.lahm.dao.CarMapper;
import com.lhh.lahm.dao.CarMessageLogMapper;
import com.lhh.lahm.entity.Car;
import com.lhh.lahm.rabbitmq.producer.CarSender;
import com.lhh.lahm.service.CarService;
import com.lhh.lahm.service.impl.CarServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class MainController extends BaseController{


    @Autowired
    private CarService carService;

    @Autowired
    private CarAsync carAsync;

    @Autowired
    CarMessageLogMapper carMessageLogMapper;

    @GetMapping("/index/{page}")
    public String index(@PathVariable int page,@RequestParam(required = false) Map param, ModelMap mm){
        log.info("进入主页查询");
        param.put("pageNum",page);
        if(param.get("name")==null){
            param.put("name","");
        }
        if(param.get("date")==null){
            param.put("date","");
        }
        PageInfo<Car> pageInfo=new PageInfo<>(carService.findAll(param));
        ;
        mm.put("carList",pageInfo.getList());
        mm.put("pageInfo",pageInfo);
        mm.put("param",param);
        log.info("查询完毕，结果为"+pageInfo.getList().size()+"总共页："+pageInfo.getLastPage());
        return "index";
    }

    @PostMapping("/saveCar")
    public String save(@ModelAttribute Car car){
        long curr=System.currentTimeMillis();
        carAsync.sendCar(car);
        log.info("耗时"+String.valueOf(System.currentTimeMillis()-curr));
        return "redirect:/index/1";
    }

    @GetMapping("/savePage")
    public String savePage(){
        return "save";
    }

    @GetMapping("/updatePage/{id}")
    public ModelAndView updatePage(@PathVariable int id, ModelMap modelMap){
        log.info("修改标识:"+id);
        Car car=carService.findById(id);
        System.out.println(car.getId()+car.getName());
        ModelAndView mv=new ModelAndView("update");
        mv.addObject("updateCar",car);
        return mv;
    }

    @PostMapping("/updateCar")
    public String updateCar(@ModelAttribute Car car){
        try {
            log.info("进入汽车修改操作！");
            int num=carService.updateCar(car);
            log.info("修改ok！放回："+num);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/index/1";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id){
        log.info("进入汽车删除操作！");
        int result=carService.deleteById(id);
        log.info("删除ok！ 放回："+result);
        return "redirect:/index/1";
    }

    @GetMapping("/carMessageList")
    @ResponseBody
    public Object getCarMessageLog(){
        return carMessageLogMapper.findFailMessage();
    }
}
