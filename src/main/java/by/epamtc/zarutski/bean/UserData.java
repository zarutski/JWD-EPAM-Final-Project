package by.epamtc.zarutski.bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class UserData implements Serializable {

	private static final long serialVersionUID = 6899855851114675364L;
	
	private int userId;
    private String email;
    private String name;
    private String surname;
    private String patronymic;
    private String phoneNumber;
    private String passportSeries;
    private String passportNumber;
    private LocalDate dateOfBirth;
    private String address;
    private String postCode;
    private String photoLink;
    private String roleName;


    public UserData() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassportSeries() {
        return passportSeries;
    }

    public void setPassportSeries(String passportSeries) {
        this.passportSeries = passportSeries;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getPhotoLink() {
        return photoLink;
    }

    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserData userData = (UserData) o;
        return userId == userData.userId &&
                Objects.equals(email, userData.email) &&
                Objects.equals(name, userData.name) &&
                Objects.equals(surname, userData.surname) &&
                Objects.equals(patronymic, userData.patronymic) &&
                Objects.equals(phoneNumber, userData.phoneNumber) &&
                Objects.equals(passportSeries, userData.passportSeries) &&
                Objects.equals(passportNumber, userData.passportNumber) &&
                Objects.equals(dateOfBirth, userData.dateOfBirth) &&
                Objects.equals(address, userData.address) &&
                Objects.equals(postCode, userData.postCode) &&
                Objects.equals(photoLink, userData.photoLink) &&
                Objects.equals(roleName, userData.roleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, email, name, surname, patronymic, phoneNumber, passportSeries, passportNumber, dateOfBirth, address, postCode, photoLink, roleName);
    }

    @Override
    public String toString() {
        return "UserData{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", passportSeries='" + passportSeries + '\'' +
                ", passportNumber='" + passportNumber + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", address='" + address + '\'' +
                ", postCode='" + postCode + '\'' +
                ", photoLink='" + photoLink + '\'' +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}
