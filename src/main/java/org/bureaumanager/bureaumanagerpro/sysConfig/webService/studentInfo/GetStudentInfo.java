package org.bureaumanager.bureaumanagerpro.sysConfig.webService.studentInfo;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.StringUtils;
import org.bureaumanager.bureaumanagerpro.sysConfig.utils.JdbcUtil;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * @author WangJiaWei
 * @date 2019-01-25 08:59:23
 *
 */
public class GetStudentInfo {
    public static void getAllstudentinfo() throws Exception{
        System.out.println("sisoStudentInfo update start...");
        Document doc;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        // 获取调用接口后返回的流
        InputStream is = getSoapInputStream();
        doc = db.parse(is);
        NodeList nl = doc.getElementsByTagName("getStudentInfoResult");
        int len = nl.getLength();
        Connection conn = JdbcUtil.getConnection();;
        Statement stm = conn.createStatement();
        for (int i = 0; i < len; i++) {
            String id = validatenode("EMPID",i,doc);
            String GENDER = validatenode("GENDER",i,doc);
            GENDER = "'2'".equals(GENDER) ? "'f'" : "'m'";
            String COLLEGENAME = validatenode("COLLEGENAME",i,doc);
            String COLLEGECODE = validatenode("COLLEGECODE",i,doc);
            String ZHUANYENAME = validatenode("ZHUANYENAME",i,doc);
            String ZHUANYECODE = validatenode("ZHUANYECODE",i,doc);
            String NIANJI = validatenode("NIANJI",i,doc);
            String BANJINAME = validatenode("BANJINAME",i,doc);
            String BANJIDAIMA = validatenode("BANJIDAIMA",i,doc);
            String INSTRUCTORNAME = validatenode("INSTRUCTOR_NAME",i,doc);
            String INSTRUCTORCODE = validatenode("INSTRUCTOR_CODE",i,doc);
            String BANZHURENNAME = validatenode("BANZHURENNAME",i,doc);
            String BANZHURENCODE = validatenode("BANZHURENCODE",i,doc);
            String POSTCODE = validatenode("POSTCODE",i,doc);
            String IDCARD = validatenode("IDCARD",i,doc);
            String OADDRESS = validatenode("OADDRESS",i,doc);
            String DORM = validatenode("DORM",i,doc);
            String BIRTHDATE = validatenode("BIRTHDATE",i,doc);
            String LUQUPOINT = validatenode("LUQUPOINT",i,doc);
            String PHONE = validatenode("PHONE",i,doc);
            String YINHANGCARD = validatenode("YINHANGCARD",i,doc);
            String ISONSCHOOL = validatenode("ISONSCHOOL",i,doc);
            String SCHOOLROLLSTSTE = validatenode("SCHOOLROLLSTSTE",i,doc);
            String NATION = validatenode("NATION",i,doc);
            String ENTRANCE_YEAR = validatenode("ENTRANCE_YEAR",i,doc);
            String POLITICAL = validatenode("POLITICAL",i,doc);
            String EMAIL = validatenode("EMAIL",i,doc);

            int num = countByJdbc(id,stm);
            String sql = null;
            if (num > 0)
                sql = "update siso_student_info set "
                        + "class_code=" + BANJIDAIMA
                        + ",adviser_code=" + BANZHURENCODE
                        + ",college_code=" + COLLEGECODE
                        + ",drom_code=" + DORM
                        + ",entry_year=" + ENTRANCE_YEAR
                        + ",on_shool=" + ISONSCHOOL
                        + ",entry_score=" + LUQUPOINT
                        + ",grade=" + NIANJI
                        + ",shool_roll=" + SCHOOLROLLSTSTE
                        + ",instructor_code=" + INSTRUCTORCODE
                        + ",bank_card=" + YINHANGCARD
                        + ",specialty_code=" + ZHUANYECODE
                        +" where id="+id;
            else
                sql = "insert into siso_student_info (id, college_code, specialty_code, " +
                        "grade, class_code, instructor_code, adviser_code, " +
                        "drom_code, entry_score, bank_card, " +
                        "on_shool, shool_roll, entry_year) values("
                        + id + ","
                        + COLLEGECODE + ","
                        + ZHUANYECODE + ","
                        + NIANJI + ","
                        + BANJIDAIMA + ","
                        + INSTRUCTORCODE + ","
                        + BANZHURENCODE + ","
                        + DORM + ","
                        + LUQUPOINT + ","
                        + YINHANGCARD + ","
                        + ISONSCHOOL + ","
                        + SCHOOLROLLSTSTE + ","
                        + ENTRANCE_YEAR +")";
            saveByJdbc(sql,stm);
        }
        JdbcUtil.close(conn,stm);
        is.close();
        System.out.println("sisoStudentInfo update end!");
    }

