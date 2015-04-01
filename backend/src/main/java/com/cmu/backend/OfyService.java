package com.cmu.backend;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

/**
 * Created by xing on 3/29/15.
 */
public class OfyService {

    /**
     * Objectify service wrapper so we can statically register our persistence classes
     * More on Objectify here : https://code.google.com/p/objectify-appengine/
     *
     */


        static {
            ObjectifyService.register(User.class);
            ObjectifyService.register(Request.class);
        }

        public static Objectify ofy() {
            return ObjectifyService.ofy();
        }

        public static ObjectifyFactory factory() {
            return ObjectifyService.factory();
        }



}
