package ar.edu.itba.iot.iot_android.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

public class User extends Observable implements Serializable{

    private String token = null;

    private List<Device> devices =  new LinkedList<>();

    private String username;

    private String password;

    private Long id = Long.valueOf(-1);

    private String fullName;

    private String email;

    private String logoutURL = null;

    public User(){

    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, Long id) {
        this.username = username;
        this.password = password;
        this.id = id;
    }

    public User(UserAux userAux){
        this.username = userAux.getUsername();
        this.id = userAux.getId();
        this.email = userAux.getEmail();
        this.fullName = userAux.getFullName();
        this.email = userAux.getEmail();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        updated("token");
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
        updated("id");
    }

    public void setLogoutURL(String logoutURL) {
        this.logoutURL = logoutURL;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
        updated("fullName");
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        updated("email");
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogoutURL() {
        return logoutURL;
    }

    public void addDevices(Collection<Device> devices){
        this.devices.addAll(devices);
        updated("deviceList");
    }

    public void setDevices(Collection<Device> devices){
        this.devices.clear();
        addDevices(devices);
    }

    public void updated(String tag){
        this.setChanged();
        this.notifyObservers(tag);
    }

}
