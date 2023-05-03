
package aiss.githubminer.model;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "admin",
    "maintain",
    "push",
    "triage",
    "pull"
})
@Generated("jsonschema2pojo")
public class Permissions {

    @JsonProperty("admin")
    private Boolean admin;
    @JsonProperty("maintain")
    private Boolean maintain;
    @JsonProperty("push")
    private Boolean push;
    @JsonProperty("triage")
    private Boolean triage;
    @JsonProperty("pull")
    private Boolean pull;

    @JsonProperty("admin")
    public Boolean getAdmin() {
        return admin;
    }

    @JsonProperty("admin")
    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    @JsonProperty("maintain")
    public Boolean getMaintain() {
        return maintain;
    }

    @JsonProperty("maintain")
    public void setMaintain(Boolean maintain) {
        this.maintain = maintain;
    }

    @JsonProperty("push")
    public Boolean getPush() {
        return push;
    }

    @JsonProperty("push")
    public void setPush(Boolean push) {
        this.push = push;
    }

    @JsonProperty("triage")
    public Boolean getTriage() {
        return triage;
    }

    @JsonProperty("triage")
    public void setTriage(Boolean triage) {
        this.triage = triage;
    }

    @JsonProperty("pull")
    public Boolean getPull() {
        return pull;
    }

    @JsonProperty("pull")
    public void setPull(Boolean pull) {
        this.pull = pull;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Permissions.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("admin");
        sb.append('=');
        sb.append(((this.admin == null)?"<null>":this.admin));
        sb.append(',');
        sb.append("maintain");
        sb.append('=');
        sb.append(((this.maintain == null)?"<null>":this.maintain));
        sb.append(',');
        sb.append("push");
        sb.append('=');
        sb.append(((this.push == null)?"<null>":this.push));
        sb.append(',');
        sb.append("triage");
        sb.append('=');
        sb.append(((this.triage == null)?"<null>":this.triage));
        sb.append(',');
        sb.append("pull");
        sb.append('=');
        sb.append(((this.pull == null)?"<null>":this.pull));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
