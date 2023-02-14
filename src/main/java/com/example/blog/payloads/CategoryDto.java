package com.example.blog.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class CategoryDto {

    private int categoryId;
    @NotEmpty
    @Size(min = 3, max = 20, message = "Please title should min 3 and max 20 character")
    private String categoryTitle;

    @NotEmpty
    private String categoryDescription;

}
