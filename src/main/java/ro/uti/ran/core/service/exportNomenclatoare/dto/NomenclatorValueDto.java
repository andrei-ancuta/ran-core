package ro.uti.ran.core.service.exportNomenclatoare.dto;

import java.nio.charset.Charset;
import java.util.Date;

/**
 * Created by Stanciu Neculai on 16.Nov.2015.
 */
public class NomenclatorValueDto {

    private Object code;
    private String valoare;
    private Date validFrom;
    private Date validTo;
    private Object parentCode;

    public static NomenclatorValueDto createNomValueDtoFrom(Object code, String value) {
        NomenclatorValueDto valueDto = new NomenclatorValueDto();
        valueDto.setCode(code);
        valueDto.setValoare(value);
        return valueDto;
    }

    public Object getCode() {
        return code;
    }

    public void setCode(Object code) {
        this.code = code;
    }

    public String getValoare() {
        return valoare;
    }

    public void setValoare(String valoare) {
        this.valoare = valoare;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }

    public Object getParentCode() {
        return parentCode;
    }

    public void setParentCode(Object parentCode) {
        this.parentCode = parentCode;
    }

    public NomenclatorValueDto withValidFrom(Date validFrom) {
        this.validFrom = validFrom;
        return this;
    }

    public NomenclatorValueDto withValidTo(Date validTo) {
        this.validTo = validTo;
        return this;
    }

    public NomenclatorValueDto withParentCode(String parentCode) {
        this.parentCode = parentCode;
        return this;
    }

    @Override
    public String toString() {
        return "NomenclatorValueDto{" +
                "code='" + code + '\'' +
                ", valoare='" + new String(valoare.getBytes(), Charset.forName("UTF-8")) + '\'' +
                ", validFrom='" + validFrom + '\'' +
                ", validTo='" + validTo + '\'' +
                ", parentCode='" + parentCode + '\'' +
                '}';
    }
}
