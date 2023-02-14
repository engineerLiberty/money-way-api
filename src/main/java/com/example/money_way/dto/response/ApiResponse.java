package com.example.money_way.dto.response;

<<<<<<< HEAD
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
=======
>>>>>>> 2131e92fd9a6f291128149f716a40bf453b455c9
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
<<<<<<< HEAD
@JsonIgnoreProperties(ignoreUnknown = true)
=======
>>>>>>> 2131e92fd9a6f291128149f716a40bf453b455c9
@NoArgsConstructor
public class ApiResponse<T> {

    private String status;
    private String message;
    private T data;
}
