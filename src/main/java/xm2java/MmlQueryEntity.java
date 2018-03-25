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
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

public class MmlQueryEntity {

    private String query;
    private boolean breakAfterFirstError;

    @XmlValue
    public void setQuery(String query) {
        this.query = query;
    }

    @XmlAttribute(name = "breakAfterFirstError")
    public void setBreakAfterFirstError(boolean breakAfterFirstError) {
        this.breakAfterFirstError = breakAfterFirstError;
    }

    @Override
    public String toString() {
        return "MmlQuery{" + "query=" + query + '}';
    }

    public String getQuery() {
        return query;
    }

    public boolean isBreakAfterFirstError() {
        return breakAfterFirstError;
    }

}
