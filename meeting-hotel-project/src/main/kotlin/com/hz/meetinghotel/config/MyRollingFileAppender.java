package com.hz.meetinghotel.config;

import ch.qos.logback.core.rolling.RollingFileAppender;


public class MyRollingFileAppender extends RollingFileAppender {




    @Override
    public String getFile() {
        String file = super.getFile();
        if (file.contains("applicationName_IS_UNDEFINED")){
            file = file.replace("applicationName_IS_UNDEFINED",this.getClass().getName().split("\\.")[2]);
        }
        return file;
    }


}
