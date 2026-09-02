package com.dhiraj.weatherforecast.location;

import com.dhiraj.weatherapicommon.Location;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LocationApiController.class)
class LocationApiControllerTest {

    private static final String END_POINT_PATH = "/v1/locations";

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockitoBean
    LocationService locationService;

    @Test
    public void testShouldReturn400BadRequest() throws Exception {

        Location location = new Location();
        String content = objectMapper.writeValueAsString(location);

        mockMvc.perform(post(END_POINT_PATH)
                        .contentType("application/json")
                        .content(content))
                .andExpect(status().isBadRequest())
                .andDo(print());

    }

    @Test
    public void testShouldReturn201Created() throws Exception {
        Location location = buildLocation("NYC_USA");

        Mockito.when(locationService.save(location)).thenReturn(location);

        String content = objectMapper.writeValueAsString(location);

        mockMvc.perform(post(END_POINT_PATH)
                        .contentType("application/json")
                        .content(content))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("NYC_USA"))
                .andExpect(header().string("Location", "/v1/locations/NYC_USA"))
                .andDo(print());
    }

    @Test
    public void testListShouldReturn204noContent() throws Exception {

        Mockito.when(locationService.list()).thenReturn(Collections.emptyList());

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isNoContent())
                .andDo(print());

    }

    @Test
    public void testListShouldReturn200OK() throws Exception {
        Location location = buildLocation("NYC_USA");
        Location location2 = buildLocation("LACA_USA");
        Mockito.when(locationService.list()).thenReturn(List.of(location, location2));

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    public void testGetShouldReturn405MethodNotAllowed() throws Exception {
        mockMvc.perform(post(buildUrl("ABCDE")))
                .andExpect(status().isMethodNotAllowed())
                .andDo(print());

    }

    @Test
    public void testGetShouldReturn200OK() throws Exception {
        Location location = buildLocation("NYC_USA");
        Mockito.when(locationService.get(location.getCode())).thenReturn(location);

        mockMvc.perform(get(buildUrl(location.getCode())))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    public void testGetShouldReturn400NotFound() throws Exception {
        mockMvc.perform(get(buildUrl("ABCDE")))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn200OK() throws Exception {
        Location location = buildLocation("NYC_USA");
        String bodyContent = objectMapper.writeValueAsString(location);

        Mockito.when(locationService.update(location)).thenReturn(location);

        mockMvc.perform(put(END_POINT_PATH).contentType("application/json")
                        .content(bodyContent))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.code").value("NYC_USA"))
                .andExpect(jsonPath("$.city_name").value("New York City"))
                .andDo(print());


    }

    @Test
    public void testValidateRequestBodyLocationCodeNotNull() throws Exception {
        Location location = buildLocation("NYC_USA");
        location.setCode(null);
        String bodyContent = objectMapper.writeValueAsString(location);

        mockMvc.perform(put(END_POINT_PATH).contentType("application/json")
                        .content(bodyContent))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.errors[0]").value("Location code cannot be null"))
                .andDo(print());


    }

    @Test
    public void testValidateRequestBodyLocationCodeLength() throws Exception {
        Location location = buildLocation("");
        String bodyContent = objectMapper.writeValueAsString(location);

        mockMvc.perform(put(END_POINT_PATH).contentType("application/json")
                        .content(bodyContent))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.errors[0]").value("Location code must have 3-12 characters"))
                .andDo(print());


    }

    @Test
    public void testValidateRequestBodyAllFieldsInvalid() throws Exception {
        Location location = new Location();
        location.setCityName("");
        String bodyContent = objectMapper.writeValueAsString(location);

        mockMvc.perform(put(END_POINT_PATH).contentType("application/json")
                        .content(bodyContent))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
//                .andExpect(jsonPath("$.errors[0]").value("Location code must have 3-12 characters"))
                .andDo(print());


    }

    @Test
    public void testUpdateShouldReturn404NotFound() throws Exception {
        Location location = buildLocation("ABCD");

        String content = objectMapper.writeValueAsString(location);

        Mockito.when(locationService.update(location)).thenThrow(new LocationNotFoundException("No location found"));
        mockMvc.perform(put(END_POINT_PATH).contentType("application/json")
                        .content(content))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn400BadRequest() throws Exception {
        Location location = buildLocation("ABCD");
        location.setCode(null);
        String content = objectMapper.writeValueAsString(location);

        Mockito.when(locationService.update(location)).thenReturn(location);
        mockMvc.perform(put(END_POINT_PATH).contentType("application/json")
                        .content(content))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testDeleteShouldReturn404NotFound() throws Exception {
        String code = "ABCD";
        Mockito.doThrow(LocationNotFoundException.class).when(locationService).delete(code);
        mockMvc.perform(delete(buildUrl(code)).contentType("application/json"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testDeleteShouldReturn204NoContent() throws Exception {
        Location location = buildLocation("NYC_USA");
        Mockito.doNothing().when(locationService).delete(location.getCode());
        mockMvc.perform(delete(buildUrl(location.getCode())))
                .andExpect(status().isNoContent())
                .andDo(print());
    }


    public Location buildLocation(String code) {
        Location location = new Location();
        location.setCode(code);
        location.setCityName("New York City");
        location.setCountryName("USA");
        location.setCountryCode("US");
        location.setRegionName("New York");
        location.setEnabled(true);
        return location;
    }

    public String buildUrl(String code) {
        return END_POINT_PATH + "/" + code;
    }

}