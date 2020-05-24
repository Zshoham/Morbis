package com.morbis.service.viewable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.morbis.service.viewable.ViewableEntityType.VIEWABLE_VALIDATOR;

@Getter
@EqualsAndHashCode
public class ViewableProperties<T> {

    private transient ObjectMapper mapper;
    private Map<String, ?> properties;
    private final Class<T> type;


    private ViewableProperties(Class<T> type) {
        properties = new HashMap<>();
        this.type = type;
    }

    public static ViewableProperties<?> EMPTY = new ViewableProperties<>(Void.TYPE);


    @SuppressWarnings("unchecked")
    public static <TYPE> ViewableProperties<TYPE> from(TYPE record) {
        if (!VIEWABLE_VALIDATOR.validate(record.getClass()))
            throw new IllegalArgumentException("cannot create properties of non viewable type (" + record.getClass().getSimpleName() + ")");

        ViewableProperties<TYPE> res = new ViewableProperties<>((Class<TYPE>) record.getClass());
        res.mapper = new ObjectMapper();
        res.mapper.registerModule(new JavaTimeModule());
        res.properties =  res.mapper.convertValue(record, Map.class);
        return res;
    }

    public T asRecord() {
        return mapper.convertValue(properties, type);
    }

    public boolean isValue(String key) {
        return properties.get(key) instanceof String;
    }

    public Optional<?> get(String key) {
        return Optional.ofNullable(properties.get(key));
    }
}
