package com.crm.springSecurity.model;

import jakarta.persistence.*;

@Entity
@Table(name = "inspections")
public class Inspection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "inspetor_id")
    private Long inspetorId;

    private String status;
    private String worder;
    private String inspector;
    private String client;
    private String name;
    private String address1;
    private String address2;
    private String city;
    private String zip;
    private String otype;
    private String duedate;
    private String rush;
    private String followup;
    private String vacant;
    private String mortgage;
    private String vandalism;

    @Column(name = "freeze_flag")
    private String freezeFlag;

    private String storm;
    private String roof;
    private String water;

    @Column(name = "natural_flag")
    private String naturalFlag;

    private String fire;
    private String hazard;
    private String structure;
    private String mold;
    private String pump;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInspetorId() {
        return inspetorId;
    }

    public void setInspetorId(Long inspetorId) {
        this.inspetorId = inspetorId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWorder() {
        return worder;
    }

    public void setWorder(String worder) {
        this.worder = worder;
    }

    public String getInspector() {
        return inspector;
    }

    public void setInspector(String inspector) {
        this.inspector = inspector;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getOtype() {
        return otype;
    }

    public void setOtype(String otype) {
        this.otype = otype;
    }

    public String getDuedate() {
        return duedate;
    }

    public void setDuedate(String duedate) {
        this.duedate = duedate;
    }

    public String getRush() {
        return rush;
    }

    public void setRush(String rush) {
        this.rush = rush;
    }

    public String getFollowup() {
        return followup;
    }

    public void setFollowup(String followup) {
        this.followup = followup;
    }

    public String getVacant() {
        return vacant;
    }

    public void setVacant(String vacant) {
        this.vacant = vacant;
    }

    public String getMortgage() {
        return mortgage;
    }

    public void setMortgage(String mortgage) {
        this.mortgage = mortgage;
    }

    public String getVandalism() {
        return vandalism;
    }

    public void setVandalism(String vandalism) {
        this.vandalism = vandalism;
    }

    public String getFreezeFlag() {
        return freezeFlag;
    }

    public void setFreezeFlag(String freezeFlag) {
        this.freezeFlag = freezeFlag;
    }

    public String getStorm() {
        return storm;
    }

    public void setStorm(String storm) {
        this.storm = storm;
    }

    public String getRoof() {
        return roof;
    }

    public void setRoof(String roof) {
        this.roof = roof;
    }

    public String getWater() {
        return water;
    }

    public void setWater(String water) {
        this.water = water;
    }

    public String getNaturalFlag() {
        return naturalFlag;
    }

    public void setNaturalFlag(String naturalFlag) {
        this.naturalFlag = naturalFlag;
    }

    public String getFire() {
        return fire;
    }

    public void setFire(String fire) {
        this.fire = fire;
    }

    public String getHazard() {
        return hazard;
    }

    public void setHazard(String hazard) {
        this.hazard = hazard;
    }

    public String getStructure() {
        return structure;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }

    public String getMold() {
        return mold;
    }

    public void setMold(String mold) {
        this.mold = mold;
    }

    public String getPump() {
        return pump;
    }

    public void setPump(String pump) {
        this.pump = pump;
    }
}
