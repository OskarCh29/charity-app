package pl.fundraising.charity.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AssignRequest {

    @NotBlank(message = "Event ID which you want to assign box is not valid ")
    private long eventId;
}
