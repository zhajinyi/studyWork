package org.bureaumanager.bureaumanagerpro.sysConfig.webService.fwdtStudentAward;

import org.bureaumanager.bureaumanagerpro.model.rewardsAndPunishmentsManagement.pojo.dto.SisoGraduateInfoDto;
import org.bureaumanager.bureaumanagerpro.model.rewardsAndPunishmentsManagement.service.impl.SisoGraduateInfoServiceImpl;
import org.bureaumanager.bureaumanagerpro.sysConfig.utils.WebServiceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Component
public class StudentAwardClient3 {
    private static final Logger logger= LoggerFactory.getLogger(StudentAwardClient3.class);
    private static final StudentExcellAwardService service = new StudentExcellAwardServiceService().getPort(StudentExcellAwardService.class);

    @Autowired
    private SisoGraduateInfoServiceImpl spImpl;
    private static StudentAwardClient3 studentAwardClient;

    @PostConstruct
    public void init() {
        studentAwardClient = this;
        studentAwardClient.spImpl = this.spImpl;
    }

    private static String str1 = service.querySisoGraduateApplyInfos();
    private static final String K1 = "SisoGraduateApplyImpls";
    public static void doRun(){
    //判断是否有数据传过来，有则返回List，无则返回null
        List<Map>  SisoGraduateApplyList = WebServiceUtils.isHaveMap(str1,K1);
        if (null!=SisoGraduateApplyList){
            doSave(SisoGraduateApplyList);
        }else {
            logger.info(K1+"没有数据!");
        }
    }

    private static void doSave(List<Map> SisoGraduateApplyList){
        for (Map p:SisoGraduateApplyList){
            SisoGraduateInfoDto pDto =new SisoGraduateInfoDto();
            String id = p.get("applyid").toString();
            Boolean isUpdate = studentAwardClient.spImpl.countByPrimaryKey(id);
            pDto.setId(id);
            pDto.setEmpid(p.get("empid").toString());
            pDto.setReferrer(p.get("referrer").toString());
            pDto.setEmpname(p.get("empname").toString());
            pDto.setOrgid(p.get("orgid").toString());
            pDto.setOrgname(p.get("orgname").toString());
            pDto.setRemark(p.get("remark").toString());
            pDto.setClassname(p.get("classname").toString());
            pDto.setClassid(p.get("classid").toString());
            pDto.setAvgsort1(p.get("avgsort1").toString());
            pDto.setAvgsort1(p.get("avgsort2").toString());
            pDto.setPhone(p.get("phone").toString());
            pDto.setAwardinfo(p.get("awardinfo").toString());
            pDto.setFiletype(p.get("filetype").toString());
            pDto.setGender(p.get("gender").toString());
            pDto.setEndtime(p.get("endtime").toString());
            if (isUpdate){
                logger.info("update : K为"+K1+", id = "+id+"的数据");
                studentAwardClient.spImpl.updateEntityByPrimaryKey(pDto);
            }else {
                logger.info("insert : K为"+K1+", id = "+id+"的数据");
                studentAwardClient.spImpl.insertEntity(pDto);
            }
        }
    }

}
