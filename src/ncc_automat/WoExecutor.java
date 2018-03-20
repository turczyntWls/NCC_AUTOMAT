/*
 * To change this license header, choose License Headers in Project RootEntity.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ncc_automat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import nbipackage.NewFile;
import soapOperations.AtmoWithAuth;
import xm2java.RootEntity;

/**
 *
 * @author turczyt
 */
public class WoExecutor {

    private java.util.List<NccExecutor> nccExecLst;
    private Logger logger;
    private RootEntity root;
    private int addWoToAtmo;
    private AtmoWithAuth atmo;

    public WoExecutor(RootEntity root, List<NccExecutor> nccExecLst, Logger logger) {
        this.nccExecLst = nccExecLst;
        this.logger = logger;
        this.root = root;
        this.atmo = new AtmoWithAuth(root.getAtmoUserId());
    }

    public int addWoToAtmo() throws JAXBException {

        soapObjects.WorkOrder newWo = new soapObjects.WorkOrder();
        newWo.setCategory(root.getAtmoCategory());
        newWo.setCreatedByGroup(root.getAtmoCreatedByGroup());
        newWo.setDescription(prepareDescription());
        newWo.setImplementingGroup(root.getGatmoImplementingGroup());
        newWo.setNetworkElement(root.getAtmoNetworkElement());
        newWo.setPriority(root.getAtmoPriority());
        newWo.setServiceAffecting(false);
        newWo.setSummary(root.getAtmoSummary());
        newWo.setVerifyGroup(root.getAtmoVerifyGroup());

        addWoToAtmo = atmo.addWoToAtmo(newWo);
        logger.log(Level.INFO, "[ROOT]\t-  WO: Add to atmo(woId="+ addWoToAtmo+")");
        return addWoToAtmo;
    }

    public boolean sendWoToImplementation() throws JAXBException {
        return atmo.sendToImplementationRequest(addWoToAtmo, "send to implementation", root.getGatmoImplementingGroup(), false);
    }

    public boolean addDependentNetworkElements2Wo() {

        java.util.HashSet<String> elementyZalezne = new java.util.HashSet<String>();
        for (NccExecutor exec : this.nccExecLst) 
        {
            List<MMLCommand> mmlToExecute = exec.getMmlToExecute();
            for (MMLCommand mml : mmlToExecute) 
            {
                elementyZalezne.addAll(mmlCommand2DependElement(mml));
            }
        }
        try 
        {
            List<String> list = new ArrayList<String>(elementyZalezne);
            if (!list.isEmpty()) 
            {
                atmo.addDependentNetworkElementsRequest(addWoToAtmo, "dodane elementy zalezne", list);
                return true;
            }
        } catch (Exception ee) 
        {
            logger.throwing(this.getClass().getName(), "addDependentNetworkElementsRequest", ee);
        }
        return false;
        
    }
    
    public int addAttachments2Wo() 
    {
        int attachedFiles=0;
        logger.log(Level.INFO, "[ROOT]\t-  WO: create attachments");
        java.util.List<String> attachsPath=new java.util.LinkedList<>();
        attachsPath.add(createMmlFile().pass());
        attachsPath.add(logger.getName());
        
        for(String path:attachsPath)
        {
            try{
                    atmo.addAttachmentToWo(addWoToAtmo, path);
                    attachedFiles++;
            }
            catch(JAXBException jax)
            {
                logger.throwing(this.getClass().getSimpleName(), "addAttachmentToWo", jax);
            }
            catch(IOException io)
            {
                logger.throwing(this.getClass().getSimpleName(), "addAttachmentToWo", io);
            }
        }    
       
        return attachedFiles;
    }
    
