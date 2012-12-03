package org.motechproject.web.message.converters;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class Request {

    @XmlElement
    private String name;

    @XmlElement
    private int age;

    @XmlElement(name = "information")
    private RequestInformation requestInformation;

    @XmlElement(name = "nickname")
    @XmlElementWrapper(name = "nicknames")
    private List<String> nickNames;

    public Request() {

    }

    public Request(String name, int age, RequestInformation requestInformation, List<String> nicknames) {
        this.name = name;
        this.age = age;
        this.requestInformation = requestInformation;
        this.nickNames = nicknames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Request that = (Request) o;

        return new EqualsBuilder()
                .append(this.name, that.name)
                .append(this.age, that.age)
                .append(this.nickNames, that.nickNames)
                .append(this.requestInformation, that.requestInformation)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.name)
                .append(this.age)
                .append(this.nickNames)
                .append(this.requestInformation)
                .toHashCode();
    }
}
