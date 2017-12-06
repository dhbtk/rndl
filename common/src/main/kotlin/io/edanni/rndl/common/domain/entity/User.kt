package io.edanni.rndl.common.domain.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.threeten.bp.OffsetDateTime

data class User constructor(
        override val id: Long? = null,
        override val createdAt: OffsetDateTime? = null,
        override val updatedAt: OffsetDateTime? = null,
        val email: String? = null,
        val fullName: String? = null,
        @JsonIgnore val passwordDigest: String? = null
) : Entity, UserDetails {

    @JsonIgnore
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = ArrayList<GrantedAuthority>()

    @JsonIgnore
    override fun isEnabled(): Boolean = true

    @JsonIgnore
    override fun getUsername(): String = email!!

    @JsonIgnore
    override fun isCredentialsNonExpired(): Boolean = true

    @JsonIgnore
    override fun getPassword(): String = passwordDigest!!

    @JsonIgnore
    override fun isAccountNonExpired(): Boolean = true

    @JsonIgnore
    override fun isAccountNonLocked(): Boolean = true

    @JsonIgnore
    override fun getName(): String {
        return super.getName()
    }
}