package ro.uti.ran.core.ws.internal.transmitere;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by Anastasia cea micuta on 1/30/2016.
 */
public class RanCapitolAssertUtil {
    @SuppressWarnings("unchecked")
    public static void assertEqualsUsingJsonSerialization(Object input1, Object input2) {
        ObjectMapper om = new ObjectMapper();
        ObjectWriter or = getObjectWriter(om);
        try {
            Map m1 = om.readValue(or.writeValueAsString(input1), Map.class);
            Map m2 = om.readValue(or.writeValueAsString(input2), Map.class);
            replaceEmptyStringToNullAndUpperCase((Map<String, Object>) m1);
            replaceEmptyStringToNullAndUpperCase((Map<String, Object>) m2);

            Assert.assertEquals(m1, m2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ObjectWriter getObjectWriter(ObjectMapper mapper) {
        mapper.setAnnotationIntrospector(new AnnotationIntrospector());
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        FilterProvider filters = new SimpleFilterProvider().addFilter("exceptFilter",
                SimpleBeanPropertyFilter.FilterExceptFilter.serializeAllExcept(
                        "denumire",
                        "denumireLegaturaRudenie",
                        "dataExport",
                        "indicativ",
                        "codXml"
                )
        );

//        String[] fieldsToIgnore = {"denumire"};
//        SimpleBeanPropertyFilter propertyFilter = SimpleBeanPropertyFilter.serializeAllExcept(fieldsToIgnore);
//        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("IgnoreFilter", propertyFilter);

        return mapper.writer(filters);
    }

    @SuppressWarnings("unchecked")
    private static void replaceEmptyStringToNullAndUpperCase(Map<String, Object> map) {
        for (String key : map.keySet()) {
            Object value = map.get(key);
            if (null != value) {
                if (value instanceof Map) {
                    replaceEmptyStringToNullAndUpperCase((Map<String, Object>) value);
                } else if (value.getClass().isArray()) {
                    Object[] array = (Object[]) value;
                    for (int i = 0; i < array.length; i++) {
                        if (null != array[i]) {
                            if (array[i] instanceof String) {
                                array[i] = makeEmptyStringToNullAndUpperCase((String) array[i]);
                            } else if (array[i] instanceof Map) {
                                replaceEmptyStringToNullAndUpperCase((Map<String, Object>) array[i]);
                            }
                        }
                    }
                } else if (value instanceof List) {
                    List list = (List) value;
                    if (!list.isEmpty()) {
                        if (list.get(0) instanceof Map && (
                                ((Map) list.get(0)).containsKey("codRand") ||
                                        ((Map) list.get(0)).containsKey("codIdentificare") ||
                                        ((Map) list.get(0)).containsKey("nrOfertaVanzare") ||
                                        ((Map) list.get(0)).containsKey("codIdentificare") ||
                                        ((Map) list.get(0)).containsKey("codTip") ||
                                        ((Map) list.get(0)).containsKey("nr") ||
                                        ((Map) list.get(0)).containsKey("nrCrt")

                        )) {
                            Collections.sort(list, new Comparator<Map>() {

                                @Override
                                public int compare(Map o1, Map o2) {
                                    if (o1.containsKey("codRand")) {
                                        return MapUtils.getInteger(o1, "codRand").compareTo(MapUtils.getInteger(o2, "codRand"));
                                    } else if (o1.containsKey("codRand")) {
                                        return MapUtils.getInteger(o1, "codRand").compareTo(MapUtils.getInteger(o2, "codRand"));
                                    } else if (o1.containsKey("nrOfertaVanzare")) {
                                        return MapUtils.getString(o1, "nrOfertaVanzare").compareToIgnoreCase(MapUtils.getString(o2, "nrOfertaVanzare"));
                                    } else if (o1.containsKey("codIdentificare")) {
                                        return MapUtils.getInteger(o1, "codIdentificare").compareTo(MapUtils.getInteger(o2, "codIdentificare"));
                                    } else if (o1.containsKey("codTip")) {
                                        return MapUtils.getInteger(o1, "codTip").compareTo(MapUtils.getInteger(o2, "codTip"));
                                    } else if (o1.containsKey("nr")) {
                                        return MapUtils.getInteger(o1, "nr").compareTo(MapUtils.getInteger(o2, "nr"));
                                    } else if (o1.containsKey("nrCrt")) {
                                        return MapUtils.getInteger(o1, "nrCrt").compareTo(MapUtils.getInteger(o2, "nrCrt"));
                                    } else {
                                        throw new UnsupportedOperationException("compare not supported!");
                                    }
                                }
                            });
                        }
                    }

                    for (int i = 0; i < list.size(); i++) {
                        if (null != list.get(i)) {
                            if (list.get(i) instanceof String) {
                                list.set(i, makeEmptyStringToNullAndUpperCase((String) list.get(i)));
                            } else if (list.get(i) instanceof Map) {
                                replaceEmptyStringToNullAndUpperCase((Map<String, Object>) list.get(i));
                            }
                        }
                    }
                } else if (value instanceof String) {
                    map.put(key, makeEmptyStringToNullAndUpperCase((String) value));
                }
            }
        }
    }

    private static String makeEmptyStringToNullAndUpperCase(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        } else {
            return value.toLowerCase();
        }
    }

    private static class AnnotationIntrospector extends JacksonAnnotationIntrospector {
        @Override
        public Object findFilterId(Annotated a) {
            return "exceptFilter";
        }
    }
}
