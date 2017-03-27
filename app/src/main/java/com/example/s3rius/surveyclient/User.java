package ru.ssau.domain;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class User implements UserDetails{

    private Integer      id;
    private String       name;
    private String       lastName;
    private String       login;
    private String       password;
    private List<Survey> surveys;
    private UserRoles    role;


    public User(){
    }

    public Integer getId(){
        return id;
    }

    public void setId( Integer id ){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName( String name ){
        this.name = name;
    }

    public String getLastName(){
        return lastName;
    }

    public void setLastName( String lastName ){
        this.lastName = lastName;
    }

    public String getLogin(){
        return login;
    }

    public void setLogin( String login ){
        this.login = login;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword( String password ){
        this.password = password;
    }

    public UserRoles getRole(){
        return role;
    }

    public void setRole( UserRoles role ){
        this.role = role;
    }

    public List<Survey> getSurveys(){
        return surveys;
    }

    public void setSurveys( List<Survey> surveys ){
        this.surveys = surveys;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        Collection<GrantedAuthority> roles = new HashSet();
        roles.add( new SimpleGrantedAuthority( this.getRole().name() ) );
        return roles;
    }

    @Override
    public String getUsername(){
        return this.getLogin();
    }

    @Override
    public boolean isAccountNonExpired(){
        return false;
    }

    @Override
    public boolean isAccountNonLocked(){
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return false;
    }

    @Override
    public boolean isEnabled(){
        return true;
    }
}
