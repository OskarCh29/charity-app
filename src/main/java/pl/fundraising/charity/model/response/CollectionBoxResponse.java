package pl.fundraising.charity.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CollectionBoxResponse {
    private Long boxId;

    private boolean isAssigned;

    private boolean isEmpty;

}
