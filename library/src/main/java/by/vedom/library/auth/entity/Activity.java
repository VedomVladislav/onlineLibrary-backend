package by.vedom.library.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
@DynamicUpdate
public class Activity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Type(type = "org.hibernate.type.NumericBooleanType") // convert short to boolean
    @Column
    private boolean activated;

    @NotBlank
    @Column(updatable = false)
    private String uuid; // need for user activation

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public String getUuid() {
        return uuid;
    }
}
