package org.bureaumanager.bureaumanagerpro.sysConfig.webService.fwdtWalkSchool;

import org.bureaumanager.bureaumanagerpro.model.stuSystem.pojo.dto.SisoWalkSchoolDto;
import org.bureaumanager.bureaumanagerpro.model.stuSystem.service.impl.SisoWalkSchoolServiceImpl;
import org.bureaumanager.bureaumanagerpro.sysConfig.utils.DateUtils;
import org.bureaumanager.bureaumanagerpro.sysConfig.utils.WebServiceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * @Author:     BaoLing   
 * @Date:    2019/1/16 11:10
 * @Description:  ${NAME}
 * @Version:    1.0
 */
@Component
public class WalkClient {
  private static final Logger logger= LoggerFactory.getLogger(WalkClient.class);
  private static  WalkSchoolInterfaceService service = new WalkSchoolInterfaceServiceService().getPort(WalkSchoolInterfaceService.class);
  @Autowired
  private SisoWalkSchoolServiceImpl wImpl;
  private static WalkClient walkClient;
  @PostConstruct
  public void init() {
    walkClient = this;
    walkClient.wImpl = this.wImpl;
  }
  //获取年份，过去n年就传参数n
  //今年
  private static String currentYear= DateUtils.getYear();
  //去年
  private static String beforeYear1= DateUtils.pastYears(1);
  //前年
  private static String beforeYear2= DateUtils.pastYears(2);
  //大前年
  private static String beforeYear3= DateUtils.pastYears(3);

  //当前年份数据
  private static String str =service.queryWalkSchool(currentYear);
  private static String str1 =service.queryWalkSchool(beforeYear1);
  private static String str2 =service.queryWalkSchool(beforeYear2);
  private static String str3 =service.queryWalkSchool(beforeYear3);
  private static String K = "SisoWalkSchools";

//  public static void main(String[] args) {
//    doRun();
//  }
  /**
   *运行方法，在WebServiceUtils定时调用
   */
  public static void doRun(){
//判断是否有数据传过来，有则返回List，无则返回null
    List<Map> wList = WebServiceUtils.isHaveMap(str,K);
    List<Map> w1List = WebServiceUtils.isHaveMap(str1,K);
    List<Map> w2List = WebServiceUtils.isHaveMap(str2,K);
    List<Map> w3List = WebServiceUtils.isHaveMap(str3,K);
    //如果数据不为空就执行保存方法
    if (null!=wList){
      doSave(wList);
    }else {
      logger.info(currentYear+"年没有走读。");
    }
    if (null!=w1List){
      doSave(w1List);
    }else {
      logger.info(beforeYear1+"年没有走读。");
    }
    if (null!=w2List){
      doSave(w2List);
    }else {
      logger.info(beforeYear2+"年没有走读。");
    }
    if (null!=w3List){
      doSave(w3List);
    }else {
      logger.info(beforeYear3+"年没有走读。");
    }
  }

  /**
   * 保存数据方法
   * @param list
   */
  private static void doSave(List<Map> list){
    for (Map w:list){
      SisoWalkSchoolDto wDto =new SisoWalkSchoolDto();
      String id  = w.get("applyid").toString();
      Boolean isUpdate = walkClient.wImpl.countByPrimaryKey(id );
      wDto.setId(id);
      wDto.setProcessinstid(w.get("processinstid").toString());
      wDto.setEmpId(w.get("empid").toString());
      wDto.setEmpCode(w.get("empcode").toString());
      wDto.setEmpName(w.get("empname").toString());
      wDto.setOrgId(w.get("orgid").toString());
      wDto.setOrgName(w.get("orgname").toString());
      wDto.setGrade(w.get("nianji").toString());
      wDto.setClassName(w.get("banjiname").toString());
      wDto.setDormCode(w.get("sushecode").toString());
      wDto.setAddress(w.get("address").toString());
      wDto.setFlatmate(w.get("hezhuren").toString());
      wDto.setPhone(w.get("phone").toString());
      wDto.setWalkTime(w.get("walktime").toString());
      wDto.setReason(w.get("reason").toString());
      String endTime = w.get("endTime").toString().substring(0,19);//年月日时分秒
      wDto.setEmpty1(endTime);

      if (isUpdate){
        logger.info("update : K为"+K+", id = "+id+"的数据");
        walkClient.wImpl.updateEntityByPrimaryKey(wDto);
      }else {
        logger.info("insert : K为"+K+", id = "+id+"的数据");
        walkClient.wImpl.insertEntity(wDto);
      }

    }
  }

