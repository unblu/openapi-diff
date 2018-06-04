package com.qdesrame.openapi.diff.model;

import com.qdesrame.openapi.diff.utils.ChangedUtils;
import io.swagger.v3.oas.models.responses.ApiResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangedResponse implements Changed {
    private final ApiResponse oldApiResponse;
    private final ApiResponse newApiResponse;
    private final DiffContext context;

    private boolean changeDescription;
    private ChangedHeaders changedHeaders;
    private ChangedContent changedContent;

    public ChangedResponse(ApiResponse oldApiResponse, ApiResponse newApiResponse, DiffContext context) {
        this.oldApiResponse = oldApiResponse;
        this.newApiResponse = newApiResponse;
        this.context = context;
    }

    @Override
    public DiffResult isChanged() {
        if (!changeDescription && ChangedUtils.isUnchanged(changedContent) && ChangedUtils.isUnchanged(changedHeaders)) {
            return DiffResult.NO_CHANGES;
        }
        if (ChangedUtils.isCompatible(changedContent) && ChangedUtils.isCompatible(changedHeaders)) {
            return DiffResult.COMPATIBLE;
        }
        return DiffResult.INCOMPATIBLE;
    }
}
