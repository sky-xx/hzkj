package com.hz.meetinghotel.config;

import com.hz.general.utils.ResultVo;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PageResultVo<T> extends ResultVo {
    public Long totalElements;

    public PageResultVo() {
    }

    public PageResultVo(String code, String msg, T data) {
        super(code, msg, data);
    }

    public PageResultVo(String code, String msg, T data, Long totalElements) {
        super(code, msg, data);
        this.totalElements = totalElements;
    }
}
