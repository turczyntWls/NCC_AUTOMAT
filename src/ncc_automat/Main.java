package ncc_automat;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import mysqlpackage.DataSource;

import xm2java.NccEntity;
import xm2java.RootEntity;
import xm2java.XmlParser;

public class Main {

    public static void main(String[] args) throws JAXBException, IOException, SQLException, Exception {
        String katalogPath = "/usr/samba/utran/PP/Logs/NCC_AUTOMAT/";
        String xmlPath = "NCC_AUTOMAT.xml";
        java.util.logging.Level levelOfLoggin = Level.FINER;
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equalsIgnoreCase("-h")) {
                    System.out.println("Dostepne parametry wywolania:");
                    System.out.println("\t-h");
                    System.out.println("\t-f sciezka_do_xml");
                    System.out.println("\t-LOG_NORMAL_LEVEL");
                    System.out.println("\t-LOG_DEBUG_LEVEL");
                    System.out.println("\r\nexample:-f /katalog/katalog1/daneWejsciowe.xml -LOG_NORMAL_LEVEL");
                    System.exit(0);
                }
                if (args[i].equalsIgnoreCase("-f")) {
                    if ((i++) < args.length) {
                        xmlPath = args[i];
                    } else {
                        System.out.println("\r\n\r\nBledne wywolanie: brak sciezki do pliku konfiguracyjnego,wymaganej po atrybucie -f");
                        System.exit(0);
                    }
                }
                if (args[i].equalsIgnoreCase("-LOG_NORMAL_LEVEL")) {
                    levelOfLoggin = Level.FINER;
                }

                if (args[i].equalsIgnoreCase("-LOG_DEBUG_LEVEL")) {
                    levelOfLoggin = Level.ALL;
                }
            }
        }
        Logger logger = DefaultLoggerFactory.getDefaultLogger(katalogPath, levelOfLoggin);
        mysqlpackage.DataSource DOA = DataSource.getInstance();

        logger.log(Level.INFO, "[ROOT]\t-  PARSING XML: start " + xmlPath);
        RootEntity root = XmlParser.getProperties(xmlPath);
        logger.log(Level.INFO, "[ROOT]\t-  PARSING XML: " + root.getNccs().size() + " ncc's founded");
        logger.log(Level.ALL, root.toString());
        List<NccEntity> nccAll = root.getNccs();
        logger.log(Level.INFO, "[ROOT]\t-  PROCESSING NCC: start");

        java.util.LinkedList<NccExecutor> nccExecutorList = new java.util.LinkedList<>();
        for (NccEntity ncc : nccAll) {
            NccExecutor exec = new NccExecutor(ncc, DOA, logger, root.getNorthLogin(), root.getNorthPassword());
            exec.execute();
            nccExecutorList.add(exec);

        }
       DOA.close();
        logger.log(Level.INFO, "[ROOT]\t-  PROCESSING NCC: end");
        logger.log(Level.INFO, "[ROOT]\t-  WO: start");
        logger.log(Level.INFO, "[ROOT]\t-  WO: create attachments");

        WoExecutor woExecutor = new ncc_automat.WoExecutor(root, nccExecutorList, logger);
        int addWoToAtmo = woExecutor.addWoToAtmo();
        if (addWoToAtmo > 0) {
            int iloscDodanychZalacznikow = woExecutor.addAttachments2Wo();
            logger.log(Level.INFO, "[ROOT]\t-  WO:attached " + iloscDodanychZalacznikow + " files(woId=" + addWoToAtmo + ")");
            boolean addDependentNetworkElements2Wo = woExecutor.addDependentNetworkElements2Wo();
            logger.log(Level.INFO, "[ROOT]\t-  WO:add dependent network elements,succeeded=" + addDependentNetworkElements2Wo + "(woId=" + addWoToAtmo + ")");
            woExecutor.sendWoToImplementation();
            logger.log(Level.INFO, "[ROOT]\t-  WO: send to implementation(woId=" + addWoToAtmo + ")");
            int probaft;
            if((probaft=woExecutor.getSumaryProblemAfter())==0)
            {
                String komentarz = "automat";
                woExecutor.takeFromQueueAndClose(komentarz);
                logger.log(Level.INFO, "[ROOT]\t-  WO: close(woId=" + addWoToAtmo + ")");
            }
            else
                logger.log(Level.INFO, "[ROOT]\t-  WO:SumaryProblemAfter="+probaft+", left unclosed(woId=" + addWoToAtmo + ")");
        }
        logger.log(Level.INFO, "[ROOT]\t-  WO:END(woId=" + addWoToAtmo + ")");
    }
}