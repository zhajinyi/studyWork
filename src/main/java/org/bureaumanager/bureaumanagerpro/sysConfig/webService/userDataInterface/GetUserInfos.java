package org.bureaumanager.bureaumanagerpro.sysConfig.webService.userDataInterface;

import org.apache.commons.lang3.StringUtils;
import org.bureaumanager.bureaumanagerpro.sysConfig.utils.JdbcUtil;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class GetUserInfos {

    public static void getAllUserInfo() throws Exception{
        System.out.println("sysUser update start...");
        Document doc;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        // 获取调用接口后返回的流
        InputStream is = getSoapInputStream();
        doc = db.parse(is);
        NodeList nl = doc.getElementsByTagName("getUserResult");
        int len = nl.getLength();
        Connection conn = JdbcUtil.getConnection();;
        Statement stm = conn.createStatement();
        for (int i = 0; i < len; i++) {
            String id = validatenode("OPERATOR_ID",i,doc);
            String login_name = validatenode("USER_NAME",i,doc);
            String emp_id = validatenode("USER_ID",i,doc);
            String user_type = validatenode("USER_TYPE",i,doc);
            String photo = validatenode("PHOTO",i,doc);
            String login_ip = validatenode("IPADDRESS",i,doc);
            String login_date = validatenode("LASTLOGIN",i,doc);
            login_date = login_date == null ? null : utcToString(login_date.substring(0, 20) + "'");
            String create_by = validatenode("CREATEUSER",i,doc);
            String create_date = validatenode("CREATETIME",i,doc);
            create_date = create_date == null ? null : utcToString(create_date.substring(0, 20) + "'");
            String update_by = validatenode("UPDATE_BY",i,doc);
            String update_date = validatenode("UPDATE_DATE",i,doc);
            update_date = update_date == null ? null : utcToString(update_date.substring(0, 20) + "'");
            String remarks = validatenode("REMARKS",i,doc);
            String qrcode = validatenode("QRCODE",i,doc);

            int num = countByJdbc(id,stm);
            String password="'"+passwordByJdbc(id,stm)+"'";
            String sql = null;
            if (num > 0)
                sql = "update sys_user set "
                        //+ ",`password`=" + password.toString().trim()
                        + "user_type=" + user_type
                        + ",photo=" + photo
                        + ",login_ip=" + login_ip
                        + (login_date == null ? ",login_date=" + login_date : ",login_date=DATE_FORMAT(" + login_date + ",'%Y-%m-%d')")
                        + ",create_by=" + create_by
                        + (create_date == null ? ",create_date=" + create_date : ",create_date=DATE_FORMAT(" + create_date + ",'%Y-%m-%d')")
                        + ",update_by=" + update_by
                        + (update_date == null ? ",update_date=" + update_date : ",update_date=DATE_FORMAT(" + update_date + ",'%Y-%m-%d')")
                        + ",remark=" + remarks
                        + ",qrcode=" + qrcode
                        +" where id="+id;
            else
                sql = "insert into sys_user (id,`password`,user_type,photo,login_ip,login_date,create_by,create_date,update_by,update_date,remark,qrcode) values ("
                        + id + ","
                        + password + ","
                        + user_type + ","
                        + photo + ","
                        + login_ip + ","
                        + (login_date == null ? login_date+"," : "DATE_FORMAT(" + login_date + ",'%Y-%m-%d'),")
                        + create_by + ","
                        + (create_date == null ? create_date+"," : "DATE_FORMAT(" + create_date + ",'%Y-%m-%d'),")
                        + update_by + ","
                        + (update_date == null ? update_date+"," : "DATE_FORMAT(" + update_date + ",'%Y-%m-%d'),")
                        + remarks + ","
                        + qrcode
                        + ")";
            saveByJdbc(sql,stm);
        }
        JdbcUtil.close(conn,stm);
        is.close();
        System.out.println("sysUser update end!");
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
            String soap = getSoapRequestString();
            if (soap == null) {
                return null;
            }
            //调用的webserviceURL
            URL url = new URL("http://192.168.200.89:5888/services/ws_xgxt_user");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Length", Integer.toString(soap.length()));
            conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            conn.setRequestProperty("SOAPAction", "http://www.apusic.com/esb/ws_xgxt_user/getUser");
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
            } else {
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
    private static String getSoapRequestString() {
        StringBuffer sb = new StringBuffer();
        String username = "sisoservice";
        String password = "123@abcd";
        sb.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"http://www.apusic.com/esb/ws_xgxt_user\">");
        sb.append("   <soapenv:Header> ");
        sb.append("		<wsse:Security soapenv:mustUnderstand=\"1\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"> ");
        sb.append("   		<wsse:UsernameToken wsu:Id=\"UsernameToken-1\"> ");
        sb.append("   			<wsse:Username>"+username+"</wsse:Username> ");
        sb.append("   			<wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">"+password+"</wsse:Password>  ");
        sb.append("    		</wsse:UsernameToken> ");
        sb.append("   	</wsse:Security> ");
        sb.append("   </soapenv:Header> ");
        sb.append("   <soapenv:Body>");
        sb.append("      <ws:getUser/>");
        sb.append("   </soapenv:Body>");
        sb.append("</soapenv:Envelope>");
        return sb.toString();
    }

    private static String validatenode(String Tagname, int i, Document doc) {
        if (null == doc.getElementsByTagName(Tagname) || "null".equals(doc.getElementsByTagName(Tagname))) {
            return null;
        }
        if (null == doc.getElementsByTagName(Tagname).item(i) || "null".equals(doc.getElementsByTagName(Tagname).item(i))) {
            return null;
        }
        if (null == doc.getElementsByTagName(Tagname).item(i).getFirstChild() || "null".equals(doc.getElementsByTagName(Tagname).item(i).getFirstChild())) {
            return null;
        }
        return !StringUtils.isNotBlank(doc.getElementsByTagName(Tagname).item(i).getFirstChild().getNodeValue())? null : "'"+doc.getElementsByTagName(Tagname).item(i).getFirstChild().getNodeValue()+"'";
    }

    private static String utcToString(String utc) {
        if (utc == null)
            return null;
        String[] str = utc.split("T");
        return str[0] + " " + str[1];
    }

    //用JDBC查询
    private static Integer countByJdbc(String userid, Statement stm) {
        Integer num = 0;
        try {
            String sql = "select count(id) num from sys_user where id=" + userid;
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
    //用JDBC查询
    private static String passwordByJdbc(String userid, Statement stm) {
        Integer num = 0;
        String cardno=null;
        try {
            String sql = "select cardno FROM sys_emp_info WHERE emp_id = " + userid;
            ResultSet res = stm.executeQuery(sql);
            while(res.next()){
                //提倡通过列名获取
                cardno=res.getString("cardno");
            }
            //关闭资源
            res.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(cardno==null||cardno.equals("null")){
            return "000000";
        }else {
            return cardno.substring(cardno.length()-6);
        }
    }

    //用JDBC执行SQL语句的增删改
    private static Integer saveByJdbc(String sql, Statement stm) {
        Integer num = 0;
        try {
            num = stm.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return num;
    }
}
