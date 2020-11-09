package by.epamtc.zarutski.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

public class Operation implements Serializable {

    private static final long serialVersionUID = 7393408681746082352L;

    private int id;
    private Timestamp date;
    private boolean allowed;
    private String operationName;
    private OperationDetail detail;

    public Operation() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public OperationDetail getDetail() {
        return detail;
    }

    public void setDetail(OperationDetail detail) {
        this.detail = detail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operation operation = (Operation) o;
        return id == operation.id &&
                allowed == operation.allowed &&
                Objects.equals(date, operation.date) &&
                Objects.equals(operationName, operation.operationName) &&
                Objects.equals(detail, operation.detail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, allowed, operationName, detail);
    }

    @Override
    public String toString() {
        return "Operation{" +
                "id=" + id +
                ", date=" + date +
                ", allowed=" + allowed +
                ", operationName='" + operationName + '\'' +
                ", detail=" + detail +
                '}';
    }
}