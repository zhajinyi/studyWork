package org.bureaumanager.bureaumanagerpro.sysConfig.webService.fwdtStudentAward;

import org.bureaumanager.bureaumanagerpro.model.rewardsAndPunishmentsManagement.pojo.dto.SisoExcelleaderInfoDto;
import org.bureaumanager.bureaumanagerpro.model.rewardsAndPunishmentsManagement.service.impl.SisoExcelleaderInfoServiceImpl;
import org.bureaumanager.bureaumanagerpro.sysConfig.utils.WebServiceUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Component
public class StudentAwardClient2 {
    private static final Logger logger= LoggerFactory.getLogger(StudentAwardClient3.class);
    private static final StudentExcellAwardService service = new StudentExcellAwardServiceService().getPort(StudentExcellAwardService.class);

    @Autowired
    private SisoExcelleaderInfoServiceImpl spImpl;
    private static StudentAwardClient2 studentAwardClient2;

    @PostConstruct
    public void init() {
        studentAwardClient2 = this;
        studentAwardClient2.spImpl = this.spImpl;
    }


    private static final String K1 = "SisoExcelleaderImpls";
    public static void doRun(){
        //判断是否有数据传过来，有则返回List，无则返回null
        String[] a={"2017-2018","2018-2019","2019-2020"};
        for (int i = 0;i<a.length;i++){
            List<Map> SisoExcelleaderInfoList = WebServiceUtils.isHaveMap(service.querySisoExcelleaderINfos(a[i]),K1);
            if (null!=SisoExcelleaderInfoList){
                doSave(SisoExcelleaderInfoList);
            }else {
                logger.info(K1+"在"+a[i]+"没有数据!");
            }
        }

    }

    private static void doSave(List<Map> SisoExcelleaderInfoList){
        for (Map p:SisoExcelleaderInfoList){
            SisoExcelleaderInfoDto pDto =new SisoExcelleaderInfoDto();
            String id = p.get("applyid").toString();
            Boolean isUpdate = studentAwardClient2.spImpl.countByPrimaryKey(id);
            pDto.setId(id);
            pDto.setEmpid(p.get("empid").toString());
            pDto.setEmpname(p.get("empname").toString());
            pDto.setOrgid(p.get("orgid").toString());
            pDto.setOrgname(p.get("orgname").toString());
            pDto.setSex(p.get("sex").toString());
            pDto.setBanji(p.get("banji").toString());
            pDto.setAdmissionnumber(p.get("admissionnumber").toString());
            pDto.setPhone(p.get("phone").toString());
            pDto.setExcellentyear(p.get("excellentyear").toString());
            pDto.setJob(p.get("job").toString());
            pDto.setSelfassessment(p.get("selfassessment").toString());
            pDto.setAvegrafed(p.get("avegrafde").toString());
            pDto.setLowgrafed(p.get("lowgrade").toString());
            pDto.setClassrank(p.get("classrank").toString());
            pDto.setEndtime(p.get("endtime").toString());
            if (isUpdate){
                logger.info("update : K为"+K1+", id = "+id+"的数据");
                studentAwardClient2.spImpl.updateEntityByPrimaryKey(pDto);
            }else {
                logger.info("insert : K为"+K1+", id = "+id+"的数据");
                studentAwardClient2.spImpl.insertEntity(pDto);
            }
        }
    }
}
