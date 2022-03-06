package com.gm.wj.controller;

import com.gm.wj.entity.Review;
import com.gm.wj.result.Result;
import com.gm.wj.result.ResultFactory;
import com.gm.wj.service.IReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Slf4j
@RestController
public class ReviewController {
    @Autowired
    private IReviewService reviewService;

    @GetMapping("/api/review/{type}/ooo")
    public Result ReviewList(@PathVariable("type") String type){
        if(!"0".equals(type)){
          return ResultFactory.buildSuccessResult(reviewService.getReviewByType(type));
        }else{
            return ResultFactory.buildSuccessResult(reviewService.list());
        }
    }

    @GetMapping("/api/reviewType")
    public Result ReviewTypeList(){
        return ResultFactory.buildSuccessResult(reviewService.getAllType());
    }

    @RequestMapping(value = "/api/ExcelInsertGoodinStore", method = RequestMethod.POST,produces="application/json")
    @ResponseBody
    public Result ExcelInsertGoodinStore(@RequestParam("diaFile") MultipartFile excelFile) {
        log.info("========进入导入excel方法1========");
        String name = excelFile.getOriginalFilename();
        try {
            if(reviewService.excelToList(excelFile.getInputStream())>0) {
                return  ResultFactory.buildSuccessResult(null);
            }else return ResultFactory.buildFailResult("导入失败");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultFactory.buildFailResult("文件内容出现错误，导致无法正确导入");
        }
    }

}
