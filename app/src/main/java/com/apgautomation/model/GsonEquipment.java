package com.apgautomation.model;

public class GsonEquipment {
    private int EquipmentTypeId;
    private String EquipmentType;


    // Getter Methods

    public float getEquipmentTypeId() {
        return EquipmentTypeId;
    }

    public String getEquipmentType() {
        return EquipmentType;
    }

    // Setter Methods

    public void setEquipmentTypeId(int EquipmentTypeId) {
        this.EquipmentTypeId = EquipmentTypeId;
    }

    public void setEquipmentType(String EquipmentType) {
        this.EquipmentType = EquipmentType;
    }
}