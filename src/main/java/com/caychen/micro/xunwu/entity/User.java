package com.caychen.micro.xunwu.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Author:       Caychen
 * Date:         2019/9/7
 * Desc:
 */
@Entity
@Data
@Table(name = "user")
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;

	private String password;

	private String email;

	@Column(name = "phone_number")
	private String phoneNumber;

	private int status;

	@Column(name = "create_time")
	private Date createTime;

	@Column(name = "last_login_time")
	private Date lastLoginTime;

	@Column(name = "last_update_time")
	private Date lastUpdateTime;

	private String avatar;//头像

	@Transient
	private List<GrantedAuthority> authorityList;//权限

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorityList;
	}

	@Override
	public String getUsername() {
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
