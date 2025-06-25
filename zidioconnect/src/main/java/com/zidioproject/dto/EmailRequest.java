package com.zidioproject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailRequest {
    public String to;
    public String subject;
    public String message;

}
