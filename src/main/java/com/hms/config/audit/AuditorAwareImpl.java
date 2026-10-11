package com.hms.config.audit;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Supplies the value for @CreatedBy / @LastModifiedBy fields on entities
 * extending AudiLogEntity.
 *
 * - If a request is authenticated (a real logged-in user via JWT), returns
 *   their email/username from the security context.
 * - If there is no authenticated user (e.g. RoleSeeder running at app
 *   startup, before any HTTP request has ever happened), falls back to
 *   "SYSTEM" so NOT NULL created_by/updated_by columns never fail.
 */
@Component("auditorAware")
public class AuditorAwareImpl implements AuditorAware<String> {

    private static final String SYSTEM_AUDITOR = "SYSTEM";

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.of(SYSTEM_AUDITOR);
        }

        return Optional.of(authentication.getName());
    }
}