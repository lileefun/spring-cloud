package com.libin.test.api.entity;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by libin on 2016/10/25.
 */
public class AcpResponse {

    private static final Log logger = LogFactory.getLog(AcpResponse.class);

    private HttpServletResponse response;
    private HttpServletRequest request;
    private Boolean requireCompression = false;
    //public static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat("yyyyMMdd HH:mm:ss").create();
    //private static SerializerFeature[] features = {SerializerFeature.PrettyFormat,SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.DisableCircularReferenceDetect};


    public AcpResponse(HttpServletRequest request, HttpServletResponse response, boolean requireCompression) {
        super();
        this.response = response;
        this.request = request;
        this.requireCompression = requireCompression;
    }

    public AcpResponse(HttpServletRequest request, HttpServletResponse response) {
        this(request, response, false);
    }

    public void writeByte(String fileName, byte[] b) {
        OutputStream os = null;
        try {
            if (StringUtils.isNotBlank(fileName)) {
                response.setContentType("application/octet-stream");
                String userAgent = request.getHeader("User-Agent");
                if (userAgent.contains("MSIE")) {
                    fileName = java.net.URLEncoder.encode(fileName, "UTF8");
                } else {
                    fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
                }
                response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            }
            os = response.getOutputStream();
            os.write(b);
            os.flush();
        } catch (IOException e) {
            logger.warn("Response.write", e);
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                logger.error("Response.write", e);
            }
        }
    }

    public void write(ServiceResult serviceResult) {
        if (serviceResult == null) {
            return;
        }

        if (serviceResult.getB() != null) {
            writeByte(serviceResult.getFileName(), serviceResult.getB());
            return;
        }
        OutputStream os = null;
        try {
            writeHttpHeader();
            os = response.getOutputStream();
            if (requireCompression) {
                os = new GZIPOutputStream(os);
            }
            writeJSON(serviceResult, os);
            os.flush();
        } catch (IOException e) {
            logger.warn("Response.write", e);
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                logger.error("Response.write", e);
            }
        }
    }

    private void writeJSON(ServiceResult serviceResult, OutputStream os) throws IOException {
        String rt = JSON.toJSONString(serviceResult);
        byte[] ob = rt == null ? null : rt.getBytes(CharEncoding.UTF_8);
        os.write(ob);
    }

    private void writeHttpHeader() {
        try {
            response.setStatus(HttpServletResponse.SC_OK);
            if (requireCompression) {
                response.setContentType("application/json-gz");
                response.setHeader("content-encoding", "gzip");
            } else {
                response.setContentType("application/json");
            }
        } catch (Exception e) {
            logger.error("writeHttpHeader", e);
        }
    }


}
