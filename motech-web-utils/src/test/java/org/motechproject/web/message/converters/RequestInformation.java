package org.motechproject.web.message.converters;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public  class RequestInformation {

    @XmlElement
    private String message;

    public RequestInformation() {

    }

    public RequestInformation(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestInformation that = (RequestInformation) o;

        return new EqualsBuilder()
                .append(this.message, that.message)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.message)
                .toHashCode();
    }
}