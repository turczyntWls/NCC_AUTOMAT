/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xm2java;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

/**
 * @author turczyt
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ncc")
public class NccEntity 
{
    @XmlAttribute(name="name")
    private String name;
    
    @XmlAttribute(name="countAfterUpdate")
    private boolean countAfterUpdate;
    
    @XmlAttribute(name="countMML")   
    private boolean countMML;
    
    @XmlAttribute(name="executeMML")   
    private boolean executeMML;
    
    
    @XmlElement(name="countQuery")
    private String countQuery;
    
    
    @XmlElementWrapper(name = "beforeQuerys")
    @XmlElement(name = "query")
    private java.util.List<String> beforeQuerys;
    
    @XmlElementWrapper(name = "afterQuerys")
    @XmlElement(name = "query")
    private java.util.List<String> afterQuerys;
    
    @XmlElementWrapper(name = "mmlQuerys")
    @XmlElement(name = "mmlQuery")
    private java.util.List<MmlQueryEntity> mmlQueryList;
    
    private String countedProblemBefore;
    private String countedProblemAfter;

    
    public String getName() 
    {
        return name;
    }
    
    public void setName(String name) 
    {
        this.name = name;
    }

    public boolean isCountAfterUpdate()
    {
        return countAfterUpdate;
    }

   
    public void setCountAfterUpdate(boolean countAfterUpdate) 
    {
        this.countAfterUpdate = countAfterUpdate;
    }

   public String getCountQuery() 
    {
        return countQuery;
    }

  
    public void setCountQuery(String countQuery) 
    {
        this.countQuery = countQuery;
    }
 
    public boolean ifCountMML() 
    {
        return isCountMML();
    }
    
    public void setCountMML(boolean countMML) 
    {
        this.countMML = countMML;
    }

    public List<String> getBeforeQuerys() {
        return beforeQuerys;
    }

    public void setBeforeQuerys(List<String> beforeQuerys) {
        this.beforeQuerys = beforeQuerys;
    }

    public List<String> getAfterQuerys() {
        return afterQuerys;
    }

    public void setAfterQuerys(List<String> afterQuerys) {
        this.afterQuerys = afterQuerys;
    }

    public boolean isMmlExecutable() {
        return executeMML;
    }

    public void setMmlExecutable(boolean executeMML) {
        this.executeMML = executeMML;
    }


    public String getCountedProblemBefore() {
        return countedProblemBefore;
    }

    public void setCountedProblemBefore(String countedProblemBefore) {
        this.countedProblemBefore = countedProblemBefore;
    }

    public String getCountedProblemAfter() {
        if(this.isMmlExecutable())
            return countedProblemAfter;
        else
            return "--";
    }

     public void setCountedProblemAfter(String countedProblemAfter) {
        this.countedProblemAfter = countedProblemAfter;
    }

    @Override
    public String toString() {
        return "Ncc{" + "name=" + getName() + ", countAfterUpdate=" + isCountAfterUpdate() + ", countMML=" + isCountMML() + ", executeMML=" + isMmlExecutable() + ", countQuery=" + getCountQuery() + ", beforeQuerys=" + getBeforeQuerys() + ", afterQuerys=" + getAfterQuerys() + ", mmlQueryList=" + getMmlQueryList() + '}';
    }

    public boolean isCountMML() {
        return countMML;
    }

    public List<MmlQueryEntity> getMmlQueryList() {
        return mmlQueryList;
    }

    /**
     * @param mmlQueryList the mmlQueryList to set
     */
    public void setMmlQueryList(java.util.List<MmlQueryEntity> mmlQueryList) {
        this.mmlQueryList = mmlQueryList;
    }
}