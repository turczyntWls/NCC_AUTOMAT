package ncc_automat;

public class MMLCommand 
{
    private final String m2000;
    private final String neName;
    private final String command;
    private final String sqlUpdateQuery;

    private String regNeAns;
    private String executeCommAns;
    private boolean northSucced;
    private boolean sqlUpdated;
    private boolean breakAfterFirstError;
    private String sqlExceptionInfo;
    private String retcodeLine;
    static String SUCCEED_RETCODE_PATTERN = "RETCODE = 0";

    public MMLCommand(MMLCommandBuilder builder) 
    {
        this.m2000 = builder.m2000;
        this.neName = builder.neName;
        this.command = builder.command;
        this.sqlUpdateQuery = builder.sqlUpdateQuery;
        this.breakAfterFirstError=builder.breakAfterFirstError;
        regNeAns = null;
        executeCommAns = null;
        northSucced = false;
        sqlUpdated = false;
    }

    public String getM2000()
    {
        return m2000;
    }

    public String getNeName() 
    {
        return neName;
    }

    public String getCommand() 
    {
        return command;
    }

    public String getSqlUpdateQuery() 
    {
        return sqlUpdateQuery;
    }

    public String getRegNeAns() 
    {
        return regNeAns;
    }

    public void setRegNeAns(String regNeAns) 
    {
        this.regNeAns = regNeAns;
    }

    public String getExecuteCommAns() 
    {
        return executeCommAns;
    }

    public void setExecuteCommAns(String executeCommAns) 
    {
        if(this.executeCommAns!=null)
            this.executeCommAns=this.executeCommAns+executeCommAns;
        else
            this.executeCommAns = executeCommAns;
    }

    public boolean isNorthSucced() 
    {
        return northSucced;
    }

    public void setNorthSucced(boolean northSucced) 
    {
        this.northSucced = northSucced;
    }

    public boolean isSqlUpdated() 
    {
        return sqlUpdated;
    }

    public void setSqlUpdated(boolean sqlUpdated) 
    {
        this.sqlUpdated = sqlUpdated;
    }

    public String getSqlExceptionInfo() 
    {
        return sqlExceptionInfo;
    }

    public void setSqlExceptionInfo(String e) 
    {
        this.sqlExceptionInfo = e;
    }

    @Override
    public String toString() 
    {
        return "MMLCommand{[northSucced=" + this.northSucced + ",sqlUpdated=" + this.sqlUpdated + "] " + "m2000=" + m2000 + ", neName=" + neName + ", command=" + command+ ", retcodeLine=" + retcodeLine + ", sqlUpdateQuery=" + sqlUpdateQuery + ", executeCommAns=" + executeCommAns + " sqlExceptionInfo=" + sqlExceptionInfo  + '}';
    }
    

    public String getRetcodeLine() 
    {
        return retcodeLine;
    }

    public void setRetcodeLine(String retcodeLine) 
    {
        if(this.retcodeLine!=null)
            this.retcodeLine = this.retcodeLine+retcodeLine;
        else
            this.retcodeLine = retcodeLine;
    }

    public boolean isBreakAfterFirstError() {
        return breakAfterFirstError;
    }

    public void setBreakAfterFirstError(boolean breakAfterFirstError) {
        this.breakAfterFirstError = breakAfterFirstError;
    }

    public static class MMLCommandBuilder 
    {
        private String m2000;
        private String neName;
        private String command;
        private String sqlUpdateQuery;
        private boolean breakAfterFirstError;

        public MMLCommandBuilder() 
        {
        }

        public MMLCommandBuilder setM2000(String m2000) 
        {
            this.m2000 = m2000;
            return this;
        }

        public MMLCommandBuilder setNeName(String neName) 
        {
            this.neName = neName;
            return this;
        }

        public MMLCommandBuilder setCommand(String command) 
        {
            this.command = command;
            return this;
        }

        public MMLCommandBuilder setSqlUpdateQuery(String sqlUpdateQuery) 
        {
            this.sqlUpdateQuery = sqlUpdateQuery;
            return this;
        }
        
        public MMLCommandBuilder setBreakAfterFirstError(boolean breakAfterFirstError )
        {
            this.breakAfterFirstError=breakAfterFirstError;
            return this;
        }

        public MMLCommand createMMLCommand() 
        {
            return new MMLCommand(this);
        }
    }
}
