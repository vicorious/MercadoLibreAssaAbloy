package com.example.springboot.dto.response.orders;

public class AlternativePhone
{
    private String number;
    private String extension;
    private String area_code;

    public AlternativePhone(String number, String extension, String area_code) {
        this.number = number;
        this.extension = extension;
        this.area_code = area_code;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getArea_code() {
        return area_code;
    }

    public void setArea_code(String area_code) {
        this.area_code = area_code;
    }
}
