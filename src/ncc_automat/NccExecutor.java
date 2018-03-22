/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ncc_automat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mysqlpackage.Baza;
import mysqlpackage.DataSource;
import mysqlpackage.OdpowiedzSQL;
import nbipackage.NBIAnsException;
import xm2java.MmlQueryEntity;
import xm2java.NccEntity;

public class NccExecutor
{

    private NccEntity nccEntity;
    private final mysqlpackage.DataSource DOA;
    private Connection connection = null;
    private Statement testStatement = null;
    private java.util.List<MMLCommand> mmlToExecute;
    private Logger logger;
    private int poprawnie;
    private int blednie;
    private String northLogin;
    private String northPassword;

    public NccExecutor(NccEntity nccEntity, DataSource DOA, Logger logger, String northLogin, String northPassword) throws SQLException, Exception
    {
        this.nccEntity = nccEntity;
        this.DOA = DOA;
        this.logger = logger;
        this.northLogin = northLogin;
        this.northPassword = northPassword;
        poprawnie = -1;
        blednie = -1;
    }

    public void execute() throws Exception
    {
        logger.info("[" + this.nccEntity.getName() + "]\t-  START: ");
        try
        {
            init();
            handleBeforeQuerys();
            prepareMmlToExecuteList();
            this.nccEntity.setCountedProblemBefore(getInconsistencyNumber());
            if (this.nccEntity.isMmlExecutable())
                handleMmlExecution();
            
            if (this.nccEntity.isCountAfterUpdate())
                this.nccEntity.setCountedProblemAfter(getInconsistencyNumber());
            else
                this.nccEntity.setCountedProblemAfter("--");
            handleAfterQuerys();
            logger.info("[" + this.nccEntity.getName() + "]\t-  END: ");
        }
        catch (SQLException | NBIAnsException e)
        {
            logger.throwing(this.nccEntity.getName(), null, e);
        }
        finally
        {
            close();
        }
    }

    private void init() throws SQLException
    {
        connection = DOA.getConnection();
        testStatement = connection.createStatement();
    }

    private void handleBeforeQuerys() throws SQLException
    {
        List<String> beforeQuerys = this.nccEntity.getBeforeQuerys();
        if (beforeQuerys != null)
        {
            logger.info("[" + this.nccEntity.getName() + "]\t-  BEFOREQUERY: start ");
            for (String query : beforeQuerys)
            {

                logger.log(Level.ALL, "[" + this.nccEntity.getName() + "]\t-  BEFOREQUERY: query:" + query);
                if (!query.isEmpty())
                {
                    boolean execute = testStatement.execute(query);
                    //OdpowiedzSQL sqlOdp = Baza.createAnswer(res);
                    logger.log(Level.ALL, "[" + this.nccEntity.getName() + "]\t-  BEFOREQUERY: succeded:" + execute);
                }
            }
            logger.info("[" + this.nccEntity.getName() + "]\t-  BEFOREQUERY: end, " + beforeQuerys.size() + " query's executed");
        }
        else
            logger.info("[" + this.nccEntity.getName() + "]\t-  BEFOREQUERY: empty");
    }

    private void prepareMmlToExecuteList() throws SQLException
    {
        logger.info("[" + this.nccEntity.getName() + "]\t-  MMLQUERYS: start");
        this.mmlToExecute = new java.util.LinkedList<MMLCommand>();

        List<MmlQueryEntity> mmlsQuery = this.nccEntity.getMmlQueryList();
        if (mmlsQuery != null)
        {
            for (MmlQueryEntity mmlq : mmlsQuery)
            {
                String sqlQuery = mmlq.getQuery();
                logger.log(Level.ALL, "[" + this.nccEntity.getName() + "]\t-  MMLQUERYS: query=" + sqlQuery);
                if (!sqlQuery.isEmpty())
                {
                    ResultSet res = testStatement.executeQuery(sqlQuery);
                    OdpowiedzSQL sqlOdp = Baza.createAnswer(res);
                    for (int r = 0; r < sqlOdp.rowCount(); r++)
                    {
                        MMLCommand mml = new ncc_automat.MMLCommand.MMLCommandBuilder()
                                .setCommand(sqlOdp.getValue("Command", r))
                                .setM2000(sqlOdp.getValue("M2000_Ip", r))
                                .setNeName(sqlOdp.getValue("NE_NAME", r))
                                .setSqlUpdateQuery(sqlOdp.getValue("UpdateQuery", r))
                                .setBreakAfterFirstError(mmlq.isBreakAfterFirstError())
                                .createMMLCommand();
                        this.mmlToExecute.add(mml);
                        logger.log(Level.ALL, "[" + this.nccEntity.getName() + "]\t-  MMLQUERYS: mml=" + mml.toString());
                    }
                }
            }
            logger.log(Level.INFO, "[" + this.nccEntity.getName() + "]\t-  MMLQUERYS: end, " + this.mmlToExecute.size() + " mml's prepared");
        }
        else
            logger.log(Level.INFO, "[" + this.nccEntity.getName() + "]\t-  MMLQUERYS: end, empty");

    }

