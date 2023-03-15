package io.brandy.realworld.expection;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonTypeName("errors")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class ErrorResponse {
    ErrorResponse(String error) {
        this.errors = new ArrayList<String>();
        this.errors.add(error);
    }
    @JsonProperty("body")
    private List<String> errors;

}
