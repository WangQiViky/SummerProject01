package com.example.cinema.controller.router;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author deng
 * @date 2019/03/11
 */
@Controller
public class ViewController {
    @RequestMapping(value = "/index")
    public String getIndex() {
        return "index";
    }

    @RequestMapping(value = "/finish")
    public String getFinish() {
        return "finish";
    }

    @RequestMapping(value = "/bofang")
    public String getBofang() {
        return "bofang";
    }

    @RequestMapping(value = "/false")
    public String getFalse() {
        return "false";
    }

    @RequestMapping(value = "/abandon")
    public String getAbandon(){ return "abandonAdjust";}

    @RequestMapping(value = "/user/boy")
    public String getBoy() { return "twoChoices"; }

    @RequestMapping(value = "/user/girl")
    public String getGirl() { return "twoChoices"; }

    @RequestMapping(value = "/user/selectPs")
    public String getSelectFace() {
        return "selectPs";
    }

    @RequestMapping(value = "/user/startTiaoZheng")
    public String getStartTiaoZheng() {
        return "startTiaoZheng";
    }

    @RequestMapping(value = "/user/selectWhichPs")
    public String getSelectPs() {
        return "selectWhichPs";
    }

    @RequestMapping(value = "/user/selectFix")
    public String getSelectFix() {
        return "selectFix";
    }

    @RequestMapping(value = "/user/selectWhichFix")
    public String getSelectWhichFix() {
        return "selectWhichFix";
    }

    @RequestMapping(value = "/user/selectOcean")
    public String getSelectOcean() {
        return "selectOcean";
    }

    @RequestMapping(value = "/user/selectHouse")
    public String getSelectHouse() {
        return "selectHouse";
    }

    @RequestMapping(value = "/user/selectTree")
    public String getSelectTree() {
        return "selectTree";
    }

    @RequestMapping(value = "/user/start")
    public String getSrart() {
        return "start";
    }

    @RequestMapping(value = "/user/startSelect")
    public String getSelect() {
        return "startSelect";
    }
    @RequestMapping(value = "/user/startTalking")
    public String getTalking() {
        return "startTalking";
    }

    @RequestMapping(value = "/signUp")
    public String getSignUp() {
        return "signUp";
    }


    @RequestMapping(value = "/user/selectWhichCartoon")
    public String getWhichCartoon(){return "selectWhichCartoon";}


    @RequestMapping(value = "/user/showPhoto")
    public String showPhoto(){return "showPhoto";}

    @RequestMapping(value = "/user/choosedEye")
    public String choosedEye(){return "choosedEye";}

    @RequestMapping(value = "/user/choosedMouth")
    public String choosedMouth(){return "choosedMouth";}

    @RequestMapping(value = "/user/eyeBeforeMouth")
    public String eyeBeforeMouth(){return "eyeBeforeMouth";};

    @RequestMapping(value = "/user/mouthBeforeEye")
    public String mouthBeforeEye(){return "mouthBeforeEye";};


    @RequestMapping(value = "/user/resultCartoon")
    public String resultCartoon(){return "resultCartoon";}


    @RequestMapping(value = "/user/resultCartoonAdjust")
    public String resultCartoonAdjust(){return "resultCartoonAdjust";}
}
