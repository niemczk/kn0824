package com.kn0824.core.tools;

import java.util.Objects;

public class Tool {
    private String toolCode;
    private ToolType toolType;
    private String brand;

    public Tool(String toolCode, ToolType toolType, String brand) {
        this.toolCode = toolCode;
        this.toolType = toolType;
        this.brand = brand;
    }

    public String getToolCode() {
        return toolCode;
    }

    public void setToolCode(String toolCode) {
        this.toolCode = toolCode;
    }

    public ToolType getToolType() {
        return toolType;
    }

    public void setToolType(ToolType toolType) {
        this.toolType = toolType;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tool tool = (Tool) o;
        return Objects.equals(toolCode, tool.toolCode) && Objects.equals(toolType, tool.toolType) && Objects.equals(brand, tool.brand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(toolCode, toolType, brand);
    }
}
