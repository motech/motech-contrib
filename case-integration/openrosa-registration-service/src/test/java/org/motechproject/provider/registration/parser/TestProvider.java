package org.motechproject.provider.registration.parser;

import org.motechproject.provider.registration.contract.OpenRosaXmlRequest;

public class TestProvider implements OpenRosaXmlRequest {
    private String primary_mobile;
    private String secondary_mobile;
    private String tertiary_mobile;
    private String provider_id;
    private String district;

    private String username;
    private String password;
    private String uuid;
    private String date;
    private String api_key;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPrimary_mobile() {
        return primary_mobile;
    }

    public void setPrimary_mobile(String primary_mobile) {
        this.primary_mobile = primary_mobile;
    }

    public String getSecondary_mobile() {
        return secondary_mobile;
    }

    public void setSecondary_mobile(String secondary_mobile) {
        this.secondary_mobile = secondary_mobile;
    }

    public String getTertiary_mobile() {
        return tertiary_mobile;
    }

    public void setTertiary_mobile(String tertiary_mobile) {
        this.tertiary_mobile = tertiary_mobile;
    }

    public String getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(String provider_id) {
        this.provider_id = provider_id;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    @Override
    public String getId() {
        return "test Id";
    }

    @Override
    public String getType() {
        return "test Type";
    }
}
