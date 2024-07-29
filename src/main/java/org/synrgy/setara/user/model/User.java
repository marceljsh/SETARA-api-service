package org.synrgy.setara.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.synrgy.setara.common.model.Auditable;
import org.synrgy.setara.vendor.model.Bank;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Set;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_users")
public class User extends Auditable implements UserDetails {

  @ManyToOne
  @JoinColumn(name = "id_bank")
  private Bank bank;

  @Column(
    unique = true,
    nullable = false,
    updatable = false
  )
  private String email;

  @Column(
    unique = true,
    nullable = false
  )
  private String signature;

  @Column(
    unique = true,
    nullable = false,
    updatable = false
  )
  private String accountNumber;

  @Column(nullable = false)
  private String name;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Column(
    nullable = false,
    length = 60
  )
  private String password;

  private String imagePath;

  @Column(
    unique = true,
    nullable = false,
    updatable = false
  )
  private String nik;

  @Column(
    unique = true,
    nullable = false
  )
  private String phoneNumber;

  @Column(nullable = false)
  private String address;

  private BigDecimal balance;

  @Column(nullable = false)
  private String mpin;

  @Override
  public String getUsername() {
    return signature;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Set.of(new SimpleGrantedAuthority("ROLE_USER"));
  }
}
