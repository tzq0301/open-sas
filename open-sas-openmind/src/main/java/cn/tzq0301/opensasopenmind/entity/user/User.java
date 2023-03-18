package cn.tzq0301.opensasopenmind.entity.user;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.google.common.base.Preconditions.checkArgument;

@Table
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private long id;

    @Column
    private String username;

    @Column
    private String password;

    public User(String username, String password) {
        checkArgument(StringUtils.isNotBlank(username));
        checkArgument(StringUtils.isNotBlank(password));
        this.username = username;
        this.password = password;
    }
}
