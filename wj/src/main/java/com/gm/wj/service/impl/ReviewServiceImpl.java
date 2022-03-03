package com.gm.wj.service.impl;


import com.gm.wj.dao.ReviewDAO;
import com.gm.wj.entity.Category;
import com.gm.wj.entity.Review;
import com.gm.wj.redis.RedisService;
import com.gm.wj.service.IReviewService;
import com.gm.wj.util.CastUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReviewServiceImpl implements IReviewService {
    @Autowired
    private ReviewDAO reviewDAO;
    @Autowired
    private RedisService redisService;

    @Override
    public List<Review> list() {
        //首先判断redis内是否有缓存
        String key = "reviewList";
        Object reviewCache = redisService.get(key);
        if(reviewCache == null){
            Sort sort = new Sort(Sort.Direction.DESC, "id");
            redisService.set(key,reviewDAO.findAll(sort).stream().peek(x -> x.setIsOK(true)).collect(Collectors.toList()));
            return reviewDAO.findAll(sort).stream().peek(x -> x.setIsOK(true)).collect(Collectors.toList());
        }else{
            return CastUtils.objectConvertToList(reviewCache,Review.class);
        }

    }

    @Override
    public Review get(int id) {
        Review review = reviewDAO.findById(id).orElse(null);
        return review;
    }

    @Override
    public int excelToList(InputStream inputStream) throws IOException, InvalidFormatException {
        List<Review> reviewsList = new ArrayList<Review>();
        Workbook workbook = null;
        //workbook =  new HSSFWorkbook(inputStream);
        workbook = WorkbookFactory.create(inputStream);
        inputStream.close();
        //工作表对象
        Sheet sheet = workbook.getSheetAt(0);
        //总行数
        int rowLength = sheet.getLastRowNum() + 1;
        //工作表的列
        Row row = sheet.getRow(0);
        //总列数
        int colLength = row.getLastCellNum();
        for (int i = 1; i < rowLength; i++) {
            Review review = new Review();
            row = sheet.getRow(i);
            row.getCell(0).setCellType(CellType.STRING);
            String cell0 = row.getCell(0).getStringCellValue().trim();
            log.info("======"+cell0);
            review.setQuestion(cell0);
            row.getCell(1).setCellType(CellType.STRING);
            String cell1 = row.getCell(1).getStringCellValue().trim();
            review.setAnswer(cell1);
            reviewsList.add(review);
        }
//        reviewDAO.save(review);
        reviewDAO.saveAll(reviewsList);
        return 1;
    }

}