    public void takeFromQueueAndClose(String comment) throws JAXBException, DatatypeConfigurationException
    {
        this.atmo.takeFromQueueRequest(addWoToAtmo);
         logger.log(Level.INFO, "[ROOT]\t-  WO: Take from queue(woId="+ addWoToAtmo+")");
        this.atmo.reportTaskFinished(addWoToAtmo,comment, false);
        logger.log(Level.INFO, "[ROOT]\t-  WO: Close(woId="+ addWoToAtmo+")");
    }
    public void doCancel(String comment) throws JAXBException
    {
        this.atmo.doCancel(addWoToAtmo,comment, false);
        logger.log(Level.INFO, "[ROOT]\t-  WO: Cancel(woId="+ addWoToAtmo+")");
    }
    private NewFile createMmlFile() {
        String  DATE_FORMAT_NOW = "yyyy_MM_dd";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT_NOW);
        String dataPattern = sdf.format(new java.util.Date());
        String katalogPath = "/usr/samba/utran/PP/Logs/NCC_AUTOMAT/";
        NewFile mmls = new NewFile(katalogPath+"MML_"+dataPattern+".txt", "n");
        for (NccExecutor exec : this.nccExecLst)
        {
            List<MMLCommand> mmlToExecute = exec.getMmlToExecute();
            mmls.dopisz("////////////////"+exec.getNccEntity().getName()+"//////////////////////\r\n");
            for (MMLCommand mml : mmlToExecute)
            {
                if(mml.getCommand()!=null)
                {
                    String[] commands = mml.getCommand().split(";");
                    for(String comm:commands)
                        mmls.dopisz(comm+"{"+mml.getNeName()+"}"+"\r\n");
                }
            }
        }
        return mmls;
    }

    private java.util.HashSet<String> mmlCommand2DependElement(MMLCommand mml) {
        java.util.HashSet<String> tmp = new java.util.HashSet<String>();

        if (mml.isNorthSucced() && !mml.getNeName().isEmpty()) {
            String[] tokeny = mml.getNeName().split("_", 4);

            if (tokeny.length > 1) {
                tmp.add("NodeB \\ " + tokeny[1]);
                tmp.add("eNodeB \\ " + tokeny[1]);

            } else {
                if (mml.getNeName().contains("RNC")) {
                    tmp.add("RNC \\ " + mml.getNeName());
                }
                if (mml.getNeName().contains("BSC")) {
                    tmp.add("BSC \\ " + mml.getNeName());
                }
            }
        }
        return tmp;

    }

    private String prepareDescription() {
        StringBuilder descr = new StringBuilder();

        descr
                .append(prepareTableBody())
                .append(root.getAtmoLegendaHtmlPoTabelce())
                .append("  </body>\n")
                .append("</html>");
        return descr.toString();
    }

    private String prepareTableBody() {
        StringBuilder tableBody = new StringBuilder();
        tableBody.append("<html>\n"
                + "<body>\n"
                + "\n"
                + "    <table >\n"
                + "      <thead>\n"
                + "        <tr>\n"
                + "          <th >Nazwa NCC</th>\n"
                + "          <th >Bledy przed</th>\n"
                + "          <th >Bledy po</th>\n"
                + "          <th >NorthB auto</th>\n"
                + "          <th >MMle wygenerowane</th>\n"
                + "          <th >wykonane poprawnie</th>\n"
                + "          <th >wykonane blednie</th>\n"
                + "        </tr>\n"
                + "      </thead>\n"
                + "      <tbody>\n"
        );

        for (NccExecutor exec : this.nccExecLst) {
            tableBody.append(prepareTableRow(exec));
        }
        tableBody.append("</tbody>\n")
                .append("</table>\n");
        return tableBody.toString();
    }

    private String prepareTableRow(NccExecutor exec) {
        String tmp
                = "<tr>\n"
                + "    <td >" + exec.getNccEntity().getName() + "</td>\n"
                + "    <td >" + exec.getNccEntity().getCountedProblemBefore() + "</td>\n"
                + "    <td >" + exec.getNccEntity().getCountedProblemAfter() + "</td>\n"
                + "    <td >" + exec.getNccEntity().isMmlExecutable() + "</td>\n"
                + "    <td >" + exec.getMmlToExecute().size() + "</td>\n"
                + "    <td >" + exec.getPoprawnie() + "</td>\n"
                + "    <td >" + exec.getBlednie() + "</td>\n"
                + "</tr>\n";
        return tmp;
    }
}
