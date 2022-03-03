package com.gm.wj.service;

import com.gm.wj.entity.Review;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface IReviewService {
    List<Review> list();

    Review get(int id);

    int excelToList(InputStream inputStream) throws IOException, InvalidFormatException;
}
