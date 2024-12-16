package com.tekup.gatherwise.web.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventTypeForm {
    @NotBlank
    @Size(min=4, max=20)
    private String typeName;
    @Size(max=255)
    private String description;
    private String icon;
}