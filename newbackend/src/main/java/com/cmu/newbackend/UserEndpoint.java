package com.cmu.newbackend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.ConflictException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.List;

import static com.cmu.newbackend.OfyService.ofy;

/**
 * Created by xing on 3/29/15.
 */
//@Api(name = "userEndpoint", version = "v1", namespace = @ApiNamespace(ownerDomain = "backend.cmu.com", ownerName = "backend.cmu.com", packagePath=""))
@Api(name = "userEndpoint", version = "v1", namespace = @ApiNamespace(ownerDomain = "newbackend.cmu.com", ownerName = "newbackend.cmu.com", packagePath=""))
public class UserEndpoint {
    public UserEndpoint(){
    }

    /**
     * Return a collection of users
     *
     * @param count The number of users
     * @return a list of Users
     */
    @ApiMethod(name = "listUser")
    public CollectionResponse<User> listUser(@Nullable @Named("cursor") String cursorString,
                                               @Nullable @Named("count") Integer count) {

        Query<User> query = ofy().load().type(User.class);
        if (count != null) query.limit(count);
        if (cursorString != null && cursorString != "") {
            query = query.startAt(Cursor.fromWebSafeString(cursorString));
        }

        List<User> records = new ArrayList<User>();
        QueryResultIterator<User> iterator = query.iterator();
        int num = 0;
        while (iterator.hasNext()) {
            records.add(iterator.next());
            if (count != null) {
                num++;
                if (num == count) break;
            }
        }

        //Find the next cursor
        if (cursorString != null && cursorString != "") {
            Cursor cursor = iterator.getCursor();
            if (cursor != null) {
                cursorString = cursor.toWebSafeString();
            }
        }
        return CollectionResponse.<User>builder().setItems(records).setNextPageToken(cursorString).build();
    }

    /**
     * Sign up: this inserts a new <code>User</code> object.
     * @param user The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "signUp")
    public User signUp(User user) throws ConflictException {
        //If if is not null, then check if it exists. If yes, throw an Exception
        //that it is already present

            if (user.getEmail() != null) {
                if (findRecord(user.getEmail()) != null) {
                    throw new ConflictException("User already exists");
                }
            }
            //Since our @Id field is a Long, Objectify will generate a unique value for us
            //when we use put
            ofy().save().entity(user).now();


            return user;

    }

    /**
     * Authenticate: this would check if the user's password is correct.
     * @param user The object to check
     * @return  The object after checking, if correct, its attribute loggedin would be true
     * @throws com.google.api.server.spi.response.NotFoundException
     */
    @ApiMethod(name = "authenticate")
    public User authenticate(User user) throws NotFoundException,UnauthorizedException{
        User userInDatastore = findRecord(user.getEmail());
        if ( userInDatastore == null) {
            throw new NotFoundException("User Record does not exist");
        }
        if(!user.getPassword().equals(userInDatastore.getPassword())){
            throw new UnauthorizedException("Invalid Password");

        }
        return user;
    }

    /**
     * This updates an existing <code>User</code> object.
     * @param user The object to be added.
     * @return The object to be updated.
     */
    @ApiMethod(name = "updateUser")
    public User updateUser(User user)throws NotFoundException {
        if (findRecord(user.getEmail()) == null) {
            throw new NotFoundException("User Record does not exist");
        }
        ofy().save().entity(user).now();
        return user;
    }

    /**
     * This deletes an existing <code>User</code> object.
     * @param email The id of the object to be deleted.
     */
    @ApiMethod(name = "removeUser")
    public void removeUser(@Named("id") String email) throws NotFoundException {
        User record = findRecord(email);
        if(record == null) {
            throw new NotFoundException("User Record does not exist");
        }
        ofy().delete().entity(record).now();
    }

    //Private method to retrieve a <code>User</code> record
    private User findRecord(String email) {
        return ofy().load().type(User.class).id(email).now();
//or return ofy().load().type(Quote.class).filter("id",id).first.now();
    }


}
