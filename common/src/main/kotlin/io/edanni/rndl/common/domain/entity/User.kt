package io.edanni.rndl.common.domain.entity

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.threeten.bp.OffsetDateTime

data class User constructor(
        override val id: Long? = null,
        override val createdAt: OffsetDateTime? = null,
        override val updatedAt: OffsetDateTime? = null,
        val email: String? = null,
        val fullName: String? = null,
        val passwordDigest: String? = null
) : Entity, UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = ArrayList<GrantedAuthority>()

    override fun isEnabled(): Boolean = true

    override fun getUsername(): String = email!!

    override fun isCredentialsNonExpired(): Boolean = true

    override fun getPassword(): String = passwordDigest!!

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true
}