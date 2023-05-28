package adeo.leroymerlin.cdp.domain.band;

import adeo.leroymerlin.cdp.domain.member.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.util.Set;

@Entity
public class Band {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany(fetch=FetchType.EAGER)
    private Set<Member> members;

    @JsonIgnore
    @Transient
    private int memberCountForFilter;

    public void setMemberCountForFilter(int memberCountForFilter) {
        this.memberCountForFilter = memberCountForFilter;
    }

    public Set<Member> getMembers() {
        return members;
    }

    public void setMembers(Set<Member> members) {
        this.members = members;
    }

    public String getName() {
        if(memberCountForFilter > 0) {
            return name + " [" + memberCountForFilter + "]";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