    /*
     * 用户把SOAP请求发送给服务器端，并返回服务器点返回的输入流
     *
     *
     * @return 服务器端返回的输入流，供客户端读取
     *
     * @throws Exception
     *
     * @备注：有四种请求头格式1、SOAP 1.1； 2、SOAP 1.2 ； 3、HTTP GET； 4、HTTP POST
     * 参考---》http://
     *
     */
    private static InputStream getSoapInputStream() throws Exception {
        try {
            // 获取请求规范
            //根据ID获取当前未还图书记录
            String soap = getSoapRequestString();
            if (soap == null) {
                return null;
            }
            // 调用的webserviceURL
            URL url = new URL("http://192.168.200.89:5888/services/ws_xgxt_student");
            URLConnection conn1 = url.openConnection();
            HttpURLConnection conn = (HttpURLConnection) conn1;
            conn.setRequestMethod("GET");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Length", Integer.toString(soap.length()));
            conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");

            conn.setRequestProperty("SOAPAction", "http://www.apusic.com/esb/ws_xgxt_student");

            conn.setRequestProperty("WSS-Password Type", "PasswordText");

            OutputStream os = conn.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "utf-8");
            osw.write(soap);
            osw.flush();
            osw.close();
            InputStream is = null;
            int code = conn.getResponseCode();
            if (code == 200) {
                is = conn.getInputStream(); // 得到网络返回的输入流
            }else
            {
                is = conn.getErrorStream(); // 得到网络返回的输入流}
            }
            return is;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /*
     * 获取SOAP的请求头，并替换其中的标志符号为用户输入的城市
     *
     * @param city： 用户输入的城市名称
     *
     * @return 客户将要发送给服务器的SOAP请求规范
     *
     * @备注：有四种请求头格式1、SOAP 1.1； 2、SOAP 1.2 ； 3、HTTP GET； 4、HTTP POST
     * 参考---》http://
     *
     * 本文采用：SOAP 1.1格式
     */
    //根据ID获取当前未还图书记录
    private static String getSoapRequestString() {
        StringBuffer sb = new StringBuffer();
        String username = "sisoservice";
        String password = "123@abcd";
        sb.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"http://www.apusic.com/esb/ws_xgxt_student\">");
        sb.append("   <soapenv:Header> ");
        sb.append("		<wsse:Security soapenv:mustUnderstand=\"1\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"> ");
        sb.append("   		<wsse:UsernameToken wsu:Id=\"UsernameToken-1\"> ");
        sb.append("   			<wsse:Username>"+username+"</wsse:Username> ");
        sb.append("   			<wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">"+password+"</wsse:Password>  ");
        sb.append("    		</wsse:UsernameToken> ");
        sb.append("   	</wsse:Security> ");
        sb.append("   </soapenv:Header> ");
        sb.append("   <soapenv:Body>");
        sb.append("      <ws:getStudentInfo/>");
        sb.append("   </soapenv:Body>");
        sb.append("</soapenv:Envelope>");
        return sb.toString();
    }

    public static String validatenode(String Tagname,int i,Document doc){

        if(null==doc.getElementsByTagName(Tagname)||doc.getElementsByTagName(Tagname).equals("null")){
            return null;
        }
        if(null==doc.getElementsByTagName(Tagname).item(i)||"null".equals(doc.getElementsByTagName(Tagname).item(i))){
            return null;
        }
        if(null== doc.getElementsByTagName(Tagname).item(i).getFirstChild()||doc.getElementsByTagName(Tagname).item(i).getFirstChild().equals("null")){
            return null;
        }

        return !StringUtils.isNotBlank(doc.getElementsByTagName(Tagname).item(i).getFirstChild().getNodeValue())? null : "'"+doc.getElementsByTagName(Tagname).item(i).getFirstChild().getNodeValue()+"'";


    }

    public static Integer countByJdbc(String id, Statement stm) {
        Integer num = 0;
        try {
            String sql = "select count(id) num from siso_student_info where id=" + id;
            ResultSet res = stm.executeQuery(sql);
            while(res.next()){
                //提倡通过列名获取
                num = res.getInt("num");
            }
            //关闭资源
            res.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return num;
    }

    public static Integer saveByJdbc(String sql, Statement stm) {
        Integer num = 0;
        try {
            num = stm.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return num;
    }
}
