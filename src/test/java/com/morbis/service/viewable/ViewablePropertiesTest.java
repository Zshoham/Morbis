package com.morbis.service.viewable;

import com.morbis.model.team.entity.Stadium;
import com.morbis.model.team.entity.Team;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static com.morbis.data.ViewableEntitySource.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ViewablePropertiesTest {

    @Before
    public void setUp() {
        initWithID();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void fromRecord() {
        ViewableProperties<Stadium> stadiumProperties = ViewableProperties.from(homeStadium);

        // values are stored correctly
        assertThat(stadiumProperties.get("id")).isPresent();
        assertThat(stadiumProperties.get("id").get())
                .isInstanceOf(Integer.class)
                .asInstanceOf(InstanceOfAssertFactories.INTEGER).isEqualTo(1);


        ViewableProperties<Team> teamProperties = ViewableProperties.from(home);

        // nested maps are stored correctly
        assertThat(teamProperties.isValue("owners")).isFalse();
        assertThat(teamProperties.get("owners")).isPresent();
        assertThat(teamProperties.get("owners").get()).isInstanceOf(List.class);

        assertThat(teamProperties.get("owners").get())
                .asInstanceOf(InstanceOfAssertFactories.LIST)
                .first().isInstanceOf(Map.class);

        List<Object> owners = (List<Object>) teamProperties.get("owners").get();
        Map<String, Object> firstOwner = (Map<String, Object>) owners.get(0);

        assertThat(firstOwner).containsEntry("id", homeOwner.getId());
        assertThat(firstOwner).containsEntry("username", homeOwner.getUsername());
        assertThat(firstOwner).containsEntry("name", homeOwner.getName());
    }

    @Test
    public void toRecord() {
        // simple object.
        ViewableProperties<Stadium> stadiumProperties = ViewableProperties.from(homeStadium);
        assertThat(stadiumProperties.asRecord()).isEqualTo(homeStadium);

        // complex object.
        ViewableProperties<Team> teamProperties = ViewableProperties.from(home);
        assertThat(teamProperties.asRecord()).isEqualTo(home);
    }
}