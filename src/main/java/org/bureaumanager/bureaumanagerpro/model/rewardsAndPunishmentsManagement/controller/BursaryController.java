package org.bureaumanager.bureaumanagerpro.model.rewardsAndPunishmentsManagement.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.bureaumanager.bureaumanagerpro.base.BaseController;
import org.bureaumanager.bureaumanagerpro.model.rewardsAndPunishmentsManagement.pojo.dto.SisoBursaryDto;
import org.bureaumanager.bureaumanagerpro.model.rewardsAndPunishmentsManagement.service.impl.SisoBursaryServiceImpl;
import org.bureaumanager.bureaumanagerpro.model.stuBasicInfo.pojo.dto.SisoClassInfoDto;
import org.bureaumanager.bureaumanagerpro.model.stuBasicInfo.service.impl.SisoClassInfoServiceImpl;
import org.bureaumanager.bureaumanagerpro.sysConfig.pojo.Msg;
import org.bureaumanager.bureaumanagerpro.sysConfig.pojo.dto.SysUserDto;
import org.bureaumanager.bureaumanagerpro.sysConfig.utils.JdbcUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: WangJiaWei
 * @Date: 2019-01-31 14:00
 * @Description:
 */

@Controller
@RequestMapping("/bursary")
public class BursaryController extends BaseController{
    @Autowired
    SisoBursaryServiceImpl sisoBursaryServiceImpl;
    @Autowired
    SisoClassInfoServiceImpl sisoClassInfoService;

    @RequestMapping(value = "/all" , method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Msg findAllClass(@RequestBody(required = false) SisoBursaryDto sisoBursaryDto) {
        String orgid = sisoBursaryDto.getOrgId();
        if ("0".equals(orgid)){
            sisoBursaryDto.setOrgId(null);
        }
        String btype = sisoBursaryDto.getBursaryType();
        if (null != btype && !"".equals(btype)) {
            if (btype.equals("1")) {
                sisoBursaryDto.setBursaryType("一等奖学金");
            } else if (btype.equals("2")) {
                sisoBursaryDto.setBursaryType("二等奖学金");
            } else if (btype.equals("3")) {
                sisoBursaryDto.setBursaryType("三等奖学金");
            } else if (btype.equals("4")) {
                sisoBursaryDto.setBursaryType("企业奖学金");
            } else {
                sisoBursaryDto.setBursaryType(null);
            }
        }
        PageHelper.startPage(sisoBursaryDto.getCurrentPage(), sisoBursaryDto.getPageSize());
        List<SisoBursaryDto> list = sisoBursaryServiceImpl.queryByTemplate(sisoBursaryDto);
        PageInfo page = new PageInfo(list,5);
        return Msg.success().add("PageInfo",page);
    }

    @RequestMapping("/onestudent")
    @ResponseBody
    public Msg selectBursaryOfStudent(@RequestParam(value="currentPage",defaultValue="1")Integer currentPage
            ,@RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize
            ,@Param(value = "t") SisoBursaryDto t){
        if (null== SecurityUtils.getSubject().getSession()){
            //logger.info("Session has been invalidated! Can not select Bursary Of Student!");
            return Msg.failure();
        }
        SysUserDto userDto = (SysUserDto) SecurityUtils.getSubject().getSession().getAttribute("user");
        SisoBursaryDto bursaryDto = new SisoBursaryDto();
        if (null != t){
            bursaryDto = t;
        }
        bursaryDto.setEmpId(userDto.getId());
        PageHelper.startPage(currentPage, pageSize);
        List<SisoBursaryDto> pList = sisoBursaryServiceImpl.selectEntitiesByTemplate(bursaryDto);
        PageInfo page = new PageInfo(pList,5);
        return Msg.success().add("PageInfo",page);
    }

    @RequestMapping(value = "/tree",method =  {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    @Override
    public List<Map> selectCollegeAndClassTree(@RequestParam(value = "grade",defaultValue = "",required = false) String grade) throws Exception{
        return super.selectCollegeAndClassTree(grade);
    }

    private static Long countByJdbc(String orgid) {
        Connection conn = null;
        Statement stm = null;
        ResultSet res = null;
        Long num = Long.valueOf(0);
        try {
            //1.连接数据库
            conn = JdbcUtil.getConnection();
            //2.获取到statement对象
            stm = conn.createStatement();
            //3.执行sql语句
            String sql = "select count(id) num from siso_bursary where orgid=" + orgid;
            res = stm.executeQuery(sql);
            while(res.next()){
                //提倡通过列名获取
                num = res.getLong("num");
            }
            //关闭资源
            JdbcUtil.close(conn, stm, res);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return num;
    }
}
