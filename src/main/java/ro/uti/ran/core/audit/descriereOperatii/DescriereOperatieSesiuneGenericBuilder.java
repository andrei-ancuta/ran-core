package ro.uti.ran.core.audit.descriereOperatii;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ro.uti.ran.core.audit.Audit;
import ro.uti.ran.core.audit.DescriereOperatieSesiune;
import ro.uti.ran.core.model.registru.Nomenclator;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter.serializeAllExcept;

/**
 * Created by Stanciu Neculai on 07.Jan.2016.
 */
@Component
public class DescriereOperatieSesiuneGenericBuilder implements DescriereOperatieSesiune {
    @Autowired
    protected Environment env;

    @Override
    public String getDescriereOpSesiuneFrom(Audit auditMeta, Object[] args, Throwable t) {
        return "exceptie: " + t.getMessage();
    }

    @Override
    public String getDescriereOpSesiuneFrom(Audit auditMeta, Object[] args, Object result) {
        StringBuilder sb = new StringBuilder(300);
        sb.append("Operatie: ").
                append(auditMeta.opType().name()).
                append(" executata cu parametrii de intrare: ").
                append(toJson(args)).
                append(" rezultat intors: ").
                append(toJson(result));
        String descriere = sb.toString();

        return descriere.length() > 1999 ? descriere.substring(0, 1999) : descriere;
    }

    @Override
    public  String getNomenclatorDescriereOpSesiuneFrom(Audit auditMeta, Object[] args, Object result)  {
        StringBuilder sb = new StringBuilder(300);
        sb.append("Operatie: ").
                append(auditMeta.opType().name()).
                append(" executata cu parametrii de intrare: ").
                append(toJson(reflectNom(args[0])));
        
        if(result != null) {
        	sb.append(" rezultat intors: ").
            append(toJson(reflectNom(result)));
        }
               

        String descriere = sb.toString();
        return descriere.length() > 1999 ? descriere.substring(0, 1999) : descriere;
    }

    private Map<String,Object> reflectNom(Object args)  {
        Map<String, Object> data = new HashMap<String, Object>();
        Method[] objMeth=args.getClass().getDeclaredMethods();
        try {


            for (Field field : args.getClass().getDeclaredFields()) {
                //Type fieldType = field.getType();
                field.setAccessible(true);
                if (field.getType().getSuperclass()==Nomenclator.class && !field.getType().isArray()) {
                    if(field.get(args)!=null) {
                        Map<String, Object> dataFk = new HashMap<String, Object>();
                        try {
                            Method idMeth = field.get(args).getClass().getMethod("getId");
                            dataFk.put("id", idMeth.invoke(field.get(args)));
                            dataFk.put("clasaEntitate", field.getType().getName().substring(field.getType().getName().lastIndexOf(".") + 1));
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        data.put(field.getName(), dataFk);
                    }
                    else{
                        data.put(field.getName(), null);
                    }
                } else if (!field.getType().isArray() && !Collection.class.isAssignableFrom(field.getType())) {
                    boolean isClassMethod = false;
                    String capitaliseField = field.getName();
                    String getter ="get"+Character.toUpperCase(capitaliseField.charAt(0)) + capitaliseField.substring(1);
                    for (Method metod: objMeth){
                        if(metod.getName().equals(getter)){
                            isClassMethod=true;
                            break;
                        }
                    }
                    if(isClassMethod) {
                        data.put(field.getName(), field.get(args));
                    }
                }
            }

            return data;
        }
        catch(IllegalAccessException ex){
            data.put("Exceptie",ex.getMessage());
            return data;
        }
    }

    private String toJson(Object obj) {
        ObjectWriter objectWriter = getObjectWriter();
        try {
            return objectWriter.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            if (obj.getClass().isArray()) {
                return new JSONArray(Arrays.asList((Object[]) obj)).toString();
            } else {
                return new JSONObject(obj).toString();
            }
        }
    }

    private ObjectWriter getObjectWriter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FilterProvider filters = new SimpleFilterProvider().addFilter("auditFilter",
                serializeAllExcept("continutFisier", "newPassword", "password")
        );
        return mapper.writer(filters);
    }

    @Override
    public String getDescriereCompletaOpSesiuneFrom(Audit auditMeta, Object[] args, Throwable t) {
        return "-";
    }

    @Override
    public String getDescriereCompletaOpSesiuneFrom(Audit auditMeta, Object[] args, Object result) {
        return MessageFormat.format(env.getProperty(auditMeta.opType().getCodDescriereCompletaProperties()), args);
    }
}
