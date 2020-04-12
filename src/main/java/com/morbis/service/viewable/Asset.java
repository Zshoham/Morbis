package com.morbis.service.viewable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class Asset<T> {

    private ViewableEntityType type;
    private int id;

    private ViewableProperties<T> properties;

    public Asset(ViewableEntityType type, int id) {
        this.type = type;
        this.id = id;
        properties = null;
    }

    public void putRecord(T record) {
        if (!ViewableEntityType.classValidatorFor(type).validate(record.getClass()))
            throw new IllegalArgumentException("cannot populate asset with type -  (" + record.getClass().getSimpleName() + ")");

        properties = ViewableProperties.from(record);
    }

    public T getAsset() {
        if (properties == null)
            throw new IllegalStateException("the asset was never populated with the corresponding record");

        return properties.asRecord();
    }
}
