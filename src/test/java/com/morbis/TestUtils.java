package com.morbis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public abstract class TestUtils {

    @SafeVarargs
    public static <T> List<T> listOf(T... items) {
        return Stream.of(items).collect(Collectors.toList());
    }

    public static String asJsonString(final Object obj) {
        try { return new ObjectMapper().writeValueAsString(obj); }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static MvcResult makePostRequest(String url, MockMvc mvc, MediaType mediaType, Object body) {
        RequestBuilder request = MockMvcRequestBuilders.post(url)
                .contentType(mediaType)
                .content(asJsonString(body));
        var res = new Object() { MvcResult response; };
        Throwable possibleException = catchThrowable(() -> res.response = mvc.perform(request).andReturn());
        assertThat(possibleException).doesNotThrowAnyException();

        return res.response;
    }

    public static MvcResult makeGetRequest(String url, MockMvc mvc, Object... reqParameters) {
        RequestBuilder request = MockMvcRequestBuilders.get(url, reqParameters);
        var res = new Object() { MvcResult response; };
        Throwable possibleException = catchThrowable(() -> res.response = mvc.perform(request).andReturn());
        assertThat(possibleException).doesNotThrowAnyException();

        return res.response;
    }

}
