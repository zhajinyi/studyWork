
package org.bureaumanager.bureaumanagerpro.sysConfig.webService.fwdtStudentAward;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.bureaumanager.bureaumanagerpro.sysConfig.webService.fwdtStudentAward package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _QuerySisoExcelleaderINfosResponseJSONSisoExcelleaderInfos_QNAME = new QName("http://www.primeton.com/studentExcellAwardService", "JSONSisoExcelleaderInfos");
    private final static QName _QuerySisoGraduateApplyInfosResponseJSONSisoGraduateApplyInfos_QNAME = new QName("http://www.primeton.com/studentExcellAwardService", "JSONSisoGraduateApplyInfos");
    private final static QName _QuerySisoExcelleaderINfosExcellentyear_QNAME = new QName("http://www.primeton.com/studentExcellAwardService", "excellentyear");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.bureaumanager.bureaumanagerpro.sysConfig.webService.fwdtStudentAward
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link QuerySisoGraduateApplyInfosResponse }
     * 
     */
    public QuerySisoGraduateApplyInfosResponse createQuerySisoGraduateApplyInfosResponse() {
        return new QuerySisoGraduateApplyInfosResponse();
    }

    /**
     * Create an instance of {@link QuerySisoGraduateApplyInfos }
     * 
     */
    public QuerySisoGraduateApplyInfos createQuerySisoGraduateApplyInfos() {
        return new QuerySisoGraduateApplyInfos();
    }

    /**
     * Create an instance of {@link QuerySisoExcelleaderINfosResponse }
     * 
     */
    public QuerySisoExcelleaderINfosResponse createQuerySisoExcelleaderINfosResponse() {
        return new QuerySisoExcelleaderINfosResponse();
    }

    /**
     * Create an instance of {@link QuerySisoExcelleaderINfos }
     * 
     */
    public QuerySisoExcelleaderINfos createQuerySisoExcelleaderINfos() {
        return new QuerySisoExcelleaderINfos();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.primeton.com/studentExcellAwardService", name = "JSONSisoExcelleaderInfos", scope = QuerySisoExcelleaderINfosResponse.class)
    public JAXBElement<String> createQuerySisoExcelleaderINfosResponseJSONSisoExcelleaderInfos(String value) {
        return new JAXBElement<String>(_QuerySisoExcelleaderINfosResponseJSONSisoExcelleaderInfos_QNAME, String.class, QuerySisoExcelleaderINfosResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.primeton.com/studentExcellAwardService", name = "JSONSisoGraduateApplyInfos", scope = QuerySisoGraduateApplyInfosResponse.class)
    public JAXBElement<String> createQuerySisoGraduateApplyInfosResponseJSONSisoGraduateApplyInfos(String value) {
        return new JAXBElement<String>(_QuerySisoGraduateApplyInfosResponseJSONSisoGraduateApplyInfos_QNAME, String.class, QuerySisoGraduateApplyInfosResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.primeton.com/studentExcellAwardService", name = "excellentyear", scope = QuerySisoExcelleaderINfos.class)
    public JAXBElement<String> createQuerySisoExcelleaderINfosExcellentyear(String value) {
        return new JAXBElement<String>(_QuerySisoExcelleaderINfosExcellentyear_QNAME, String.class, QuerySisoExcelleaderINfos.class, value);
    }

}
