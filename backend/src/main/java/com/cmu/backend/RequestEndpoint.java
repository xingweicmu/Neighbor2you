package com.cmu.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.ConflictException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.List;

import static com.cmu.backend.OfyService.ofy;

/**
 * Created by xing on 3/31/15.
 */

@Api(name = "requestEndpoint", version = "v1", namespace = @ApiNamespace(ownerDomain = "backend.cmu.com", ownerName = "backend.cmu.com", packagePath=""))

public class RequestEndpoint {

    public RequestEndpoint(){
    }

    /**
     * Return a collection of requests
     *
     * @param count The number of requests
     * @return a list of Requests
     */
    @ApiMethod(name = "listRequest")
    public CollectionResponse<Request> listRequest(@Nullable @Named("cursor") String cursorString,
                                             @Nullable @Named("count") Integer count) {

        Query<Request> query = ofy().load().type(Request.class);
        if (count != null) query.limit(count);
        if (cursorString != null && cursorString != "") {
            query = query.startAt(Cursor.fromWebSafeString(cursorString));
        }

        List<Request> records = new ArrayList<Request>();
        QueryResultIterator<Request> iterator = query.iterator();
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
        return CollectionResponse.<Request>builder().setItems(records).setNextPageToken(cursorString).build();
    }

    @ApiMethod(name = "getRequest")
    public CollectionResponse<Request> getRequest(@Nullable @Named("cursor") String cursorString,
                                                   @Nullable @Named("count") Integer count,
                                                   @Named("latitude") float latitude,
                                                   @Named("longitude")float longitude
                                                    ) {

        Query<Request> query = ofy().load().type(Request.class).filter("latitude >=",-10.0).filter("latitude <=",10.0);
        List<Request> longs = ofy().load().type(Request.class).filter("longitude >=",-10.0).filter("longitude <=",10.0).list();
        List<Request> lats = ofy().load().type(Request.class).filter("latitude >=",-10.0).filter("latitude <=",10.0).list();
        List<Request> result = new ArrayList<Request>();
        for(Request lo: longs){
            for(Request la:lats){
                if(lo.getId() == la.getId()){
                    result.add(la);
                    break;
                }
            }
        }
        // The following not used temporarily
        if (count != null) query.limit(count);
        System.out.println(result.size());
        if (cursorString != null && cursorString != "") {
            query = query.startAt(Cursor.fromWebSafeString(cursorString));
        }

        List<Request> records = new ArrayList<Request>();
        QueryResultIterator<Request> iterator = query.iterator();
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

        return CollectionResponse.<Request>builder().setItems(result).setNextPageToken(cursorString).build();
    }


    /**
     * Sign up: this inserts a new <code>Request</code> object.
     * @param request The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "insertRequest")
    public Request signUp(Request request) throws ConflictException {
        //If if is not null, then check if it exists. If yes, throw an Exception
        //that it is already present

        if (request.getId() != null) {
            if (findRecord(request.getId()) != null) {
                throw new ConflictException("Record already exists");
            }
        }
        //Since our @Id field is a Long, Objectify will generate a unique value for us
        //when we use put
        ofy().save().entity(request).now();
        return request;

    }

    /**
     * This updates an existing <code>Request</code> object.
     * @param request The object to be added.
     * @return The object to be updated.
     */
    @ApiMethod(name = "updateRequest")
    public Request updateRequest(Request request)throws NotFoundException {
        if (findRecord(request.getId()) == null) {
            throw new NotFoundException("Request Record does not exist");
        }
        ofy().save().entity(request).now();
        return request;
    }

    /**
     * This deletes an existing <code>Request</code> object.
     * @param id The id of the object to be deleted.
     */
    @ApiMethod(name = "removeRequest")
    public void removeRequest(@Named("id") Long id) throws NotFoundException {
        Request record = findRecord(id);
        if (record == null) {
            throw new NotFoundException("Request Record does not exist");
        }
        ofy().delete().entity(record).now();
    }


    //Private method to retrieve a <code>Request</code> record
    private Request findRecord(Long id) {
        return ofy().load().type(Request.class).id(id).now();
//or return ofy().load().type(Quote.class).filter("id",id).first.now();
    }


}
