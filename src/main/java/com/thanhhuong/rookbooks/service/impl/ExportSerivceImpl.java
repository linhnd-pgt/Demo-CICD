package com.thanhhuong.rookbooks.service.impl;

import com.thanhhuong.rookbooks.dto.BookDto;
import com.thanhhuong.rookbooks.dto.CategoryDto;
import com.thanhhuong.rookbooks.dto.OrderDTO;
import com.thanhhuong.rookbooks.entity.User;
import com.thanhhuong.rookbooks.service.ExportService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class ExportSerivceImpl implements ExportService {

    @Override
    public String exportOrderReport(User user, List<OrderDTO> orderDTOList, String keyword) {

        String filePath = "D:\\BackUp-2\\bookshopwebsite-main\\bookshopwebsite-main\\src\\main\\resources\\orders.jrxml";
        Map<String, Object> parameters = new HashMap<>();
        JRBeanCollectionDataSource orderDataSource = new JRBeanCollectionDataSource(orderDTOList);

        parameters.put("fullName", user.getFullName());
        parameters.put("email", user.getEmail());
        parameters.put("phoneNumber", user.getPhoneNumber());
        parameters.put("orderDataSet", orderDataSource);

        try {
            JasperReport report = JasperCompileManager.compileReport(filePath);
            JasperPrint print = JasperFillManager.fillReport(report, parameters, orderDataSource);
            String desktopPath = System.getProperty("user.home") + "/Desktop";
            String outputFile = desktopPath + "/order";
            if(Objects.equals(keyword, "pdf")){
                JasperExportManager.exportReportToPdfFile(print, outputFile + ".pdf");
            }
            if(Objects.equals(keyword, "html")){
                JasperExportManager.exportReportToHtmlFile(print, outputFile + ".html");
            }
        } catch (JRException e) {
            return "Cannot find jasper report file";
        }


        return "Exported to report successfully";
    }

    @Override
    public String exportCategoryReport(User user, List<CategoryDto> categoryDTOList, String keyword) {
        String filePath = "D:\\BackUp-2\\bookshopwebsite-main\\bookshopwebsite-main\\src\\main\\resources\\category1.jrxml";
        Map<String, Object> parameters = new HashMap<>();
        JRBeanCollectionDataSource categoryDataSource = new JRBeanCollectionDataSource(categoryDTOList);
//
//        parameters.put("fullName", user.getFullName());
//        parameters.put("email", user.getEmail());
//        parameters.put("phoneNumber", user.getPhoneNumber());
        parameters.put("categoryDataSet", categoryDataSource);

        try {
            JasperReport report = JasperCompileManager.compileReport(filePath);
            JasperPrint print = JasperFillManager.fillReport(report, parameters, categoryDataSource);
            String desktopPath = System.getProperty("user.home") + "/Desktop";
            String outputFile = desktopPath + "/category";
            if(Objects.equals(keyword, "pdf")){
                JasperExportManager.exportReportToPdfFile(print, outputFile + ".pdf");
            }
            if(Objects.equals(keyword, "html")){
                JasperExportManager.exportReportToHtmlFile(print, outputFile + ".html");
            }
        } catch (JRException e) {
            return "Cannot find jasper report file";
        }


        return "Exported to report successfully";
    }

    @Override
    public String exportBookReport(User user, List<BookDto> bookDtoList, String keyword) {
        String filePath = "D:\\BackUp-2\\bookshopwebsite-main\\bookshopwebsite-main\\src\\main\\resources\\book1.jrxml";
        Map<String, Object> parameters = new HashMap<>();
        JRBeanCollectionDataSource bookDataSource = new JRBeanCollectionDataSource(bookDtoList);

//        parameters.put("fullName", user.getFullName());
//        parameters.put("email", user.getEmail());
//        parameters.put("phoneNumber", user.getPhoneNumber());
        parameters.put("bookDataSet", bookDataSource);

        try {
            JasperReport report = JasperCompileManager.compileReport(filePath);
            JasperPrint print = JasperFillManager.fillReport(report, parameters, bookDataSource);
            String desktopPath = System.getProperty("user.home") + "/Desktop";
            String outputFile = desktopPath + "/book";
            if(Objects.equals(keyword, "pdf")){
                JasperExportManager.exportReportToPdfFile(print, outputFile + ".pdf");
            }
            if(Objects.equals(keyword, "html")){
                JasperExportManager.exportReportToHtmlFile(print, outputFile + ".html");
            }
        } catch (JRException e) {
            return "Cannot find jasper report file";
        }


        return "Exported to report successfully";
    }

}
