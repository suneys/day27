package upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import entities.*;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.CellFormat;
import jxl.write.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import utils.FileUtil;
import utils.ResponseUtil;

public class FileServlet extends HttpServlet {

    private HashMap<String, LineEntity> lineEntities = new HashMap<String, LineEntity>();
    private List<ImageEntity> imagePath = new ArrayList<>();
    private Map<String, ImageEntity> imageMap = new HashMap<>();
    private TreeMap<String, SmtpmsEntity> smtpmsEntities = new TreeMap<String, SmtpmsEntity>();

    private long excelUpdateTime = System.currentTimeMillis();
    private HashMap<String, Long> updataClient = new HashMap<>();


    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String method = request.getParameter("method");
        if ("upload".equals(method)) {
            upload(request, response);
        } else if ("downList".equals(method)) {
            downList(request, response);
        } else if ("down".equals(method)) {
            down(request, response);
        } else if ("incTotalCount".equals(method)) {
            incTotalCount(request, response);
        } else if ("checkUpdate".equals(method)) {
            checkUpdate(request, response);
        } else if ("correct".equals(method)) {  //巡检员确定正确
            correct(request, response);
        } else if ("error".equals(method)) {     //巡检员确定错误
            error(request, response);
        } else if ("uploadExcel".equals(method)) {
            synchronized (FileServlet.this) {
                uploadExcel(request, response);
            }
        } else if ("checkExcelUpdate".equals(method)) {
            checkExcelUpdate(request, response);
        } else if ("updateSmtpmsEntities".equals(method)) {
            updateSmtpmsEntities(request, response);
        } else if ("updateRecord".equals(method)) {
            synchronized (FileServlet.this) {
                updateRecord(request, response);
            }
        } else if("updateMainExcel".equals(method)){
            updateMainExcel(request,response);
        }
    }

    private void updateRecord(HttpServletRequest request, HttpServletResponse response) {
        try {
            String strJson = request.getParameter("record");
            RecordEntity recordEntity = JSON.parseObject(strJson, RecordEntity.class);
            String path = request.getRealPath("/产量记录");
            File pathFile = new File(path);
            if (!pathFile.exists()) {
                pathFile.mkdir();
            }
            File recordFile = new File(path, "产量记录.xls");
            if (!recordFile.exists()) {
                recordFile.createNewFile();
            }
            Workbook rwb = Workbook.getWorkbook(recordFile);
            WritableWorkbook wwb = Workbook.createWorkbook(recordFile, rwb);
            Sheet sheet = rwb.getSheet(0);
            int rows = sheet.getRows();
            WritableSheet ws = wwb.getSheet(0);
            Label label = new Label(0, rows, recordEntity.getUserName());
            ws.addCell(label);
            label = new Label(1, rows, recordEntity.getLineNumber());
            ws.addCell(label);
            label = new Label(2, rows, recordEntity.getBatchNumber());
            ws.addCell(label);
            label = new Label(3, rows, recordEntity.getProgramName());
            ws.addCell(label);
            label = new Label(4, rows, recordEntity.getOnDayProduction());
            ws.addCell(label);
            label = new Label(5, rows, recordEntity.getRecordTime());
            ws.addCell(label);
            wwb.write();
            wwb.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateCell(String filePath, int sheet,int r, int l,String content)  {
        InputStream is = null;
        WritableWorkbook wbe = null;
        try {
            is = new FileInputStream(filePath);
            Workbook rwb = Workbook.getWorkbook(is);
            wbe = Workbook.createWorkbook(new File(filePath),rwb);
            WritableSheet rst = wbe.getSheet(sheet);
            WritableCell cell = rst.getWritableCell(l, r);
            CellFormat cf = cell.getCellFormat();
            Label label = new Label(l, r, content);
            if(cf != null) {
                label.setCellFormat(cf);
            }
            rst.addCell(label);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(wbe != null) {
                    wbe.write();
                    wbe.close();
                }
                if(is != null) {
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }

    public static void updateMainExcel(HttpServletRequest request, HttpServletResponse response){
        String filePath = request.getRealPath("/精密线体计划排产") + "/精密线体计划排产表.xls";
        int sheet = Integer.valueOf(request.getParameter("sheet"));
        int row = Integer.valueOf(request.getParameter("row"));
        String statusA = request.getParameter("statusA");
        String cumulativeProduction = request.getParameter("cumulativeProduction");
        try {
            updateCell(filePath,sheet,row,11,statusA);
            updateCell(filePath,sheet,row,13,cumulativeProduction);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void updateSmtpmsEntities(HttpServletRequest request, HttpServletResponse response) {
        String strLineNumber = request.getParameter("lineNumber");
        int lineNumber = Integer.parseInt(strLineNumber);
        String strJson = request.getParameter("smtpmsEntity");
        SmtpmsEntity smtpmsEntity = JSON.parseObject(strJson, SmtpmsEntity.class);
        smtpmsEntities.put("T"+(lineNumber + 1), smtpmsEntity);
        getServletContext().setAttribute("smtpmsEntities", smtpmsEntities);

    }

    private void checkExcelUpdate(HttpServletRequest request, HttpServletResponse response) {
        String clientName = request.getParameter("client");
        Long clientUpdateTime = updataClient.get(clientName);
        if (clientUpdateTime == null) {
            updataClient.put(clientName, excelUpdateTime);
        } else {
            Map jsonMap = new HashMap();
            if (clientUpdateTime < excelUpdateTime) {
                jsonMap.put("excelUpdate", "Ture");
                updataClient.put(clientName, System.currentTimeMillis());
            } else {
                jsonMap.put("excelUpdate", "Flase");
            }
            ResponseUtil.response(response, jsonMap);
        }
    }

    private void uploadExcel(HttpServletRequest request, HttpServletResponse response) {
        String type = request.getParameter("type");
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } // 设置编码
        // 获得磁盘文件条目工厂
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 获取文件需要上传到的路径
        String path = "";
        if ("main".equals(type)) {
            path = request.getRealPath("/精密线体计划排产");
        } else if ("appUpdateMain".equals(type)) {
            path = request.getRealPath("/精密线体计划排产");
        } else if ("firstCheck".equals(type)) {
            path = request.getRealPath("/首检");
        } else {
            path = request.getRealPath("/批次清单");
        }

        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        factory.setRepository(file);
        // 设置 缓存的大小，当上传文件的容量超过该缓存时，直接放到 暂时存储室
        factory.setSizeThreshold(1024 * 1024);

        // 高水平的API文件上传处理
        ServletFileUpload upload = new ServletFileUpload(factory);
        String fileNewName = "";
        try {
            // 可以上传多个文件
            List<FileItem> list = (List<FileItem>) upload.parseRequest(request);

            for (FileItem item : list) {
                // 获取表单的属性名字
                String name = item.getFieldName();

                // 如果获取的 表单信息是普通的 文本 信息
                if (item.isFormField()) {
                    // 获取用户具体输入的字符串 ，名字起得挺好，因为表单提交过来的是 字符串类型的
                    // request.setAttribute(name, value);
                }
                // 对传入的非 简单的字符串进行处理 ，比如说二进制的 图片，电影这些
                else {
                    /**
                     * 以下三步，主要获取 上传文件的名字
                     */
                    // 获取路径名
                    String value = item.getName();
                    // 索引到最后一个反斜杠
                    int start = value.lastIndexOf("\\");
                    // 截取 上传文件的 字符串名字，加1是 去掉反斜杠，
                    String filename = value.substring(start + 1);
                    if ("main".equals(type)) {
                        File tempFile = new File(path, filename);
                        if (tempFile.exists()) {
                            filename = "temp.xls";
                        }
                    }
//                    if(filename.endsWith("XLS")){
//                        filename = filename.replace(".XLS",".xls");
//                    }

                    //System.out.println("filename---->"+filename);


                    // request.setAttribute(name, filename);
                    //setLineEntities(fileNewName);

                    // fileNames.put(filename, filename);
                    // 真正写到磁盘上
                    // 它抛出的异常 用exception 捕捉

                    // item.write( new File(path,filename) );//第三方提供的

                    // 手动写的
                    OutputStream out = new FileOutputStream(new File(path,
                            filename));

                    InputStream in = item.getInputStream();

                    int length = 0;
                    byte[] buf = new byte[1024];

                    // System.out.println("获取上传文件的总共的容量：" + item.getSize());

                    // in.read(buf) 每次读到的数据存放在 buf 数组中
                    while ((length = in.read(buf)) != -1) {
                        // 在 buf 数组中 取出数据 写到 （输出流）磁盘上
                        out.write(buf, 0, length);

                    }

                    in.close();
                    out.close();
                    if ("detail".equals(type)) {
                        Map jsonMap = new HashMap();
                        jsonMap.put("code", "True");
                        ResponseUtil.response(response, jsonMap);
                    }
                }
            }
            if ("main".equals(type)) {
                mergeProductionScheduling(path + "/精密线体计划排产表.XLS", path + "/temp.xls");
                excelUpdateTime = System.currentTimeMillis();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
        }
    }

    private void mergeProductionScheduling(String target, String source) {
        WritableWorkbook wwb = null;
        File sourceFile = null;
        try {
            File targetFile = new File(target);
            sourceFile = new File(source);
            Workbook targetRwb = Workbook.getWorkbook(targetFile);
            Workbook sourceRwb = Workbook.getWorkbook(sourceFile);
            wwb = Workbook.createWorkbook(targetFile, targetRwb);
            for (int i = 0; i < 8; i++) {
                Sheet sourceSheet = sourceRwb.getSheet(i);
                int sourceRows = sourceSheet.getRows();
                if (sourceRows < 2) {
                    continue;
                }
                Sheet targetSheet = targetRwb.getSheet(i);
                int targetRows = targetSheet.getRows();
                int columns = targetSheet.getColumns();
                WritableSheet wrSheet = wwb.getSheet(i);
                for (int j = 1; j < sourceRows; j++) {
                    for (int k = 0; k < columns; k++) {
                        Cell targetCell = sourceSheet.getCell(k, j);
//                        if((k == 5 || k == 6 || k == 13) && (!targetCell.getContents().trim().equals(""))){
//                            jxl.write.NumberFormat nf = new jxl.write.NumberFormat("#");    //设置数字格式
//                            jxl.write.WritableCellFormat wcfN = new jxl.write.WritableCellFormat(nf); //设置表单格式
//                            jxl.write.Number labelNF = new jxl.write.Number(k, (j - 1) + targetRows, Integer.valueOf(targetCell.getContents().trim()), wcfN); //格式化数值
//                            wrSheet.addCell(labelNF);   //在表单中添加格式化的数字
//
//                        }
//                        else {
//                            Label label = new Label(k, (j - 1) + targetRows, targetCell.getContents());
//                            wrSheet.addCell(label);
//                        }
                        Label label = new Label(k, (j - 1) + targetRows, targetCell.getContents());
                        wrSheet.addCell(label);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (wwb != null) {
                    wwb.write();
                    wwb.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (sourceFile != null) {
                sourceFile.delete();
            }

        }
    }

    private void error(HttpServletRequest request, HttpServletResponse response) {
        try {
            String imgPath = request.getParameter("imgPath");
            String batch = request.getParameter("batch");
            String program = request.getParameter("program");
            String line = request.getParameter("line");
            String stack = request.getParameter("stack");
            String controller = request.getParameter("controller");
            String name = request.getParameter("name");
            String time = request.getParameter("time");
            String path = request.getRealPath("/resource");
            String newPath = request.getRealPath("/error");
            newPath = newPath + "/" + batch + "#" + program + "#" + line + "#" + stack + "#" + controller + "#" + name
                    + "#" + time;
            //File file = new File(path, imgPath);
            //file.delete();
            FileUtil.fileMove(path + "/" + imgPath, newPath);

            setLineEntities(batch + "#" + program + "#" + line);
            removeImageInMap(imgPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void correct(HttpServletRequest request, HttpServletResponse response) {
        String msg = "";
        try {

            String imgPath = request.getParameter("imgPath");
            String batch = request.getParameter("batch");
            String program = request.getParameter("program");
            String line = request.getParameter("line");
            String stack = request.getParameter("stack");
            String controller = request.getParameter("controller");
            String name = request.getParameter("name");
            String time = request.getParameter("time");
            String newImgPath = batch + "#" + program + "#" + line + "#" + stack + "#" + controller + "#" + name
                    + "#" + time;
            String path = request.getRealPath("/resource");
            String newPath = request.getRealPath("/correct");
//            File file = new File(path, imgPath);
//            File newFile = new File(path, newImgPath);
//            file.renameTo(newFile);
            FileUtil.fileMove(path + "/" + imgPath, newPath + "/" + newImgPath);
            setLineEntities(batch + "#" + program + "#" + line);
            msg = "success";
            removeImageInMap(imgPath);
        } catch (Exception e) {
            e.printStackTrace();
            msg = "fail";
        } finally {
            setResponse(response, msg);
        }


    }

    private void setResponse(HttpServletResponse response, String msg) {
        try {
            response.flushBuffer();
            response.getWriter().write(msg);
            response.getWriter().flush();
            response.getWriter().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkUpdate(HttpServletRequest request, HttpServletResponse response) {
        Map jsonMap = new HashMap();
        synchronized (this) {
            if (imagePath.size() > 0) {
                jsonMap.put("img", imagePath);
                ResponseUtil.response(response, jsonMap);
            } else {
                setResponse(response, "");
            }
        }


    }

    private void incTotalCount(HttpServletRequest request,
                               HttpServletResponse response) {
        String lineName = request.getParameter("lineName");
        String bathNumber = request.getParameter("bathNumber");
        String programName = request.getParameter("programName");
        System.out.println("lineName =" + lineName + ",bathNumber="
                + bathNumber + ",programName=" + programName);

        if (lineName != null) {

            setLineEntity(bathNumber, programName, lineName);
        }

    }

    private void incTotalCount(String fileName) {
        String[] strArry = fileName.split("#");
        String bathNumber = strArry[0];
        String programName = strArry[1];
        String lineName = strArry[2];
        if (lineName != null) {
            setLineEntity(bathNumber, programName, lineName);
        }
    }

    private void setLineEntity(String bathNumber, String programName, String lineName) {
        if (lineEntities.get(lineName) != null
                && lineEntities.get(lineName).getProgramName()
                .equals(programName)
                && lineEntities.get(lineName).getBathNumber()
                .equals(bathNumber)) {
            int totalCount = lineEntities.get(lineName).getTotalCount();
            lineEntities.get(lineName).setTotalCount(totalCount + 1);

        } else {
            LineEntity lineEntity = new LineEntity();
            lineEntity.setBathNumber(bathNumber);
            lineEntity.setProgramName(programName);
            lineEntity.setTotalCount(1);
            lineEntity.setOkCount(0);
            lineEntities.put(lineName, lineEntity);
        }
        getServletContext().setAttribute("lineEntities", lineEntities);
    }

    private void down(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // TODO Auto-generated method stub
        String fileName = request.getParameter("fileName");
        fileName = new String(fileName.getBytes("ISO8859-1"), "utf-8");
        String basePath = "D:/upload";// getServletContext().getRealPath("/upload");
        InputStream in = new FileInputStream(new File(basePath, fileName));
        fileName = URLEncoder.encode(fileName, "utf-8");
        response.setHeader("content-disposition", "attachment;fileName="
                + fileName);
        OutputStream out = response.getOutputStream();
        byte[] b = new byte[1024];
        int len = -1;
        while ((len = in.read(b)) != -1) {
            out.write(b, 0, len);
        }
        out.close();
        in.close();
    }

    private void downList(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        HashMap<String, String> fileNames = new HashMap<String, String>();
        String basePath = "D:/upload";// getServletContext().getRealPath("/upload");
        File file = new File(basePath);
        String[] list = file.list();
        if (list != null && list.length > 0) {
            for (int i = 0; i < list.length; i++) {
                String fileName = list[i];
                // String shortName = fileName
                // .substring(fileName.lastIndexOf("#") + 1);
                fileNames.put(fileName, fileName);
            }
        }
        request.setAttribute("fileNames", fileNames);
        request.getRequestDispatcher("/downlist.jsp")
                .forward(request, response);
    }

    private void upload(HttpServletRequest request, HttpServletResponse response) {
        String stackNum = "";
        try {
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } // 设置编码
        HashMap<String, String> fileNames = new HashMap<String, String>();
        // 获得磁盘文件条目工厂
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 获取文件需要上传到的路径
        String path = request.getRealPath("/resource");

        //String stackName = request.getParameter("date");
        //System.out.println("stackName---->"+stackName);
        // 如果没以下两行设置的话，上传大的 文件 会占用 很多内存，
        // 设置暂时存放的 存储室 , 这个存储室，可以和 最终存储文件 的目录不同
        /**
         * 原理 它是先存到 暂时存储室，然后在真正写到 对应目录的硬盘上， 按理来说 当上传一个文件时，其实是上传了两份，第一个是以 .tem
         * 格式的 然后再将其真正写到 对应目录的硬盘上
         */
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        factory.setRepository(file);
        // 设置 缓存的大小，当上传文件的容量超过该缓存时，直接放到 暂时存储室
        factory.setSizeThreshold(1024 * 1024);

        // 高水平的API文件上传处理
        ServletFileUpload upload = new ServletFileUpload(factory);
        String fileNewName = "";
        try {
            // 可以上传多个文件
            List<FileItem> list = (List<FileItem>) upload.parseRequest(request);

            for (FileItem item : list) {
                // 获取表单的属性名字
                String name = item.getFieldName();

                // 如果获取的 表单信息是普通的 文本 信息
                if (item.isFormField()) {
                    // 获取用户具体输入的字符串 ，名字起得挺好，因为表单提交过来的是 字符串类型的
                    stackNum = item.getString();
                    //System.out.println("stackNum-->"+stackNum);
                    // request.setAttribute(name, value);
                }
                // 对传入的非 简单的字符串进行处理 ，比如说二进制的 图片，电影这些
                else {
                    /**
                     * 以下三步，主要获取 上传文件的名字
                     */
                    // 获取路径名
                    String value = item.getName();
                    // 索引到最后一个反斜杠
                    int start = value.lastIndexOf("\\");
                    // 截取 上传文件的 字符串名字，加1是 去掉反斜杠，
                    String filename = value.substring(start + 1);
                    //System.out.println("filename---->"+filename);

                    String[] fileNameArry = filename.split("#");
                    if (stackNum != null && !stackNum.isEmpty()) {
                        fileNewName = fileNameArry[0] + "#" + fileNameArry[1] +
                                "#" + fileNameArry[2] + "#" + stackNum + "#" + fileNameArry[3] +
                                "#" + fileNameArry[4];
                    }
                    // request.setAttribute(name, filename);
                    //setLineEntities(fileNewName);
                    String createFileName = UUID.randomUUID().toString() + ".jpg";
                    incTotalCount(fileNewName);
                    synchronized (this) {
                        ImageEntity imageEntity = new ImageEntity();
                        imageEntity.setFileName(fileNewName);
                        imageEntity.setImgPath("/day27/resource/" + createFileName);
                        imageEntity.setLineName(fileNameArry[2]);
                        imagePath.add(imageEntity);
                        imageMap.put(createFileName, imageEntity);
                    }
                    // fileNames.put(filename, filename);
                    // 真正写到磁盘上
                    // 它抛出的异常 用exception 捕捉

                    // item.write( new File(path,filename) );//第三方提供的

                    // 手动写的
                    OutputStream out = new FileOutputStream(new File(path,
                            createFileName));

                    InputStream in = item.getInputStream();

                    int length = 0;
                    byte[] buf = new byte[1024];

                    // System.out.println("获取上传文件的总共的容量：" + item.getSize());

                    // in.read(buf) 每次读到的数据存放在 buf 数组中
                    while ((length = in.read(buf)) != -1) {
                        // 在 buf 数组中 取出数据 写到 （输出流）磁盘上
                        out.write(buf, 0, length);

                    }

                    in.close();
                    out.close();
                }
            }
            getServletContext().setAttribute("lineEntities", lineEntities);
        } catch (Exception e) {
            // TODO Auto-generated catch block

            // e.printStackTrace();
            synchronized (this) {
                imagePath.remove(fileNewName);
            }
        }

    }

    private void setLineEntities(String filename) {
        // TODO Auto-generated method stub
        String[] strArry = filename.split("#");
        if (lineEntities.get(strArry[2]) != null) {
            int okCount = lineEntities.get(strArry[2]).getOkCount();
            lineEntities.get(strArry[2]).setOkCount(okCount + 1);
            if (lineEntities.get(strArry[2]).getOkCount() > lineEntities.get(
                    strArry[2]).getTotalCount()) {
                lineEntities.get(strArry[2]).setTotalCount(
                        lineEntities.get(strArry[2]).getOkCount());
            }
        } else {
            LineEntity lineEntity = new LineEntity();
            lineEntity.setBathNumber(strArry[0]);
            lineEntity.setProgramName(strArry[1]);
            lineEntity.setTotalCount(1);
            lineEntity.setOkCount(1);
            lineEntities.put(strArry[2], lineEntity);
        }

    }

    private void removeImageInMap(String key) {
        ImageEntity imageEntity = imageMap.get(key);
        imagePath.remove(imageEntity);
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}
