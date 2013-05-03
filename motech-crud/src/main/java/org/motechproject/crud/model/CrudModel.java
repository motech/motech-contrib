package org.motechproject.crud.model;

import lombok.Data;
import org.motechproject.crud.service.CrudActions;

import java.util.*;

@Data
public class CrudModel {
    /**
     * Fields you want to display in the listing section
     * @return
     */
    private List<String> displayFields = new ArrayList<>();

    /**
     * Fields you want to show as filters.
     * These fields should have respective "by_fieldName" views defined.
     * @return
     */
    private List<String> filterFields = new ArrayList<>();

    /**
     * Fields that should be hidden in the edit form
     * @return
     */
    List<String> hiddenFields = new ArrayList<>();

    /**
     * Field name of the ID
     * @return
     */
    String idFieldName;

    /**
     * Default values for field names
     * @return
     */
    Map<String, String> defaultValues = new HashMap<>();

    /**
     * Title for the listing page
     * @return
     */
    String displayName;

    /**
     * Crud Actions that should be allowed in the UI
     * @return
     */
    List<CrudActions> allowedActions = Arrays.asList(CrudActions.values());

    public void addDefaultValue(String key, String value) {
        defaultValues.put(key, value);
    }
}
