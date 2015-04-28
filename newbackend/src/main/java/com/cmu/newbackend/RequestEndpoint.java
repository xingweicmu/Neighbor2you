package com.cmu.newbackend;

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
import java.util.Collections;
import java.util.List;

import static com.cmu.newbackend.OfyService.ofy;

/**
 * Created by xing on 3/31/15.
 */

//@Api(name = "requestEndpoint", version = "v1", namespace = @ApiNamespace(ownerDomain = "backend.cmu.com", ownerName = "backend.cmu.com", packagePath=""))
@Api(name = "request", version = "v1", namespace = @ApiNamespace(ownerDomain = "newbackend.cmu.com", ownerName = "newbackend.cmu.com", packagePath = ""))

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

    /**
     * Retrieve surrounding requests given a certain geo point.
     * @param cursorString
     * @param count
     * @param latitude latitude of the point
     * @param longitude longitude of the ppint
     * @return a list of Requests
     */
    @ApiMethod(name = "getRequest")
    public CollectionResponse<Request> getRequestBasedOnLocation(@Nullable @Named("cursor") String cursorString,
                                                   @Nullable @Named("count") Integer count,
                                                   @Named("latitude") double latitude,
                                                   @Named("longitude")double longitude
                                                    ) {

//        Query<Request> query = ofy().load().type(Request.class).filter("latitude >=",-10.0).filter("latitude <=",10.0);
        List<Request> longs = ofy().load().type(Request.class).filter("longitude >=",longitude-0.003).filter("longitude <=",longitude+0.003).filter("accepted ==",false).list();
        List<Request> lats = ofy().load().type(Request.class).filter("latitude >=",latitude-0.05).filter("latitude <=",latitude+0.05).filter("accepted ==",false).list();
        List<Request> result = new ArrayList<Request>();
        for(Request lo: longs){
            for(Request la:lats){
                if(lo.getId() == la.getId()){
                    double distance = CalculateLength(la.getLatitude(),la.getLongitude(),latitude,longitude);
                    System.out.println(la.getLatitude()+" "+la.getLongitude() +" "+distance);
                    la.setDistance(distance);
                    result.add(la);
                    break;
                }
            }
        }

        Collections.sort(result);
        // The following not used temporarily
//        if (count != null) query.limit(count);
//        System.out.println(result.size());
//        if (cursorString != null && cursorString != "") {
//            query = query.startAt(Cursor.fromWebSafeString(cursorString));
//        }
//
//        List<Request> records = new ArrayList<Request>();
//        QueryResultIterator<Request> iterator = query.iterator();
//        int num = 0;
//        while (iterator.hasNext()) {
//            records.add(iterator.next());
//            if (count != null) {
//                num++;
//                if (num == count) break;
//            }
//        }

        //Find the next cursor
//        if (cursorString != null && cursorString != "") {
//            Cursor cursor = iterator.getCursor();
//            if (cursor != null) {
//                cursorString = cursor.toWebSafeString();
//            }
//        }

        return CollectionResponse.<Request>builder().setItems(result).setNextPageToken(cursorString).build();
    }

    /**
     * Retrieve accepted requests of acceptor
     * @param cursorString
     * @param count
     * @param acceptorName acceptor name
     * @return a list of requests
     */
    @ApiMethod(name = "getRequestBasedOnAcceptor", httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<Request> acceptedRequest(@Nullable @Named("cursor") String cursorString,
                                                                 @Nullable @Named("count") Integer count,
                                                                 @Named("acceptor") String acceptorName
    ) {

        Query<Request> query = ofy().load().type(Request.class).filter("acceptor",acceptorName).order("-status");
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

    /**
     * Retrieve posted Request of a requester.
     * Order by postedTime.
     * @param cursorString
     * @param count
     * @param requesterName requestor name
     * @return a list of requests
     */
    @ApiMethod(name = "getRequestBasedOnRequester", httpMethod = ApiMethod.HttpMethod.GET) //TODO Order by deadline, but seems ordering by posted time makes more sense
    public CollectionResponse<Request> postedRequest(@Nullable @Named("cursor") String cursorString,
                                                          @Nullable @Named("count") Integer count,
                                                          @Named("requester") String requesterName
    ) {

        List<Request> records = ofy().load().type(Request.class).filter("requester",requesterName).order("deadline").list();
//        if (count != null) query.limit(count);
//        if (cursorString != null && cursorString != "") {
//            query = query.startAt(Cursor.fromWebSafeString(cursorString));
//        }
//
//        List<Request> records = new ArrayList<Request>();
//        QueryResultIterator<Request> iterator = query.iterator();
//        int num = 0;
//        while (iterator.hasNext()) {
//            records.add(iterator.next());
//            if (count != null) {
//                num++;
//                if (num == count) break;
//            }
//        }
//
//        //Find the next cursor
//        if (cursorString != null && cursorString != "") {
//            Cursor cursor = iterator.getCursor();
//            if (cursor != null) {
//                cursorString = cursor.toWebSafeString();
//            }
//        }
        return CollectionResponse.<Request>builder().setItems(records).setNextPageToken(cursorString).build();
    }

    /**
     * Calculate distance between two geo point.
     * @param lat1 latitude of point 1
     * @param long1 longitude of point 1
     * @param lat2 latitude of point 2
     * @param long2 latitude of point 2
     * @return the distance
     */
    private double CalculateLength(double lat1, double long1, double lat2, double long2)
    {
        double d2r = Math.PI/180;
        double dlong = (long2 - long1) * d2r;
        double dlat = (lat2 - lat1) * d2r;
        double a = Math.pow(Math.sin(dlat/2.0), 2) + Math.cos(lat1*d2r) * Math.cos(lat2*d2r) * Math.pow(Math.sin(dlong/2.0), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = 6367 * c; // in km; 3956 in mile

        return d;
    }


    /**
     * Sign up: this inserts a new <code>Request</code> object.
     * @param request The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "insertRequest")
    public Request insertRequest(Request request) throws ConflictException {
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

        //invoke MessagingEndpoint to send notification
        try {
            MessagingEndpoint messagingEndpoint = new MessagingEndpoint();
            //Message to be sent should be read from the Request object
            String email = request.getAcceptor();
            String content = request.getStatus().toString();
            messagingEndpoint.sendMessage("test","123@test.com");
        }catch (Exception e){
            e.printStackTrace();
        }
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

    @ApiMethod(name = "getRequestById")
    public Request getRequestById(@Named("id") Long id) throws NotFoundException {
        Request record = findRecord(id);
        if (record == null) {
            throw new NotFoundException("Request Record does not exist");
        }
        return ofy().load().type(Request.class).id(id).now();
    }


    //Private method to retrieve a <code>Request</code> record
    private Request findRecord(Long id) {
        return ofy().load().type(Request.class).id(id).now();
//or return ofy().load().type(Quote.class).filter("id",id).first.now();
    }


}
