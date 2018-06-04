package com.qdesrame.openapi.diff.model;

import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by adarsh.sharma on 22/12/17.
 */
@Getter
@Setter
public class ChangedContent implements Changed {
    private final Content oldContent;
    private final Content newContent;
    private final DiffContext context;

    private Map<String, MediaType> increased;
    private Map<String, MediaType> missing;
    private Map<String, ChangedMediaType> changed;

    public ChangedContent(Content oldContent, Content newContent, DiffContext context) {
        this.oldContent = oldContent;
        this.newContent = newContent;
        this.context = context;
        this.increased = new HashMap<>();
        this.missing = new HashMap<>();
        this.changed = new HashMap<>();
    }

    @Override
    public DiffResult isChanged() {
        if (increased.isEmpty() && missing.isEmpty() && changed.isEmpty()) {
            return DiffResult.NO_CHANGES;
        }
        if (((context.isRequest() && missing.isEmpty()) || (context.isResponse() && increased.isEmpty()))
                && changed.values().stream().allMatch(Changed::isCompatible)) {
            return DiffResult.COMPATIBLE;
        }
        return DiffResult.INCOMPATIBLE;
    }

}