    private String getInconsistencyNumber() throws SQLException
    {
        if (!this.nccEntity.isCountMML())
        {
            if (!this.nccEntity.getCountQuery().isEmpty())
            {
                return handleCountQuerys();
            }
            else
            {
                return "--";
            }
        }
        else
        {
            return "" + this.mmlToExecute.size();
        }
    }

    private String handleCountQuerys() throws SQLException
    {
        String countQuery = this.nccEntity.getCountQuery();
        String numbersOfProblem = null;
        if (!countQuery.isEmpty())
        {
            logger.info("[" + this.nccEntity.getName() + "]\t-  COUNTQUERY: start ");
            ResultSet res = testStatement.executeQuery(countQuery);
            logger.log(Level.ALL, "[" + this.nccEntity.getName() + "]\t-  COUNTQUERY: query:" + countQuery);
            OdpowiedzSQL sqlOdp = Baza.createAnswer(res);
            if (sqlOdp.rowCount() > 0)
            {
                if (sqlOdp.kolumnCount() > 1)
                    numbersOfProblem = sqlOdp.getValue("problems_founded", 0);
                else
                    numbersOfProblem = sqlOdp.getValue(0, 0);
            }
            logger.info("[" + this.nccEntity.getName() + "]\t-  COUNTQUERY: end, " + numbersOfProblem + " problems's founded");
        }
        else
            logger.info("[" + this.nccEntity.getName() + "]\t-  COUNTQUERY: end, empty");
        return numbersOfProblem;
    }

    private void handleMmlExecution() throws NBIAnsException
    {
        MmlExecutor mmlexec = new ncc_automat.MmlExecutor(mmlToExecute, testStatement, logger, this.northLogin, this.northPassword);
        mmlexec.setNccName(this.nccEntity.getName());
        mmlexec.execute();
        this.poprawnie = mmlexec.getExecOk();
        this.blednie = mmlexec.getProblem();
    }

    private void handleAfterQuerys() throws SQLException
    {
        List<String> afterQuerys = this.nccEntity.getAfterQuerys();
        if (afterQuerys != null)
        {
            logger.info("[" + this.nccEntity.getName() + "]\t-  AFTERQUERY: start ");
            for (String query : afterQuerys)
            {

                logger.log(Level.ALL, "[" + this.nccEntity.getName() + "]\t-  AFTERQUERY: query=" + query);
                if (!query.isEmpty())
                {
                    boolean res = testStatement.execute(query);

                    logger.log(Level.ALL, "[" + this.nccEntity.getName() + "]\t-  AFTERQUERY: succedde=" + res);
                }
            }
            logger.info("[" + this.nccEntity.getName() + "]\t-  AFTERQUERY: end," + afterQuerys.size() + " executed");
        }
        else
        {
            logger.log(Level.ALL, "[" + this.nccEntity.getName() + "]\t-  AFTERQUERY: end, empty");
        }

    }

    private void close() throws SQLException
    {
        testStatement.close();
        connection.close();
    }

    public NccEntity getNccEntity()
    {
        return nccEntity;
    }

    public void setNccEntity(NccEntity nccEntity)
    {
        this.nccEntity = nccEntity;
    }

    public List<MMLCommand> getMmlToExecute()
    {
        return mmlToExecute;
    }

    public void setMmlToExecute(List<MMLCommand> mmlToExecute)
    {
        this.mmlToExecute = mmlToExecute;
    }

    public String getPoprawnie()
    {
        if (poprawnie != -1)
            return "" + poprawnie;
        else
            return "--";
    }

    public String getBlednie()
    {
        if (blednie != -1)
            return "" + blednie;
        else
            return "--";
    }
}
