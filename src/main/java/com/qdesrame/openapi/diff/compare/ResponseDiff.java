package com.qdesrame.openapi.diff.compare;

import com.qdesrame.openapi.diff.model.ChangedResponse;
import com.qdesrame.openapi.diff.model.DiffContext;
import com.qdesrame.openapi.diff.utils.RefPointer;
import com.qdesrame.openapi.diff.utils.RefType;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.responses.ApiResponse;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;

import static com.qdesrame.openapi.diff.utils.ChangedUtils.isChanged;

/**
 * Created by adarsh.sharma on 28/12/17.
 */
public class ResponseDiff extends ReferenceDiffCache<ApiResponse, ChangedResponse> {
    private OpenApiDiff openApiDiff;
    private Components leftComponents;
    private Components rightComponents;
    private static RefPointer<ApiResponse> refPointer = new RefPointer<>(RefType.RESPONSES);

    public ResponseDiff(OpenApiDiff openApiDiff) {
        this.openApiDiff = openApiDiff;
        this.leftComponents = openApiDiff.getOldSpecOpenApi() != null ? openApiDiff.getOldSpecOpenApi().getComponents() : null;
        this.rightComponents = openApiDiff.getNewSpecOpenApi() != null ? openApiDiff.getNewSpecOpenApi().getComponents() : null;
    }

    public Optional<ChangedResponse> diff(ApiResponse left, ApiResponse right, DiffContext context) {
        return cachedDiff(new HashSet<>(), left, right, left.get$ref(), right.get$ref(), context);
    }

    @Override
    protected Optional<ChangedResponse> computeDiff(HashSet<String> refSet, ApiResponse left, ApiResponse right, DiffContext context) {
        left = refPointer.resolveRef(leftComponents, left, left.get$ref());
        right = refPointer.resolveRef(rightComponents, right, right.get$ref());

        ChangedResponse changedResponse = new ChangedResponse(left, right, context);

        openApiDiff.getContentDiff().diff(left.getContent(), right.getContent(), context).ifPresent(changedResponse::setChangedContent);
        openApiDiff.getHeadersDiff().diff(left.getHeaders(), right.getHeaders(), context).ifPresent(changedResponse::setChangedHeaders);
        changedResponse.setChangeDescription(!Objects.equals(left.getDescription(), right.getDescription()));

        return isChanged(changedResponse);
    }
}
