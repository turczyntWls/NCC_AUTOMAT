package ncc_automat;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nbipackage.NBIAnsException;
import nbipackage.NorthB;

public class MmlExecutor {

    private java.util.List<MMLCommand> toExecute;
    private Statement testStatement;
    private Logger logger;

    private int execOk;
    private int problem;
    private String nccName;
    private String northLogin;
    private String northPassword;

    public MmlExecutor(List<MMLCommand> toExecute, Statement testStatement, Logger logger, String northLogin, String northPassword) {
        this.northLogin = northLogin;
        this.northPassword = northPassword;
        this.problem = 0;
        this.execOk = 0;
        this.toExecute = toExecute;
        this.testStatement = testStatement;
        this.logger = logger;
        nccName = "";
    }

    public void execute() throws NBIAnsException {
        logger.info("[" + nccName + "]\t-  NORTHB_EXECUTE: start ");
        NorthB north = null;

        for (MMLCommand m : this.toExecute) {
            if ((north = getNorthB(north, m.getM2000())) != null) {
                boolean northProblem = executeSingleMML(m, north);
                m.setNorthSucced(!northProblem);
                if (!northProblem) {
                    executeSql(m);
                }
            }
        }
        if (north != null) {
            north.closeBuffor();
        }
        logger.log(Level.INFO, "[" + nccName + "]\t-  NORTHB_EXECUTE:end,  ALL=" +( getExecOk()+getProblem()) + " SUCCEEDED=" + getExecOk() + ",PROBLEMATIC=" + getProblem());
    }

    private NorthB getNorthB(NorthB north, String m2000Ip) {
        if (m2000Ip == null) {
            return null;
        }

        if (north == null) {
            return new nbipackage.NorthB(m2000Ip, this.northLogin, this.northPassword, null);
        } else if (north.getSerwer() == null || north.getSerwer().equals("")) {
            return new nbipackage.NorthB(m2000Ip, this.northLogin, this.northPassword, null);
        } else if (north.getSerwer().equals(m2000Ip)) {
            return north;
        } else {
            north.closeBuffor();
            return new nbipackage.NorthB(m2000Ip, this.northLogin, this.northPassword, null);
        }
    }

    private boolean executeSingleMML(MMLCommand m, NorthB north) {
        String[] commands = m.getCommand().split(";");
        boolean northProblem = false;
        for (String mmlKom : commands) {
            try {
                String makeans = north.make(m.getNeName(), mmlKom);
                int indexOf = makeans.indexOf(MMLCommand.SUCCEED_RETCODE_PATTERN);
                m.setExecuteCommAns(makeans);
                if (indexOf != -1) {
                    m.setRetcodeLine(makeans.substring(indexOf, makeans.indexOf("\n", indexOf)));
                    logger.log(Level.ALL, "[" + this.nccName + "]\t-  NORTHB_EXECUTE:" + mmlKom + " \r\n" + makeans.substring(indexOf, makeans.indexOf("\n", indexOf)));
                    execOk++;
                } else {
                    northProblem = true;
                    problem++;
                    logger.warning("[" + this.nccName + "]\t-  NORTHB_EXECUTE:northB exception, COMMAND=" + mmlKom + "\r\n NORTHB_EXCEPION=\r\n" + makeans);
                    if (m.isBreakAfterFirstError()) {
                        break;
                    }
                }
            } catch (Exception e) {
                northProblem = true;
                e.printStackTrace();
            }
        }
        return northProblem;
    }

    private void executeSql(MMLCommand m) {
        if (!m.getSqlUpdateQuery().isEmpty()) {
            try {
                int executeUpdate = testStatement.executeUpdate(m.getSqlUpdateQuery());
                m.setSqlUpdated(true);
            } catch (SQLException ex) {
                m.setSqlExceptionInfo(ex.getMessage());
                logger.warning("[" + this.nccName + "]\t-  NORTHB_EXECUTE:sql exception, QUERY=" + m.getSqlUpdateQuery() + "\r\nSQL_EXCEPTION:" + m.getSqlExceptionInfo());
            }
        }
    }

    public int getExecOk() {
        return execOk;
    }

    public void setExecOk(int execOk) {
        this.execOk = execOk;
    }

    public int getProblem() {
        return problem;
    }

    public void setProblem(int problem) {
        this.problem = problem;
    }

    public void setNccName(String nccName) {
        this.nccName = nccName;
    }

    public List<MMLCommand> getToExecute() {
        return toExecute;
    }

    public void setToExecute(LinkedList<MMLCommand> toExecute) {
        this.toExecute = toExecute;
    }

    public Statement getTestStatement() {
        return testStatement;
    }

    public void setTestStatement(Statement testStatement) {
        this.testStatement = testStatement;
    }

}