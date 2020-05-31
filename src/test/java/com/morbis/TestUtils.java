package com.morbis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;

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
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper.writeValueAsString(obj);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static MvcResult makePostRequest(String url, MockMvc mvc, MediaType mediaType, Object body, Object... pathVariables) {
        RequestBuilder request = MockMvcRequestBuilders.post(url, pathVariables)
                .header("Authorization", "testToken")
                .contentType(mediaType)
                .content(asJsonString(body));
        var res = new Object() { MvcResult response; };
        Throwable possibleException = catchThrowable(() -> res.response = mvc.perform(request).andReturn());
        assertThat(possibleException).doesNotThrowAnyException();

        return res.response;
    }

    public static MvcResult makePostRequest(String url, MockMvc mvc, MediaType mediaType, Object body, List<Pair<String,String>> queryParams, Object... pathVariables) {
        LinkedMultiValueMap<String,String> queryParamsMap = new LinkedMultiValueMap<>();
        queryParams.forEach(paramPair -> queryParamsMap.add(paramPair.getFirst(), paramPair.getSecond()));
        RequestBuilder request = MockMvcRequestBuilders.post(url, pathVariables)
                .header("Authorization", "testToken")
                .queryParams(queryParamsMap)
                .contentType(mediaType)
                .content(asJsonString(body));
        var res = new Object() { MvcResult response; };
        Throwable possibleException = catchThrowable(() -> res.response = mvc.perform(request).andReturn());
        assertThat(possibleException).doesNotThrowAnyException();

        return res.response;
    }

    public static MvcResult makePostRequest(String url, MockMvc mvc, MediaType mediaType, List<Pair<String,String>> queryParams, Object... pathVariables) {
        LinkedMultiValueMap<String,String> queryParamsMap = new LinkedMultiValueMap<>();
        queryParams.forEach(paramPair -> queryParamsMap.add(paramPair.getFirst(), paramPair.getSecond()));
        RequestBuilder request = MockMvcRequestBuilders.post(url, pathVariables)
                .header("Authorization", "testToken")
                .queryParams(queryParamsMap)
                .contentType(mediaType);
        var res = new Object() { MvcResult response; };
        Throwable possibleException = catchThrowable(() -> res.response = mvc.perform(request).andReturn());
        assertThat(possibleException).doesNotThrowAnyException();

        return res.response;
    }

    public static MvcResult makeGetRequest(String url, MockMvc mvc, Object... pathVariables) {
        RequestBuilder request = MockMvcRequestBuilders.get(url, pathVariables)
                .header("Authorization", "testToken");
        var res = new Object() { MvcResult response; };
        Throwable possibleException = catchThrowable(() -> res.response = mvc.perform(request).andReturn());
        assertThat(possibleException).doesNotThrowAnyException();

        return res.response;
    }

    public static MvcResult makeGetRequest(String url, MockMvc mvc, List<Pair<String,String>> queryParams, Object... pathVariables) {
        LinkedMultiValueMap<String,String> queryParamsMap = new LinkedMultiValueMap<>();
        queryParams.forEach(paramPair -> queryParamsMap.add(paramPair.getFirst(), paramPair.getSecond()));
        RequestBuilder request = MockMvcRequestBuilders.get(url, pathVariables)
                .header("Authorization", "testToken")
                .queryParams(queryParamsMap);
        var res = new Object() { MvcResult response; };
        Throwable possibleException = catchThrowable(() -> res.response = mvc.perform(request).andReturn());
        assertThat(possibleException).doesNotThrowAnyException();

        return res.response;
    }



}
