package by.epamtc.zarutski.bean;

import java.io.Serializable;
import java.util.Objects;

public class AuthenticationData implements Serializable {

    private static final long serialVersionUID = -6826078765905106111L;

    private int userId;
    private String userRole;

    public AuthenticationData() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthenticationData that = (AuthenticationData) o;
        return userId == that.userId &&
                Objects.equals(userRole, that.userRole);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, userRole);
    }

    @Override
    public String toString() {
        return "AuthenticationData{" +
                "userId=" + userId +
                ", userRole='" + userRole + '\'' +
                '}';
    }
}