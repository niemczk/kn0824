package com.kn0824.service;

import com.kn0824.core.agreement.RentalAgreement;
import com.kn0824.core.tools.Tool;
import com.kn0824.core.tools.ToolType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class RentalAgreementService {

    // Note: Normally I would not implement in this manner. The tools and the tool types would be better served being
    // stored in a database so that they could be added, modified, and removed without requiring code updates.
    // For the sake of the scope of this demonstration, we are just going to generate the data internally.
    private Map<String, Tool> toolMapping;

    public RentalAgreementService() {
        initialize();
    }

    public RentalAgreement createRentalAgreement(String toolCode, LocalDate checkoutDate, int rentalDays,
                                                 int discountAmount) {
        Tool toolBeingRequested = toolMapping.get(toolCode);

        return null;
    }

    private void initialize() {
        Map<String, ToolType> types = createToolTypes();
        this.toolMapping = new HashMap<>();
        this.toolMapping.put("CHNS", new Tool("CHNS", types.get("Chainsaw"), "Stihl"));
        this.toolMapping.put("LADW", new Tool("LADW", types.get("Ladder"), "Werner"));
        this.toolMapping.put("JAKD", new Tool("JAKD", types.get("Jackhammer"), "DeWalt"));
        this.toolMapping.put("JAKR", new Tool("JAKR", types.get("Jackhammer"), "Ridgid"));
    }

    private Map<String, ToolType> createToolTypes() {
        Map<String, ToolType> types = new HashMap<>();
        types.put("Ladder", new ToolType("Ladder", new BigDecimal("1.99"), true, true, false));
        types.put("Chainsaw", new ToolType("Chainsaw", new BigDecimal("1.49"), true, false, true));
        types.put("Jackhammer", new ToolType("JackHammer", new BigDecimal("2.99"), true, false, false));
        return types;
    }
}
