package com.example.mobilele.config;

import com.example.mobilele.service.OfferService;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import java.util.UUID;

public class OwnerSecurityExpressionRoot
        extends SecurityExpressionRoot
        implements MethodSecurityExpressionOperations {

    private final Authentication authentication;
    private final OfferService offerService;
    private Object filterObject;
    private Object returnObject;

    /**
     * Creates a new instance
     *
     * @param authentication the {@link Authentication} to use. Cannot be null.
     * @param offerService
     */

    public OwnerSecurityExpressionRoot(Authentication authentication,
                                       OfferService offerService) {
        super(authentication);
        this.authentication = authentication;
        this.offerService = offerService;
    }

    public boolean isOwner(UUID id) {
        if (this.authentication.getPrincipal() == null) {
            return false;
        }

        String userName = authentication.getName();

        return offerService.isOwner(userName, id);
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return returnObject;
    }

    @Override
    public Object getThis() {
        return this;
    }
}