  //测试
//  public static void main(String[] argv) {
//
//    //invoke business method
//    //System.out.println(service.queryWalkSchool(beforeYear1));
//    //System.out.println(service.queryWalkSchool(currentYear));
////如果有数据就接收
//    List<Map> wNowList =isHaveMap(str1,K);
//for (Map walk : wNowList){
//  SisoWalkSchoolDto wDto = new SisoWalkSchoolDto();
//  wDto.setApplyId(walk.get("applyid").toString());
//}
//            List<Map> wNowList = w.get("SisoWalkSchools");//根据键值获取value
//    Map<String,List<Map>> p1 = (Map) JSONArray.parse(service.queryWalkSchool(beforeYear1));
//    List<Map> wPast1List = p1.get(K);//根据键值获取value
//    Map<String,List<Map>> p2 = (Map) JSONArray.parse(service.queryWalkSchool(beforeYear2));
//    List<Map> wPast2List = p2.get("SisoWalkSchools");//根据键值获取value
//    Map<String,List<Map>> p3 = (Map) JSONArray.parse(service.queryWalkSchool(beforeYear3));
//    List<Map> wPast3List = p3.get("SisoWalkSchools");//根据键值获取value
//    List<Map> wList = new ArrayList <>();

//    if ( 0 >= wNowList.size()){
//      logger.info(currentYear+"年暂无数据");
//    }
//    wList.addAll(wNowList);
//    if ( 0 >= wPast1List.size()){
//      logger.info(beforeYear1+"年无数据");
//    }
//    wList.addAll(wPast1List);
//    if ( 0 >= wPast2List.size()){
//      logger.info(beforeYear2+"年无数据");
//    }
//    wList.addAll(wPast2List);
//    if ( 0 >= wPast3List.size()){
//      logger.info(beforeYear3+"年无数据");
//    }
//    wList.addAll(wPast3List);
//    for (int i=0;i<wPast1List.size();i++){
//      String applyId = wPast1List.get(i).get("applyid").toString();
//      String processinstid = wPast1List.get(i).get("processinstid").toString();
//      String empId = wPast1List.get(i).get("empid").toString();
//      String empCode = wPast1List.get(i).get("empcode").toString();
//      String empName = wPast1List.get(i).get("empname").toString();
//      String orgId = wPast1List.get(i).get("orgid").toString();
//      String orgName = wPast1List.get(i).get("orgname").toString();
//      String grade = wPast1List.get(i).get("nianji").toString();
//      String className = wPast1List.get(i).get("banjiname").toString();
//      String dormCode = wPast1List.get(i).get("sushecode").toString();
//      String address = wPast1List.get(i).get("address").toString();
//      String flatmate = wPast1List.get(i).get("hezhuren").toString();
//      String phone = wPast1List.get(i).get("phone").toString();
//      String walkTime = wPast1List.get(i).get("walktime").toString();
//      String reason = wPast1List.get(i).get("reason").toString();
//      String endTime = wPast1List.get(i).get("endTime").toString().substring(0,19);//年月日时分秒
//
//      String sql = null;
//      int num = countByJdbc(applyId);
//      if (num > 0){
//        logger.info("修改id="+applyId+"的数据");
//        sql = "update siso_walk_school set "
//                + "`processinstid` = '"+ processinstid+"'"
//                + ",`emp_id` = '"+ empId+"'"
//                + ",`emp_code` = '"+ empCode+"'"
//                + ",`emp_name` = '"+ empName+"'"
//                + ",`org_id` = '"+ orgId+"'"
//                + ",`org_name` = '"+ orgName+"'"
//                + "`grade` = '"+ grade+"'"
//                + ",`class_name` = '"+ className+"'"
//                + ",`dorm_code` = '"+ dormCode+"'"
//                + ",`address` = '"+ address+"'"
//                + ",`flatmate` = '"+ flatmate+"'"
//                + ",`phone` = '"+ phone+"'"
//                + "`walk_time` = '"+ walkTime+"'"
//                + ",`reason` = '"+ reason+"'"
//                + ",`empty1` = '"+ endTime+"'"
//                + " where `apply_id` = '" +applyId+"';";
//      }else {
//        sql = "insert into siso_walk_school(`apply_id`,`processinstid`,`emp_id`,`emp_code`,`emp_name`,`org_id`,`org_name`,`grade`,`class_name`,`dorm_code`,`address`,`flatmate`,`phone`,`walk_time`,`reason`,`empty1`) values ( '"
//                + applyId + "','"
//                + processinstid + "','"
//                + empId + "','"
//                + empCode + "','"
//                + empName + "','"
//                + orgId + "','"
//                + orgName + "','"
//                + grade + "','"
//                + className + "','"
//                + dormCode + "','"
//                + address + "','"
//                + flatmate + "','"
//                + phone + "','"
//                + walkTime + "','"
//                + reason + "','"
//                + endTime + "');";
//      }
//      saveByJdbc(sql);
//    }
//
//  }
//  private static List<Map> isHaveMap(String str,String k){
//    List<Map> w = new ArrayList <>();
//    try {
//      w = WebServiceUtils.isHaveMap(str,k);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//    return w;
//  }
//
//  //用JDBC查询
//  private static Integer countByJdbc(String applyId) {
//    Connection conn = null;
//    Statement stm = null;
//    ResultSet res = null;
//    Integer num = 0;
//    try {
//      //1.连接数据库
//      conn = JdbcUtil.getConnection();
//      //2.获取到statement对象
//      stm = conn.createStatement();
//      //3.执行sql语句
//      String sql = "select count(*) from siso_walk_school where apply_id = '" + applyId+"';";
//      res = stm.executeQuery(sql);
//      while(res.next()){
//        //提倡通过列名获取
//        num = res.getInt("count(*)");
//      }
//      //关闭资源
//      JdbcUtil.close(conn, stm, res);
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }
//    return num;
//  }
//
//  //用JDBC执行SQL语句的增删改
//  private static Integer saveByJdbc(String sql) {
//    Connection conn = null;
//    Statement stm = null;
//    Integer num = 0;
//    try {
//      //1.连接数据库
//      conn = JdbcUtil.getConnection();
//      //2.获取到statement对象
//      stm = conn.createStatement();
//      //3.执行sql语句
//      num = stm.executeUpdate(sql);
//
//      //关闭资源
//      JdbcUtil.close(conn, stm);
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }
//    return num;
//  }

}
