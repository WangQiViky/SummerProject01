package com.example.cinema.controller.picture;


import com.example.cinema.bl.picture.CartoonService;
import com.example.cinema.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class CartoonController {

    @Autowired
    CartoonService cartoonService;

    @RequestMapping(value = "/cartoon/ps/{id}",method= RequestMethod.GET)
    public ResponseVO ps(@PathVariable int id){
        //String id=request.getParameter("id");
        System.out.println("前端传过来的id："+id);
        String property_1 = System.getProperty("user.dir");
        String path_real = property_1 + "\\src\\main\\resources\\static\\photos\\photo.png";
        String path_cartoon=property_1 + "\\src\\main\\resources\\static\\photos\\gongzhu_"+id+".png";
        return cartoonService.ps(path_real,path_cartoon);
    }


    @RequestMapping(value = "/cartoon/fix/{id}",method = RequestMethod.GET)
    public ResponseVO fix(@PathVariable int id){

        //String id=request.getParameter("id");
        String property_1 = System.getProperty("user.dir");
        String path_real = property_1 + "\\src\\main\\resources\\static\\photos\\photo.png";
        String path_cartoon=property_1 + "\\src\\main\\resources\\static\\photos\\gongzhu_"+id+".png";
        System.out.println(path_cartoon);
        return cartoonService.fix(path_real,path_cartoon);
    }


    @RequestMapping(value="/cartoon/get/{id}",method = RequestMethod.GET)
    public ResponseVO getCartoon(@PathVariable int id) throws IOException {
        String property_1 = System.getProperty("user.dir");
        String path_cartoon=property_1 + "\\src\\main\\resources\\static\\photos\\gongzhu_"+id+".png";
        String save_path=property_1+"\\src\\main\\resources\\static\\photos\\result.png";
        return cartoonService.getCartoon(path_cartoon,save_path);

    }

}
