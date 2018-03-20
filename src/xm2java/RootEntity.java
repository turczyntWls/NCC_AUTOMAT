/*
 * To change this license header, choose License Headers in Project RootEntity.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xm2java;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author turczyt
 */
@XmlRootElement(name = "root")
@XmlAccessorType(XmlAccessType.FIELD)
public class RootEntity 
{
    @XmlElement(name = "ncc")
    private java.util.List<NccEntity> nccs;
    
    @XmlElement(name = "atmoLegendaHtmlPoTabelce")
    private String atmoLegendaHtmlPoTabelce;

    @XmlElement(name = "atmoUserId")
    private String atmoUserId;

    @XmlElement(name = "atmoCategory")
    private String atmoCategory;

    @XmlElement(name = "atmoCreatedByGroup")
    private String atmoCreatedByGroup;

    @XmlElement(name = "gatmoImplementingGroup")
    private String gatmoImplementingGroup;

    @XmlElement(name = "atmoNetworkElement")
    private String atmoNetworkElement;

    @XmlElement(name = "atmoPriority")
    private String atmoPriority;

    @XmlElement(name = "atmoServiceAffecting")
    private boolean atmoServiceAffecting;

    @XmlElement(name = "atmoSummary")
    private String atmoSummary;

    @XmlElement(name = "atmoVerifyGroup")
    private String atmoVerifyGroup;
    
    @XmlElement(name = "northLogin")
    private String northLogin;
    
    @XmlElement(name = "northPassword")
    private String northPassword;

    public List<NccEntity> getNccs()
    {
        return nccs;
    }

   
    public void setNccs(List<NccEntity> nccs) 
    {
        this.nccs = nccs;
    }

    public String getAtmoLegendaHtmlPoTabelce() 
    {
        return atmoLegendaHtmlPoTabelce;
    }

  
    public void setAtmoLegendaHtmlPoTabelce(String atmoLegendaHtmlPoTabelce) 
    {
        this.atmoLegendaHtmlPoTabelce = atmoLegendaHtmlPoTabelce;
    }

    public String getAtmoUserId() 
    {
        return atmoUserId;
    }

    public void setAtmoUserId(String atmoUserId) 
    {
        this.atmoUserId = atmoUserId;
    }

    public String getAtmoCategory() 
    {
        return atmoCategory;
    }

    public void setAtmoCategory(String atmoCategory) 
    {
        this.atmoCategory = atmoCategory;
    }

    public String getAtmoCreatedByGroup() 
    {
        return atmoCreatedByGroup;
    }

    public void setAtmoCreatedByGroup(String atmoCreatedByGroup) 
    {
        this.atmoCreatedByGroup = atmoCreatedByGroup;
    }

    public String getGatmoImplementingGroup() 
    {
        return gatmoImplementingGroup;
    }

    public void setGatmoImplementingGroup(String gatmoImplementingGroup) 
    {
        this.gatmoImplementingGroup = gatmoImplementingGroup;
    }

    public String getAtmoNetworkElement() 
    {
        return atmoNetworkElement;
    }

    public void setAtmoNetworkElement(String atmoNetworkElement) 
    {
        this.atmoNetworkElement = atmoNetworkElement;
    }

    public String getAtmoPriority() 
    {
        return atmoPriority;
    }

    public void setAtmoPriority(String atmoPriority) 
    {
        this.atmoPriority = atmoPriority;
    }

    public boolean isAtmoServiceAffecting() 
    {
        return atmoServiceAffecting;
    }

    public void setAtmoServiceAffecting(boolean atmoServiceAffecting) 
    {
        this.atmoServiceAffecting = atmoServiceAffecting;
    }

    public String getAtmoSummary() 
    {
        return atmoSummary;
    }

    public void setAtmoSummary(String atmoSummary) 
    {
        this.atmoSummary = atmoSummary;
    }

    public String getAtmoVerifyGroup() 
    {
        return atmoVerifyGroup;
    }

    public void setAtmoVerifyGroup(String atmoVerifyGroup) 
    {
        this.atmoVerifyGroup = atmoVerifyGroup;
    }

    public String getNorthLogin() {
        return northLogin;
    }

    public void setNorthLogin(String northLogin) {
        this.northLogin = northLogin;
    }

    public String getNorthPassword() {
        return northPassword;
    }

    public void setNorthPassword(String northPassword) {
        this.northPassword = northPassword;
    }

    @Override
    public String toString() 
    {
        return "Properties{ atmoUserId=" + atmoUserId + "\r\n atmoCategory=" + atmoCategory + "\r\n atmoCreatedByGroup=" + atmoCreatedByGroup + "\r\n gatmoImplementingGroup=" + gatmoImplementingGroup + "\r\n atmoNetworkElement=" + atmoNetworkElement + "\r\n atmoPriority=" + atmoPriority + "\r\n atmoServiceAffecting=" + atmoServiceAffecting + "\r\n atmoSummary=" + atmoSummary + "\r\n atmoVerifyGroup=" + atmoVerifyGroup +"\r\n atmoLegendaHtmlPoTabelce=" + atmoLegendaHtmlPoTabelce  + "\r\n\r\n nccs=" + nccs + '}';
    }

}
