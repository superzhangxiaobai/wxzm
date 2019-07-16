package com.wf.ew.common.exception;

public class CustomException extends IException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CustomException() {
        super();
    }

    public CustomException(String message) {
        super(message);
    }

    public CustomException(Integer code, String message) {
        super(code, message);
    }

    @Override
    public Integer getCode() {
        Integer code = super.getCode();
        if (code == null) {
            code = 500;
        }
        return code;
    }

    @Override
    public String getMessage() {
        String message = super.getMessage();
        if (message == null) {
            message = "系统繁忙";
        }
        return message;
    }

}
