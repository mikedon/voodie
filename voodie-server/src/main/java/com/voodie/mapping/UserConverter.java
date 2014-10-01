package com.voodie.mapping;

import com.voodie.domain.identity.Authorities;
import com.voodie.domain.identity.User;
import org.dozer.CustomConverter;
import org.dozer.MappingException;

/**
 * Voodie
 * User: MikeD
 */
public class UserConverter implements CustomConverter {

    @Override
    public Object convert(Object destination, Object source, Class<?> destinationClass, Class<?> sourceClass) {
        if (source == null) {
            return null;
        }
        com.voodie.remote.types.identity.User remoteUser;
        if(source instanceof User){
            User domainUser = (User)source;
            if(destination == null){
                remoteUser = new com.voodie.remote.types.identity.User();
            }else{
                remoteUser = (com.voodie.remote.types.identity.User)destination;
            }
            remoteUser.setUsername(domainUser.getUsername());
            remoteUser.setFirstName(domainUser.getFirstName());
            remoteUser.setLastName(domainUser.getLastName());
            for(Authorities a : domainUser.getAuthorities()){
                remoteUser.getRoles().add(a.getAuthority());
            }
            return remoteUser;
        }else{
            throw new MappingException("Converter " + this.getClass().getSimpleName()
                    + "used incorrectly. Arguments passed in were:"
                    + destination + " and " + source);
        }

    }
}
